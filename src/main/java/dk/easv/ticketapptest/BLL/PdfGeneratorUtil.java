package dk.easv.ticketapptest.BLL;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

public class PdfGeneratorUtil {

    public static void generateTicket(String filePath, String ticketInfo) {
        try {
            PdfWriter writer = new PdfWriter(filePath);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            document.add(new Paragraph("🎟 Ticket Confirmation 🎟"));
            document.add(new Paragraph(ticketInfo));

            document.close();
            System.out.println("PDF created: " + filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
