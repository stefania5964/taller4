package org.arep.file;
import org.arep.Rest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Error404 implements Rest{
    @Override
    public byte[] getHeader(){
        return ("HTTP/1.1 200 OK\r\n" +
                "Content-type: text/html\r\n" +
                "\r\n").getBytes();
    }
    @Override
    public byte[] getBody(){
        try {
            byte[] fileContent = Files.readAllBytes(Paths.get("target/classes/public/404.html"));
            return fileContent;
        } catch (IOException e) {
            System.out.println("File not found: " + "target/classes/public/404.html");
            return ("HTTP/1.1 404 Not Found\r\n\r\nFile not found.").getBytes();
        }

    }

}
