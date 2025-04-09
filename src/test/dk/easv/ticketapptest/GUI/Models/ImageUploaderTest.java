package dk.easv.ticketapptest.GUI.Models;

import dk.easv.ticketapptest.BLL.Exceptions.EasvTicketException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class ImageUploaderTest {

    private ImageUploader imageUploader;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        String testDir = tempDir.resolve("images").toString();
        imageUploader = new ImageUploader(testDir);
    }

    @Test
    void testUploadAndResizeLandscape() throws Exception {
        int originalHeight = 900;
        int originalWidth = 3000;
        BufferedImage testImage = new BufferedImage(originalWidth, originalHeight, BufferedImage.TYPE_INT_ARGB);

        File testFile = tempDir.resolve("landscape.png").toFile();
        ImageIO.write(testImage, "png", testFile);

        String returnedPath = imageUploader.uploadFile(testFile.getAbsolutePath());
        assertTrue(returnedPath.endsWith("landscape.png"), "Returned path should end with 'landscape.png'");

        File resultFile = new File(tempDir.resolve("images").toString(), "landscape.png");
        assertTrue(resultFile.exists(), "Image should exist");

        BufferedImage resultImage = ImageIO.read(resultFile);
        assertNotNull(resultImage, "Image should not be null");
        assertTrue(resultImage.getWidth() <= 400 && resultImage.getHeight() <= 600,
                "\nactual width:" + resultImage.getWidth() + "\n actual height:" + resultImage.getHeight()
                    + "\n expected width:" + originalWidth + "\n expected height:" + originalHeight);
        assertEquals(resultImage.getHeight(), resultImage.getWidth(), "Image should be square");
    }

    @Test
    void testUploadAndResizePortrait() throws Exception {
        int originalHeight = 3000;
        int originalWidth = 900;
        BufferedImage testImage = new BufferedImage(originalWidth, originalHeight, BufferedImage.TYPE_INT_ARGB);

        File testFile = tempDir.resolve("portrait.png").toFile();
        ImageIO.write(testImage, "png", testFile);

        String returnedPath = imageUploader.uploadFile(testFile.getAbsolutePath());
        assertTrue(returnedPath.endsWith("portrait.png"), "Returned path should end with 'landscape.png'");

        File resultFile = new File(tempDir.resolve("images").toString(), "portrait.png");
        assertTrue(resultFile.exists(), "Image should exist");

        BufferedImage resultImage = ImageIO.read(resultFile);
        assertNotNull(resultImage, "Image should not be null");
        assertTrue(resultImage.getWidth() <= 400 && resultImage.getHeight() <= 600, resultImage.getWidth() + " " + resultImage.getHeight());
        assertEquals(resultImage.getHeight(), resultImage.getWidth(), "Image should be square");
    }


    @Test
    void testResizeImage() throws Exception {
        int originalHeight = 2500;
        int originalWidth = 2500;
        BufferedImage testImage = new BufferedImage(originalWidth, originalHeight, BufferedImage.TYPE_INT_ARGB);
        File testFile = tempDir.resolve("resize.png").toFile();
        ImageIO.write(testImage, "png", testFile);
        imageUploader.uploadFile(testFile.getAbsolutePath());
        File resultFile = new File(tempDir.resolve("images").toString(), "resize.png");
        assertTrue(resultFile.exists(), "Image should exist");
        BufferedImage resultImage = ImageIO.read(resultFile);
        assertNotNull(resultImage, "Image should not be null");
        assertTrue(resultImage.getWidth() <= 400 && resultImage.getHeight() <= 600, resultImage.getWidth() + " " + resultImage.getHeight());
        assertEquals(resultImage.getHeight(), resultImage.getWidth(), "Image should be square");
    }

    @Test
    void testFileWithoutExtension() throws Exception {
        int originalHeight = 600;
        int originalWidth = 800;
        BufferedImage testImage = new BufferedImage(originalWidth, originalHeight, BufferedImage.TYPE_INT_ARGB);
        File testFile = tempDir.resolve("fileWithoutExtension").toFile();
        ImageIO.write(testImage, "png", testFile);
        String returnedPath = imageUploader.uploadFile(testFile.getAbsolutePath());
        File resultFile = new File(tempDir.resolve("images").toString());
        assertTrue(resultFile.exists(), "Image should exist");
        assertTrue(returnedPath.endsWith(".png"));
    }

    @Test
    void testNonImageFile() throws Exception {
        File testFile = tempDir.resolve("text.txt").toFile();
        String textContext = "this is not an image lol";
        java.nio.file.Files.write(testFile.toPath(), textContext.getBytes());

        Exception exception = assertThrows(EasvTicketException.class, () -> {
            imageUploader.uploadFile(testFile.getAbsolutePath());
        });
        String message = exception.getMessage();
        assertTrue(message.contains("Image is null") ||
                        message.contains("Couldn't upload file") ||
                        message.contains("Failed to write image to file"),
                "Exception should indicate invalid image input");
    }

    @Test
    void testSmallCrop() throws Exception {
        int originalHeight = 300;
        int originalWidth = 400;
        BufferedImage testImage = new BufferedImage(originalWidth, originalHeight, BufferedImage.TYPE_INT_ARGB);

        File testFile = tempDir.resolve("smallCrop").toFile();
        ImageIO.write(testImage, "png", testFile);
        imageUploader.uploadFile(testFile.getAbsolutePath());
        File resultFile = new File(tempDir.resolve("images").toString(), "smallCrop.png");
        BufferedImage resultImage = ImageIO.read(resultFile);
        assertEquals(resultImage.getHeight(), resultImage.getWidth(), "Image should be square");
    }

    @Test
    void testSmallSquare() throws Exception {
        int originalHeight = 300;
        int originalWidth = 300;
        BufferedImage testImage = new BufferedImage(originalWidth, originalHeight, BufferedImage.TYPE_INT_ARGB);

        File testFile = tempDir.resolve("smallSquare").toFile();
        ImageIO.write(testImage, "png", testFile);
        imageUploader.uploadFile(testFile.getAbsolutePath());
        File resultFile = new File(tempDir.resolve("images").toString(), "smallSquare.png");
        BufferedImage resultImage = ImageIO.read(resultFile);
        assertTrue(resultImage.getHeight() == 300 && resultImage.getWidth() == 300, "Image should be the same");
    }

    @Test
    void testNonExistentFile() throws Exception {
        File fakeFile = tempDir.resolve("nonExistentFile.png").toFile();
        Exception exception = assertThrows(EasvTicketException.class, () -> {
            imageUploader.uploadFile(fakeFile.getAbsolutePath());
        });
        String message = exception.getMessage();
        assertTrue(message.contains("Image is null") ||
                        message.contains("Couldn't upload file") ||
                        message.contains("Failed to write image to file"),
                "Exception should indicate invalid image input");
    }


}