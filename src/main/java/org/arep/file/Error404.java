package org.arep.file;
import org.arep.Rest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Error404 implements Rest{
    @Override
    public String getHeader(){
        return "HTTP/1.1 200 OK\r\n" +
                "Content-type: text/html\r\n" +
                "\r\n";
    }
    @Override
    public String getBody(){
        try {
            byte[] fileContent = Files.readAllBytes(Paths.get("src/main/resources/public/404.html"));
            return new String(fileContent);
        } catch (IOException e) {
            System.out.println("File not found: " + "src/main/resources/public/404.html");
            return "HTTP/1.1 404 Not Found\r\n\r\nFile not found.";
        }

    }

}
