package org.arep;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.nio.file.Files;

/**
 * server gets the movie search services
 */

public class HttpServer {
    private static HttpServer _instance = new HttpServer();
    private Map<String, Rest> services = new HashMap<>();

    private HttpServer (){}
    public static HttpServer getInstance() {
        return _instance;
    }
    private static final String USER_AGENT = "Mozilla/5.0";
    private static final String URL = "https://omdbapi.com/?t=%S&apikey=1d53bda9";
    public static final ConcurrentHashMap<String, String> cache = new ConcurrentHashMap<>();

    /**
     * main is the method stars the server and requests search
     * @param args
     * @throws IOException
     */
    public void run(String[] args) throws IOException {
        //para escuchar en el puerto
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(35000);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 35000.");
            System.exit(1);
        }
        boolean running = true;

        while (running) {
            Socket clientSocket = null;
            try {
                System.out.println("Listo para recibir ...");
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                System.err.println("Accept failed.");
                System.exit(1);
            }

            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    clientSocket.getInputStream()));
            String inputLine, outputLine;

            boolean firstLine = true;
            String request = "/form";
            String method = "GET";
            while ((inputLine = in.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    request = inputLine.split(" ")[1];
                    method = inputLine.split(" ")[0];

                }
                System.out.println("Received: " + inputLine);
                if (!in.ready()) {
                    break;
                }
            }

            if (request.startsWith("/form?") && method.equals("POST")) {
                String requestedMovie = request.replace("/form?name=", "");
                outputLine = "HTTP/1.1 200 OK\r\n" +
                        "Content-type: application/json\r\n"+
                        "\r\n"
                        + getHello(requestedMovie.toLowerCase());

            }else if(request.startsWith("/apps/")){
                outputLine= getStaticFile(request.substring(5));


            } else if (request.equalsIgnoreCase("/")){
                outputLine = getStaticFile("/form");
            }else{
                outputLine = getStaticFile("/404");
            }
            out.println(outputLine);
            out.close();
            in.close();
            clientSocket.close();
        }
        serverSocket.close();
    }
    private String getStaticFile(String Name)  {
        Rest rest = services.get(Name);
        String header = rest.getHeader();
        String body = rest.getBody();
        return header + body;
    }
    public void addService(String key, Rest service) {
        services.put(key, service);
    }

    public static String getContentType(String fileName) {
        if (fileName.endsWith(".html")) {
            return "text/html";
        } else if (fileName.endsWith(".css")) {
            return "text/css";
        } else if (fileName.endsWith(".js")) {
            return "text/javascript";
        } else {

            return "text/plain";
        }
    }


    /**
     * method gethellos that gets the movie by the user, from API or cache
     * @param movie
     * @return
     * @throws IOException
     */
    public static String getHello(String movie) throws IOException {
        String Movie = "";
        if(movie !=null) {
            if (cache.containsKey(movie)) {
                Movie = cache.get(movie);
                return Movie;
            }
            String formato = String.format(URL, movie);
            URL obj = new URL(formato);
            HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", USER_AGENT);

            int Code = connection.getResponseCode();
            System.out.println("GET Response Code :: " + Code);

            if (Code == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        connection.getInputStream()));
                String inputLine;
                StringBuffer responsed = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    responsed.append(inputLine);
                }
                in.close();
                Movie = responsed.toString();
                cache.put(movie, Movie);
            } else {
                System.out.println("GET request not worked");
            }
            System.out.println("GET DONE");
        }

            return Movie;

    }

    /**
     *method that returns the page to the user in the web
     * @return
     */
    public static String getDefaultIndex() throws IOException {
        // Cambia "index.html" al nombre de tu archivo HTML predeterminado
        String htmlContenido = getHello("index.html");
        String response = "HTTP/1.1 200 OK\r\n" +
                "Content-type: text/html\r\n" +
                "\r\n" + htmlContenido;
        return response;
    }
}

