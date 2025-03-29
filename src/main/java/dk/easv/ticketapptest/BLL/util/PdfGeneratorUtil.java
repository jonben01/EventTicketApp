package dk.easv.ticketapptest.BLL.util;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import java.io.File;
import java.io.IOException;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.properties.VerticalAlignment;

public class PdfGeneratorUtil {

    public static void generateTicket(String filePath, String eventTitle, String eventDescription, String locationGuidance, String eventLocation, String eventDate, String eventStartTime, String eventEndTime, String ticketType, String customerName, String qrCodePath, String barCodePath, String logoPath)  throws IOException {
        try {
            File file = new File(filePath);
            file.getParentFile().mkdirs();
            PdfWriter writer = new PdfWriter(file);
            PdfDocument pdf = new PdfDocument(writer);
            pdf.addNewPage();
            Document document = new Document(pdf, PageSize.A4);

            //Todo use the schools fonts instead of HELVETICA

            // Fonts
            PdfFont bold = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
            PdfFont normal = PdfFontFactory.createFont(StandardFonts.HELVETICA);

            // --- Top Side: Logo and Event Details
            Table topSectionTable = new Table(UnitValue.createPercentArray(1)).useAllAvailableWidth();
            topSectionTable.setWidth(UnitValue.createPercentValue(100));
            topSectionTable.setMarginTop(10);

            // Logo
            File logoFile = new File(logoPath);
            if (logoFile.exists()) {
                ImageData logoImageData = ImageDataFactory.create(logoFile.getAbsolutePath());
                Image logoImage = new Image(logoImageData);
                logoImage.setWidth(400); // Adjust logo size as needed
                logoImage.setHeight(100);
                Cell logoCell = new Cell().add(logoImage).setBorder(Border.NO_BORDER).setVerticalAlignment(VerticalAlignment.MIDDLE);
                topSectionTable.addCell(logoCell);
            }

            // Title
            Paragraph titleParagraph = new Paragraph(eventTitle)
                    .setFont(bold)
                    .setFontSize(20)
                    .setFontColor(ColorConstants.BLACK)
                    .setTextAlignment(TextAlignment.LEFT);
            topSectionTable.addCell(new Cell().add(titleParagraph).setBorder(Border.NO_BORDER));

            // Description
            Paragraph descriptionParagraph = new Paragraph(eventDescription)
                    .setFont(normal)
                    .setFontSize(12)
                    .setFontColor(ColorConstants.BLACK)
                    .setTextAlignment(TextAlignment.LEFT);
            topSectionTable.addCell(new Cell().add(descriptionParagraph).setBorder(Border.NO_BORDER));

            // Location Guidance
            Paragraph locationGuidanceParagraph = new Paragraph("Guidance: " + locationGuidance)
                    .setFont(normal)
                    .setFontSize(12)
                    .setFontColor(ColorConstants.BLACK)
                    .setTextAlignment(TextAlignment.LEFT);
            topSectionTable.addCell(new Cell().add(locationGuidanceParagraph).setBorder(Border.NO_BORDER));

            document.add(topSectionTable);

            // --- Ticket Area ---
            // Calculate the Y position for the bottom of the ticket area
            float ticketBottomY = 175; // 150 points from the bottom of the page

            // Calculate the Y position for the dotted line (above the ticket)
            float lineY = ticketBottomY + 150; // 150 points above the bottom of the ticket

            // Draw dotted line and scissor
            drawDottedLineAndScissor(pdf, lineY);

            // --- Bottom Side: QR Code & Barcode ---
            Table bottomTable = new Table(UnitValue.createPercentArray(new float[]{1, 1})).useAllAvailableWidth(); // 2 columns: event details on the left, nested table on the right
            bottomTable.setMarginTop(lineY - 130); // Position the table below the line
            bottomTable.setMarginLeft(20);

            // Event Details (Location, Date, Time)
            Table eventDetailsTable = new Table(UnitValue.createPercentArray(1)).useAllAvailableWidth();
            eventDetailsTable.setMarginTop(5);

            // Event Title
            Paragraph eventTitleParagraph = new Paragraph(eventTitle)
                    .setFont(bold)
                    .setFontSize(16)
                    .setFontColor(ColorConstants.BLACK)
                    .setTextAlignment(TextAlignment.LEFT);
            eventDetailsTable.addCell(new Cell().add(eventTitleParagraph).setBorder(Border.NO_BORDER));

            eventDetailsTable.addCell(createCell("Location: " + eventLocation, normal, TextAlignment.LEFT));
            eventDetailsTable.addCell(createCell("Date: " + eventDate, normal, TextAlignment.LEFT));
            eventDetailsTable.addCell(createCell("Time: " + eventStartTime + " - " + eventEndTime, normal, TextAlignment.LEFT));
            eventDetailsTable.addCell(createCell("Ticket Type: " + ticketType, normal, TextAlignment.LEFT));
            eventDetailsTable.addCell(createCell("Customer: " + customerName, normal, TextAlignment.LEFT));

            bottomTable.addCell(new Cell(1,1).add(eventDetailsTable).setBorder(Border.NO_BORDER).setVerticalAlignment(VerticalAlignment.MIDDLE));

            // Create a nested table for the QR code and barcode
            Table nestedTable = new Table(UnitValue.createPercentArray(1)).useAllAvailableWidth();
            nestedTable.setBorder(Border.NO_BORDER);

            // QR Code
            File qrFile = new File(qrCodePath);
            if (qrFile.exists()) {
                ImageData qrImageData = ImageDataFactory.create(qrFile.getAbsolutePath());
                Image qrImage = new Image(qrImageData);
                qrImage.setWidth(100);
                qrImage.setHeight(100);
                // Create a new cell for the QR code
                Cell qrCell = new Cell().setBorder(Border.NO_BORDER).setVerticalAlignment(VerticalAlignment.TOP).setHorizontalAlignment(HorizontalAlignment.RIGHT);
                // Create a new table to hold the QR code and allow for vertical adjustment
                Table qrTable = new Table(UnitValue.createPercentArray(1)).useAllAvailableWidth();
                qrTable.addCell(new Cell().add(qrImage).setBorder(Border.NO_BORDER));
                // --- QR Code Margins ---
                qrTable.setMarginLeft(50); // Remove the left margin
                qrTable.setMarginRight(0); // Move QR code to the right (increase value to move further right)
                qrTable.setMarginTop(0); // Move QR code down (increase value to move further down)
                qrTable.setMarginBottom(0); // Move QR code up (increase value to move further up)
                qrCell.add(qrTable);
                nestedTable.addCell(new Cell(1,1).add(qrCell).setBorder(Border.NO_BORDER));
            }

            // Barcode
            File barFile = new File(barCodePath);
            if (barFile.exists()) {
                ImageData barImageData = ImageDataFactory.create(barFile.getAbsolutePath());
                Image barImage = new Image(barImageData);
                barImage.setWidth(300); // Change the width to 200
                barImage.setHeight(100); // Change the height to 100
                // Create a new cell for the barcode
                Cell barCell = new Cell().setBorder(Border.NO_BORDER).setVerticalAlignment(VerticalAlignment.TOP).setHorizontalAlignment(HorizontalAlignment.RIGHT);
                // Create a new table to hold the barcode and allow for vertical adjustment
                Table barTable = new Table(UnitValue.createPercentArray(1)).useAllAvailableWidth();
                barTable.addCell(new Cell().add(barImage).setBorder(Border.NO_BORDER));
                // --- Barcode Margins ---
                barTable.setMarginLeft(0); // Move barcode to the right (increase value to move further right)
                barTable.setMarginRight(0); // Move barcode to the left (increase value to move further left)
                barTable.setMarginTop(10); // Move barcode down (increase value to move further down)
                barTable.setMarginBottom(0); // Move barcode up (increase value to move further up)
                barCell.add(barTable);
                nestedTable.addCell(new Cell(1,1).add(barCell).setBorder(Border.NO_BORDER));
            }

            bottomTable.addCell(new Cell(2,1).add(nestedTable).setBorder(Border.NO_BORDER));

            document.add(bottomTable);

            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Cell createCell(String text, PdfFont font, TextAlignment alignment) {
        Paragraph paragraph = new Paragraph(text).setFont(font).setFontSize(10).setTextAlignment(alignment);
        Cell cell = new Cell().add(paragraph).setBorder(Border.NO_BORDER);
        return cell;
    }

    private static void drawDottedLineAndScissor(PdfDocument pdf, float y) {
        PdfCanvas canvas = new PdfCanvas(pdf.getFirstPage()); // Use getFirstPage() instead of getLastPage()
        canvas.setLineDash(3, 3); // Dotted line pattern
        canvas.moveTo(36, y).lineTo(PageSize.A4.getWidth() - 36, y).stroke();

        // Draw scissor (simplified representation)
        float scissorSize = 20;
        float scissorX = 36 + 10;
        float scissorY = y - scissorSize / 2;

        canvas.setLineWidth(1);
        canvas.moveTo(scissorX, scissorY);
        canvas.lineTo(scissorX + scissorSize, scissorY + scissorSize);
        canvas.moveTo(scissorX, scissorY + scissorSize);
        canvas.lineTo(scissorX + scissorSize, scissorY);
        canvas.stroke();
    }
}
