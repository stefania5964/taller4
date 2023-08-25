package org.arep.file;

import org.arep.Rest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Base64;

public class Img implements Rest {
    @Override
    public String getHeader() {
        return "HTTP/1.1 200 OK\r\n" +
                "Content-type: image/png\r\n" +
                "\r\n";
    }

    @Override
    public String getBody() {
        try {
            byte[] fileContent = Files.readAllBytes(Paths.get("src/main/resources/public/img/stich.png"));
            return new String(fileContent);
        } catch (IOException e) {
            System.out.println("File not found: " + "src/main/resources/public/img/stich.png");
            return "HTTP/1.1 404 Not Found\r\n\r\nFile not found.";
        }

    }
}

