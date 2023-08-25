package org.arep.file;
import org.arep.HttpServer;

import java.io.IOException;
public class App {
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.getInstance();
        server.addService("/form", new Html());
        server.addService("/index.css", new Css());
        server.addService("/index.js", new JavaScript());
        server.addService("/404", new Error404());
        server.addService("/image", new Img());
        server.run(args);
    }
}
