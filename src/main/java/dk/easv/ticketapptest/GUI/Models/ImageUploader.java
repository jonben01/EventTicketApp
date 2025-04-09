package dk.easv.ticketapptest.GUI.Models;
//project imports
import dk.easv.ticketapptest.BLL.Exceptions.EasvTicketException;
//java imports
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class ImageUploader {

    private static final int MAX_WIDTH = 400;
    //should have been changed to 400, but it is what it is
    private static final int MAX_HEIGHT = 600;
    private final String fileDestDir;

    public ImageUploader(String fileDestDir) {
        this.fileDestDir = fileDestDir;
    }


    //TODO should probably crop before resizing, not 100% sure. currently a very wide or very tall image will become tiny
    public String uploadFile(String filePath) throws EasvTicketException {
        File dir = new File(fileDestDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File file = new File(filePath);
        String baseName = removeExtension(file.getName());
        String forcedPng = baseName + ".png";
        File destFile = new File(dir, forcedPng);
        try {
            BufferedImage image = ImageIO.read(file);
            if (image == null) {
                throw new EasvTicketException("Image is null");
            }
            BufferedImage resized = resize(image);
            //resize the image if needed
            if (image.getWidth() > image.getHeight()) {
                 resized = cropIfWide(resized);
            } else if (image.getHeight() > image.getWidth()) {
                resized = cropIfTall(resized);
            }
            boolean success = ImageIO.write(resized, "png", destFile);
            if (!success) {
                throw new EasvTicketException("Failed to write image to file");
            }
            return "userImages/" + forcedPng;
        } catch (IOException e) {
            throw new EasvTicketException("Couldn't upload file", e);
        }
    }


    //one oversight for this method is that there is a chance the resulting dimensions are wonky (tried 180x180 once), it should ideally always be 400x400, or close to
    private BufferedImage resize(BufferedImage originalImage) {
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();
        if (height <= MAX_HEIGHT && width <= MAX_WIDTH) {
            //resize not needed
            return originalImage;
        }
        double widthRatio = (double) MAX_WIDTH / width;
        double heightRatio = (double) MAX_HEIGHT / height;
        double scaleFactor = Math.min(widthRatio, heightRatio);
        int newWidth = (int) (width * scaleFactor);
        int newHeight = (int) (height * scaleFactor);
        //argb keeps transparency of PNG files - using originalImage.getType is probably just as good
        BufferedImage newImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = newImage.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(originalImage, 0, 0, newWidth, newHeight, null);
        g.dispose();
        return newImage;
    }

    //this is me testing out new comments, not chatgpt comments - jonas
    private BufferedImage cropIfTall(BufferedImage resizedImage) {
        //1. get dimensions -- width = square dimensions for future use
        int width = resizedImage.getWidth();
        int height = resizedImage.getHeight();
        //2. check if a crop is needed
        if (height <= width) {
            return resizedImage;
        }
        //3. calculate different in height and width, and define a 25% offset based on it,
        int vertExtra = height - width;
        int offset = vertExtra / 4;
        //4. control bounds, if offset + width goes past the height, adjust it to fit square entirely in image
        if (offset + width > height) {
            offset = height - width;
        }
        //5. create a new bufferedImage holding the square crop of our image
        BufferedImage squareImage = new BufferedImage(width, width, BufferedImage.TYPE_INT_ARGB);
        //6. draw the new image: source region coords = X from 0 to width. Y from offset to offset+width - keeping it square
        //destination region = X from 0 to width. Y from 0 to width
        //essentially this takes a square chunk out of the image, offset by 25% towards the top
        //just in case I have to reuse this in the future and forget: THE Y COORDINATE GOES DOWNWARDS
        Graphics2D g = squareImage.createGraphics();
        g.drawImage(resizedImage, 0, 0, width, width,0, offset,width,offset + width, null);
        g.dispose();
        //7. return the cropped image
        return squareImage;
    }

    //TODO pretty much repeat the comments from above
    private BufferedImage cropIfWide(BufferedImage resizedImage) {
        int width = resizedImage.getWidth();
        int height = resizedImage.getHeight();
        if (width <= height) {
            return resizedImage;
        }
        int horExtra = width - height;
        //center
        int offset = horExtra / 2;
        if (offset + height > width) {
            offset = width - height;
        }
        BufferedImage squareImage = new BufferedImage(height, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = squareImage.createGraphics();
        g.drawImage(resizedImage, 0, 0, height, height, offset, 0, offset + height, height, null);
        g.dispose();
        return squareImage;
    }

    //removing extension since we force png either way
    private String removeExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex > 0) {
            return fileName.substring(0, dotIndex);
        }
        return fileName;
    }
}
