package org.arep.file;
import org.arep.HttpServer;

import java.io.IOException;
public class App {
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.getInstance();
        server.staticFiles.location("/public");
        // Configura un servicio GET usando una función lambda

        server.get("/hello", (req, respuesta) -> {
            String response = "Hello, this is a program";
            return response;});

        server.get("/get-json", (req, respuesta) -> {
            String jsonResponse = "{\"message\": \"Stefania\"}";
            String response = "HTTP/1.1 200 OK\r\n" +
                    "Content-Type: application/json\r\n" +
                    "\r\n" +
                    jsonResponse;
            return  response;
        });

        

        server.run(args);
    }
}
