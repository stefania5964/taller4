package org.arep;

import java.util.Base64;

public class Handler {
    private String status;
    private String type;
    private String body;
    public Handler() {
        status = "HTTP/1.1 200 OK";
        type = "Content-type: text/html";
    }

    public Handler(String body) {
        status = "HTTP/1.1 200 OK";
        type = "Content-type: text/html";
        this.body = body;
    }
    public void body(String body) {
        this.body = body;
    }
    public void type(String type) {
        this.type = "Content-type: " + type;
    }
    public String getResponse() {
        return status + "\r\n" + type + "\r\n" + "\r\n" + body;
    }
    /**
     * Sets the type of the Http message according to the file extension
     * @param file
     */
    public void setSpecificType(String file) {
        String extension = file.split("\\.")[1];
        if (extension.equalsIgnoreCase("html")) {
            type = "Content-type: text/html";
        } else if (extension.equalsIgnoreCase("css")) {
            type = "Content-type: text/css";
        } else if (extension.equalsIgnoreCase("js")) {
            type = "Content-type: application/javascript";
        }else if (extension.equalsIgnoreCase("png")) {
            type = "Content-type: image/png";
            body = Base64.getEncoder().encodeToString(HttpServer.getInstance().staticFiles.getImagen(file));
        }
    }

}
