package dk.easv.ticketapptest.BLL.util;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.properties.VerticalAlignment;
import java.io.File;
import java.io.IOException;

public class PdfGeneratorUtil {

    public static void generateTicket(String filePath, String eventTitle, String eventDescription, String locationGuidance, String eventLocation, String eventDate, String eventStartTime, String eventEndTime, String ticketType, String customerName, String qrCodePath, String barCodePath, String logoPath) throws IOException {
        try {
            File file = new File(filePath);
            file.getParentFile().mkdirs();
            PdfWriter writer = new PdfWriter(file);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf, PageSize.A4);

            PdfFont bold = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
            PdfFont normal = PdfFontFactory.createFont(StandardFonts.HELVETICA);

            // Logo and Event Details
            Table topSectionTable = new Table(UnitValue.createPercentArray(1)).useAllAvailableWidth();
            Table guidanceSectionTable = new Table(UnitValue.createPercentArray(1)).useAllAvailableWidth();
            topSectionTable.setMarginTop(10);
            guidanceSectionTable.setMarginTop(15);

            File logoFile = new File(logoPath);
            if (logoFile.exists()) {
                ImageData logoImageData = ImageDataFactory.create(logoFile.getAbsolutePath());
                Image logoImage = new Image(logoImageData).setWidth(400).setHeight(100);
                topSectionTable.addCell(new Cell().add(logoImage).setBorder(Border.NO_BORDER).setVerticalAlignment(VerticalAlignment.MIDDLE));
            }

            topSectionTable.addCell(new Cell().add(new Paragraph(eventTitle).setFont(bold).setFontSize(20).setFontColor(ColorConstants.BLACK)).setBorder(Border.NO_BORDER));
            topSectionTable.addCell(new Cell().add(new Paragraph(eventDescription).setFont(normal).setFontSize(12)).setBorder(Border.NO_BORDER));
            guidanceSectionTable.addCell(new Cell().add(
                    new Paragraph()
                            .add(new Text("Guidance: ").setFont(bold))
                            .add(new Text(locationGuidance).setFont(normal))
            ).setFontSize(12).setBorder(Border.NO_BORDER));

            document.add(topSectionTable);
            document.add(guidanceSectionTable);



            // Add bottom title above event details
            Paragraph bottomTitle = new Paragraph(eventTitle).setFont(bold).setFontSize(16).setTextAlignment(TextAlignment.LEFT);
            document.add(bottomTitle.setFixedPosition(36, 240, PageSize.A4.getWidth() - 72));



            // Draw dotted line above bottom title
            drawDottedLineAndScissor(pdf, 270);

            // Bottom Section
            Table bottomTable = new Table(UnitValue.createPercentArray(new float[]{1, 1})).useAllAvailableWidth();
            bottomTable.setFixedPosition(36, 50, PageSize.A4.getWidth() - 72);

            Table eventDetailsTable = new Table(UnitValue.createPercentArray(1)).useAllAvailableWidth();
            eventDetailsTable.addCell(createCell("Location: " + eventLocation, normal));
            eventDetailsTable.addCell(createCell("Date: " + eventDate, normal));
            eventDetailsTable.addCell(createCell("Time: " + eventStartTime + " - " + eventEndTime, normal));
            eventDetailsTable.addCell(createCell("Ticket Type: " + ticketType, normal));
            eventDetailsTable.addCell(createCell("Customer: " + customerName, normal));
            bottomTable.addCell(new Cell().add(eventDetailsTable).setBorder(Border.NO_BORDER));


            Table qrBarcodeTable = new Table(UnitValue.createPercentArray(1)).useAllAvailableWidth();
            if (new File(qrCodePath).exists()) {
                Image qrImage = new Image(ImageDataFactory.create(qrCodePath)).setWidth(150).setHeight(150);
                qrBarcodeTable.addCell(new Cell().add(qrImage).setBorder(Border.NO_BORDER));
            }
            if (new File(barCodePath).exists()) {
                Image barImage = new Image(ImageDataFactory.create(barCodePath)).setWidth(300).setHeight(100);
                qrBarcodeTable.addCell(new Cell().add(barImage).setBorder(Border.NO_BORDER));
            }
            Table qrCodeTable = new Table(UnitValue.createPercentArray(1)).useAllAvailableWidth();
            qrCodeTable.addCell(new Cell().add(qrBarcodeTable).setBorder(Border.NO_BORDER));
            qrCodeTable.setFixedPosition(200,10,PageSize.A4.getWidth() - 72);

            document.add(bottomTable);
            document.add(qrCodeTable);
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Cell createCell(String text, PdfFont font) {
        return new Cell().add(new Paragraph(text).setFont(font).setFontSize(10)).setBorder(Border.NO_BORDER);
    }

    private static void drawDottedLineAndScissor(PdfDocument pdf, float y) {
        PdfCanvas canvas = new PdfCanvas(pdf.getFirstPage());
        canvas.setLineDash(3, 3);
        canvas.moveTo(36, y).lineTo(PageSize.A4.getWidth() - 36, y).stroke();

        float scissorSize = 20;
        float scissorX = 36 + 10;
        float scissorY = y - scissorSize / 2;
        canvas.moveTo(scissorX, scissorY).lineTo(scissorX + scissorSize, scissorY + scissorSize);
        canvas.moveTo(scissorX, scissorY + scissorSize).lineTo(scissorX + scissorSize, scissorY);
        canvas.stroke();
    }
}
