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

            respuesta.type("application/json");
            String jsonResponse = "{\"message\": \"Stefania\"}";
            return  jsonResponse;
        });
        server.post("/json-post", (req, respuesta) -> {
            respuesta.type("application/json");
            String jsonResponse = "{\"message\": \"Stefania\"}";
            return jsonResponse;
        });
        server.post("/submit-json", (req, respuesta) -> {

            respuesta.type("application/json");
            String jsonResponse = "{\"message\": \"Stefania\"}";
            return jsonResponse;});


        server.run(args);
    }
}
