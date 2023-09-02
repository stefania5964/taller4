package org.arep.file;

import org.arep.Rest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class JavaScript implements Rest {
    @Override
    public byte[] getHeader() {
        return ("HTTP/1.1 200 OK\r\n" +
                "Content-type: application/javascript\r\n" +
                "\r\n").getBytes();
    }

    @Override
    public byte[] getBody() {

        try {
            byte[] fileContent = Files.readAllBytes(Paths.get("target/classes/public/index.js"));
            return fileContent;
        } catch (IOException e) {
            System.out.println("File not found: " + "target/classes/public/index.jsl");
            return ("HTTP/1.1 404 File not found.").getBytes();
        }

    }
}
