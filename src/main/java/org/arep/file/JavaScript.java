package org.arep.file;

import org.arep.Rest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class JavaScript implements Rest {
    @Override
    public String getHeader() {
        return "HTTP/1.1 200 OK\r\n" +
                "Content-type: application/javascript\r\n" +
                "\r\n";
    }

    @Override
    public String getBody() {

        try {
            byte[] fileContent = Files.readAllBytes(Paths.get("src/main/resources/public/index.js"));
            return new String(fileContent);
        } catch (IOException e) {
            System.out.println("File not found: " + "src/main/resources/public/index.jsl");
            return "HTTP/1.1 404 File not found.";
        }

    }
}
