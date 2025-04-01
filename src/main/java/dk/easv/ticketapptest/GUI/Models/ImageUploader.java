package dk.easv.ticketapptest.GUI.Models;

import dk.easv.ticketapptest.BLL.Exceptions.EasvTicketException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class ImageUploader {


    public String uploadFile(String filePath) throws EasvTicketException {
        String fileDestDir = "userImages";
        File dir = new File(fileDestDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        //TODO implement unique names for pictures, to combat collision

        File file = new File(filePath);
        File destFile = new File(dir, file.getName());

        try {
            //TODO impement something that tells the user, if its going to replace an existing file, ideally shouldnt happen
            // files should have unique names i think

            Files.copy(file.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            return "userImages/" + file.getName();
        } catch (IOException e) {
            throw new EasvTicketException("Couldn't upload file", e);
        }
    }
}
