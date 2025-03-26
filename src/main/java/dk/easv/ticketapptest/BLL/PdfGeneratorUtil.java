package dk.easv.ticketapptest.BLL;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import java.io.File;
import com.itextpdf.io.image.ImageDataFactory;

public class PdfGeneratorUtil {

    public static void generateTicket(String filePath, String ticketInfo, String qrCodePath) {
        try {
            File file = new File(filePath);
            file.getParentFile().mkdirs();
            PdfWriter writer = new PdfWriter(file);
            PdfDocument pdf = new PdfDocument(writer);

            Document document = new Document(pdf);
            document.add(new Paragraph(ticketInfo));

            File qrFile = new File(qrCodePath);
            if (qrFile.exists()) {
                Image qrImage = new Image(ImageDataFactory.create(qrFile.getAbsolutePath()));
                qrImage.setWidth(200);
                qrImage.setHeight(200);
                document.add(qrImage);
            }
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
