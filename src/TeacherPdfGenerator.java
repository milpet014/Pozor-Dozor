import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.awt.Color;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.rendering.PDFRenderer;

public class TeacherPdfGenerator
{
    // Názov mesiaca sa používa aj mimo tejto triedy, napríklad pri predvolenom názve ukladaného PDF.
    public static String monthName = "";

    // Pevne nastavené slovenské locale, aby názvy mesiacov neboli závislé od systému.
    private static final Locale SK_LOCALE = new Locale("sk", "SK");

    // Text nadpisu PDF pred názvom mesiaca a rokom.
    private static final String TITLE_PREFIX = "DOZOR VO VESTIBULE";

    // Súradnice a rozostupy v PDF.
    // PDFBox používa súradnicový systém, kde bod [0,0] je vľavo dole.
    private static final float TITLE_Y = 750;
    private static final float LIST_START_Y = 700;
    private static final float LIST_LINE_HEIGHT = 22;

    // X súradnice stĺpcov v zozname dozorov.
    private static final float DATE_X = 70;
    private static final float TEACHERS_X = 120;

    // Súradnice poznámky pod čiarou.
    private static final float FOOTNOTE_LINE_Y = 105;
    private static final float FOOTNOTE_TEXT_X = 70;
    private static final float FOOTNOTE_TEXT_Y = 78;

    public TeacherPdfGenerator(){}

    public void generateTeacherListPdf(List<DutyEntry> duties, YearMonth month) throws IOException
    {
        // Načítanie PDF nastavení z konfigurácie.
        AppConfig config = new AppConfig();

        float titleFontSize = config.getPdfTitleFontSize();
        float dutyFontSize = config.getPdfDutyFontSize();
        float footnoteFontSize = config.getPdfFootnoteFontSize();
        String footnoteText = config.getPdfFootnoteText();

        // Slovenský názov mesiaca pre nadpis a názov ukladaného PDF.
        monthName = month.getMonth().getDisplayName(TextStyle.FULL_STANDALONE, SK_LOCALE);

        // Príprava dátového priečinka a odstránenie starého PDF.
        Files.createDirectories(AppPath.appDataPath());
        Files.deleteIfExists(AppPath.pdfPath());

        // Vytvorenie nového PDF dokumentu.
        try(PDDocument document = new PDDocument())
        {
            PDType0Font regularFont;
            PDType0Font boldFont;

            // Načítanie fontov z resources.
            // PDType0Font umožňuje korektné zobrazovanie diakritiky.
            try(InputStream regularStream = requireResource("fonts/NotoSans-Regular.ttf");
                InputStream boldStream = requireResource("fonts/NotoSans-Bold.ttf"))
            {
                regularFont = PDType0Font.load(document, regularStream);
                boldFont = PDType0Font.load(document, boldStream);
            }

            // Vytvorenie jednej A4 strany.
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            // Content stream slúži na kreslenie textu a čiar do PDF.
            try(PDPageContentStream contentStream = new PDPageContentStream(document, page))
            {
                drawTitle(contentStream, boldFont, month, titleFontSize);
                drawDutyList(contentStream, regularFont, duties, dutyFontSize);
                drawFootnotes(contentStream, regularFont, footnoteText, footnoteFontSize);
            }

            // Uloženie hotového PDF do dátového priečinka aplikácie.
            document.save(AppPath.pdfPath().toFile());
        }
    }

    private void drawTitle(PDPageContentStream contentStream, PDType0Font boldFont, YearMonth month, float titleFontSize) throws IOException
    {
        // Zloženie nadpisu, napríklad: DOZOR VO VESTIBULE MÁJ 2026.
        String title = TITLE_PREFIX + " " + monthName.toUpperCase(SK_LOCALE) + " " + month.getYear();

        // Nadpis sa vykresľuje na stred strany.
        drawCenteredText(contentStream, boldFont, titleFontSize, title, TITLE_Y);
    }

    private void drawDutyList(
            PDPageContentStream contentStream,
            PDType0Font regularFont,
            List<DutyEntry> duties,
            float dutyFontSize
    ) throws IOException
    {
        // Aktuálna Y pozícia prvého riadku zoznamu.
        float currentY = LIST_START_Y;

        // Ak generátor nevrátil žiadny deň, do PDF sa vloží informačný text.
        if(duties.isEmpty())
        {
            writeText(
                    contentStream,
                    regularFont,
                    dutyFontSize,
                    DATE_X,
                    currentY,
                    "Žiadne dni na generovanie."
            );
            return;
        }

        // Každý DutyEntry predstavuje jeden riadok v PDF.
        for(DutyEntry duty : duties)
        {
            // Jednoduchá ochrana pred zapisovaním do priestoru poznámky pod čiarou.
            // Ak by bolo riadkov priveľa, ďalšie sa už nevykreslia.
            if(currentY < FOOTNOTE_LINE_Y + 35)
            {
                break;
            }

            // Ľavý stĺpec: dátum.
            writeText(contentStream, regularFont, dutyFontSize, DATE_X, currentY, buildDateText(duty));

            // Pravý stĺpec: dvojica učiteľov.
            writeText(contentStream, regularFont, dutyFontSize, TEACHERS_X, currentY, buildTeachersText(duty));

            // Posun na ďalší riadok.
            currentY -= LIST_LINE_HEIGHT;
        }
    }

    private void drawFootnotes(PDPageContentStream contentStream, PDType0Font regularFont, String footnoteText, float footnoteFontSize) throws IOException
    {
        // Súradnice vodorovnej deliacej čiary nad poznámkou.
        float lineStartX = 70;
        float lineEndX = 525;

        // Tenká sivá čiara v štýle poznámky pod čiarou.
        contentStream.setStrokingColor(new Color(120, 120, 120));
        contentStream.setLineWidth(0.5f);
        contentStream.moveTo(lineStartX, FOOTNOTE_LINE_Y);
        contentStream.lineTo(lineEndX, FOOTNOTE_LINE_Y);
        contentStream.stroke();

        // Text poznámky sa kreslí čiernou farbou.
        contentStream.setNonStrokingColor(Color.BLACK);

        // Poznámka môže mať viac riadkov.
        String[] footnoteLines = footnoteText.split("\\R");

        float currentY = FOOTNOTE_TEXT_Y;
        float lineHeight = footnoteFontSize + 8;

        for(String line : footnoteLines)
        {
            // Prázdny riadok vytvorí medzeru.
            if(line.trim().isEmpty())
            {
                currentY -= lineHeight;
                continue;
            }

            // Vykreslenie jedného riadku poznámky.
            writeText(contentStream, regularFont, footnoteFontSize, FOOTNOTE_TEXT_X, currentY, line.trim());

            currentY -= lineHeight;
        }
    }

    private String buildDateText(DutyEntry duty)
    {
        // Formát dátumu v PDF, napríklad 5.5.
        return duty.getDate().getDayOfMonth() + "." + duty.getDate().getMonthValue() + ".";
    }

    private String buildTeachersText(DutyEntry duty)
    {
        // Formát dvojice učiteľov v PDF.
        return duty.getFirstTeacher().getFullName() + " - " + duty.getSecondTeacher().getFullName();
    }

    private void drawCenteredText(PDPageContentStream contentStream, PDType0Font font, float fontSize, String text, float y) throws IOException
    {
        // Šírka A4 strany v bodoch.
        float pageWidth = PDRectangle.A4.getWidth();

        // PDFBox vracia šírku textu v tisícinách veľkosti fontu, preto sa prepočítava.
        float textWidth = font.getStringWidth(text) / 1000 * fontSize;

        // Výpočet X pozície tak, aby bol text horizontálne vycentrovaný.
        float x = (pageWidth - textWidth) / 2;

        writeText(contentStream, font, fontSize, x, y, text);
    }

    private void writeText(PDPageContentStream contentStream, PDType0Font font, float fontSize, float x, float y, String text) throws IOException
    {
        // Zápis jedného textového úseku na presnú pozíciu v PDF.
        contentStream.beginText();
        contentStream.setFont(font, fontSize);
        contentStream.newLineAtOffset(x, y);
        contentStream.showText(text);
        contentStream.endText();
    }

    private InputStream requireResource(String path) throws IOException
    {
        // Načítanie súboru z resources v JAR-e alebo z classpath.
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(path);

        // Ak resource neexistuje, ide o chybu balenia aplikácie.
        if(inputStream == null)
        {
            throw new IOException("Resource not found: " + path);
        }

        return inputStream;
    }

    public Path renderTeacherListPreview() throws IOException
    {
        // Odstránenie starého PNG náhľadu.
        Files.deleteIfExists(AppPath.pngPath());

        // Načítanie vygenerovaného PDF dokumentu.
        try(PDDocument document = Loader.loadPDF(AppPath.pdfPath().toFile()))
        {
            PDFRenderer renderer = new PDFRenderer(document);

            // Renderuje sa prvá strana PDF ako obrázok s rozlíšením 180 DPI.
            BufferedImage previewImage = renderer.renderImageWithDPI(0, 180);

            // Uloženie PNG náhľadu do dátového priečinka aplikácie.
            ImageIO.write(previewImage, "PNG", AppPath.pngPath().toFile());
        }

        // Vracia cestu k vytvorenému PNG súboru.
        return AppPath.pngPath();
    }
}