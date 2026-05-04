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
    public static String monthName = "";

    private static final Locale SK_LOCALE = new Locale("sk", "SK");

    private static final String TITLE_PREFIX = "DOZOR VO VESTIBULE";

    private static final float TITLE_Y = 750;
    private static final float LIST_START_Y = 700;
    private static final float LIST_LINE_HEIGHT = 22;

    private static final float DATE_X = 70;
    private static final float TEACHERS_X = 120;

    private static final float FOOTNOTE_LINE_Y = 105;
    private static final float FOOTNOTE_TEXT_X = 70;
    private static final float FOOTNOTE_TEXT_Y = 78;

    public TeacherPdfGenerator(){}

    public void generateTeacherListPdf(List<DutyEntry> duties, YearMonth month) throws IOException
    {
        AppConfig config = new AppConfig();

        float titleFontSize = config.getPdfTitleFontSize();
        float dutyFontSize = config.getPdfDutyFontSize();
        float footnoteFontSize = config.getPdfFootnoteFontSize();
        String footnoteText = config.getPdfFootnoteText();

        monthName = month.getMonth().getDisplayName(
                TextStyle.FULL_STANDALONE,
                SK_LOCALE
        );

        Files.createDirectories(AppPath.appDataPath());
        Files.deleteIfExists(AppPath.pdfPath());

        try(PDDocument document = new PDDocument())
        {
            PDType0Font regularFont;
            PDType0Font boldFont;

            try(InputStream regularStream = requireResource("fonts/NotoSans-Regular.ttf");
                InputStream boldStream = requireResource("fonts/NotoSans-Bold.ttf"))
            {
                regularFont = PDType0Font.load(document, regularStream);
                boldFont = PDType0Font.load(document, boldStream);
            }

            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            try(PDPageContentStream contentStream = new PDPageContentStream(document, page))
            {
                drawTitle(contentStream, boldFont, month, titleFontSize);
                drawDutyList(contentStream, regularFont, duties, dutyFontSize);
                drawFootnotes(contentStream, regularFont, footnoteText, footnoteFontSize);
            }

            document.save(AppPath.pdfPath().toFile());
        }
    }

    private void drawTitle(
            PDPageContentStream contentStream,
            PDType0Font boldFont,
            YearMonth month,
            float titleFontSize
    ) throws IOException
    {
        String title = TITLE_PREFIX + " " +
                monthName.toUpperCase(SK_LOCALE) + " " +
                month.getYear();

        drawCenteredText(contentStream, boldFont, titleFontSize, title, TITLE_Y);
    }

    private void drawDutyList(
            PDPageContentStream contentStream,
            PDType0Font regularFont,
            List<DutyEntry> duties,
            float dutyFontSize
    ) throws IOException
    {
        float currentY = LIST_START_Y;

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

        for(DutyEntry duty : duties)
        {
            if(currentY < FOOTNOTE_LINE_Y + 35)
            {
                break;
            }

            writeText(
                    contentStream,
                    regularFont,
                    dutyFontSize,
                    DATE_X,
                    currentY,
                    buildDateText(duty)
            );

            writeText(
                    contentStream,
                    regularFont,
                    dutyFontSize,
                    TEACHERS_X,
                    currentY,
                    buildTeachersText(duty)
            );

            currentY -= LIST_LINE_HEIGHT;
        }
    }

    private void drawFootnotes(
            PDPageContentStream contentStream,
            PDType0Font regularFont,
            String footnoteText,
            float footnoteFontSize
    ) throws IOException
    {
        float lineStartX = 70;
        float lineEndX = 525;

        contentStream.setStrokingColor(new Color(120, 120, 120));
        contentStream.setLineWidth(0.5f);
        contentStream.moveTo(lineStartX, FOOTNOTE_LINE_Y);
        contentStream.lineTo(lineEndX, FOOTNOTE_LINE_Y);
        contentStream.stroke();

        contentStream.setNonStrokingColor(Color.BLACK);

        String[] footnoteLines = footnoteText.split("\\R");

        float currentY = FOOTNOTE_TEXT_Y;
        float lineHeight = footnoteFontSize + 8;

        for(String line : footnoteLines)
        {
            if(line.trim().isEmpty())
            {
                currentY -= lineHeight;
                continue;
            }

            writeText(
                    contentStream,
                    regularFont,
                    footnoteFontSize,
                    FOOTNOTE_TEXT_X,
                    currentY,
                    line.trim()
            );

            currentY -= lineHeight;
        }
    }

    private String buildDateText(DutyEntry duty)
    {
        return duty.getDate().getDayOfMonth() + "." +
                duty.getDate().getMonthValue() + ".";
    }

    private String buildTeachersText(DutyEntry duty)
    {
        return duty.getFirstTeacher().getFullName() +
                " - " +
                duty.getSecondTeacher().getFullName();
    }

    private void drawCenteredText(
            PDPageContentStream contentStream,
            PDType0Font font,
            float fontSize,
            String text,
            float y
    ) throws IOException
    {
        float pageWidth = PDRectangle.A4.getWidth();
        float textWidth = font.getStringWidth(text) / 1000 * fontSize;
        float x = (pageWidth - textWidth) / 2;

        writeText(contentStream, font, fontSize, x, y, text);
    }

    private void writeText(
            PDPageContentStream contentStream,
            PDType0Font font,
            float fontSize,
            float x,
            float y,
            String text
    ) throws IOException
    {
        contentStream.beginText();
        contentStream.setFont(font, fontSize);
        contentStream.newLineAtOffset(x, y);
        contentStream.showText(text);
        contentStream.endText();
    }

    private InputStream requireResource(String path) throws IOException
    {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(path);

        if(inputStream == null)
        {
            throw new IOException("Resource not found: " + path);
        }

        return inputStream;
    }

    public Path renderTeacherListPreview() throws IOException
    {
        Files.deleteIfExists(AppPath.pngPath());

        try(PDDocument document = Loader.loadPDF(AppPath.pdfPath().toFile()))
        {
            PDFRenderer renderer = new PDFRenderer(document);

            BufferedImage previewImage = renderer.renderImageWithDPI(0, 180);

            ImageIO.write(previewImage, "PNG", AppPath.pngPath().toFile());
        }

        return AppPath.pngPath();
    }
}