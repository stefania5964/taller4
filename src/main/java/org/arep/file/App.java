package org.arep.file;
import org.arep.Annotation.Component;
import org.arep.HttpServer;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class App {
    public static void main(String[] args) throws IOException, ClassNotFoundException, InvocationTargetException, IllegalAccessException {
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


        server.run(getComponents());
    }
    /**
     * Method that obtains all the Classes that has the Component annotation
     * @return List of classes names
     * @throws IOException
     * @throws ClassNotFoundException if class not found
     */
    public static List<String> getComponents() throws IOException, ClassNotFoundException {
        String path = "org/arep/file";
        List<String> componentClasses = new ArrayList<>();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Enumeration<URL> resources = classLoader.getResources(path);
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            File directory = new File(resource.getFile());
            File[] files = directory.listFiles();
            for (File file : files) {
                String fileName = file.getName();
                if (file.getName().endsWith(".class")) {
                    String className = fileName.substring(0, fileName.length() - 6);
                    Class<?> clase = Class.forName(path.replace("/", ".") + "." + className);
                    if (clase.isAnnotationPresent(Component.class)) {
                        componentClasses.add(clase.getName());
                    }
                }
            }
        }
        return componentClasses;
    }
}
