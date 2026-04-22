import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.rendering.PDFRenderer;

public class TeacherPdfGenerator
{
    public TeacherPdfGenerator(){};

    private final Path pdfPath = AppPath.appDataPath().resolve("teachers.pdf");
    private final Path pngPath = AppPath.appDataPath().resolve("teachers_preview.png");

    public void generateTeacherListPdf() throws IOException
    {
        LocalDate date = LocalDate.now();
        String monthName = date.getMonth().getDisplayName(TextStyle.FULL_STANDALONE, new Locale("sk", "SK"));

        Files.createDirectories(AppPath.appDataPath());
        Files.deleteIfExists(pdfPath);

        try (PDDocument document = new PDDocument())
        {
            PDType0Font regularFont;
            PDType0Font boldFont;

            try (InputStream regularStream = requireResource("fonts/NotoSans-Regular.ttf");
                 InputStream boldStream = requireResource("fonts/NotoSans-Bold.ttf"))
            {
                regularFont = PDType0Font.load(document, regularStream);
                boldFont = PDType0Font.load(document, boldStream);
            }

            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page))
            {
                contentStream.beginText();
                contentStream.setFont(boldFont, 18);
                contentStream.newLineAtOffset(230, 780);
                contentStream.showText("DOZOR " + monthName.toUpperCase(new Locale("sk", "SK")));
                contentStream.endText();

                contentStream.beginText();
                contentStream.setFont(regularFont, 12);
                contentStream.newLineAtOffset(80, 730);
                contentStream.showText("1. Mgr. Jan Novák");
                contentStream.newLineAtOffset(0, -20);
                contentStream.showText("2. Eva Kováčová");
                contentStream.newLineAtOffset(0, -20);
                contentStream.showText("3. Peter Mrkvička, PhD.");
                contentStream.endText();

            }

            document.save(pdfPath.toFile());
        }
    }

    private InputStream requireResource(String path) throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(path);

        if (inputStream == null) {
            throw new IOException("Resource not found: " + path);
        }

        return inputStream;
    }

    public Path renderTeacherListPreview() throws IOException
    {
        Files.deleteIfExists(pngPath);

        try(PDDocument document = Loader.loadPDF(pdfPath.toFile()))
        {
            PDFRenderer renderer = new PDFRenderer(document);

            BufferedImage previewImage = renderer.renderImageWithDPI(0, 600);

            ImageIO.write(previewImage, "PNG", pngPath.toFile());
        }

        return pngPath;
    }
}
