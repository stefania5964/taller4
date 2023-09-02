package org.arep;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class StaticFil {
    private String location;
    public static final String ROOT = "src/main/resources";

    public StaticFil() {
        location = ROOT;
    }

    /**
     * Checks if a file is in the static files folder
     * @param file file
     * @return yes if the file exists, no otherwise
     */
    public boolean check(String file) {
        boolean res;
        try {
            Files.readAllBytes(Paths.get(location + file));
            res = true;
        } catch (IOException e) {
            res = false;
        }
        return res;
    }

    /**
     * Gets a file from the static files folder
     * @param file file
     * @return String corresponding to the Http response message
     */
    public String getFile(String file) {
        byte[] fileContent;
        try {
            fileContent = Files.readAllBytes(Paths.get(location + file));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String body = new String(fileContent);
        Handler han = new Handler(body);
        han.setSpecificType(file);
        return han.getResponse();
    }

    public byte[] getImagen(String file) {
        byte[] fileContent;
        try {
            fileContent = Files.readAllBytes(Paths.get(location + file));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return fileContent;
    }



    public void location(String location) {
        this.location = ROOT + location;
    }
}
