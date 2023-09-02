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
    private Map<String, Handler> getHandlers = new HashMap<>();
    private Map<String, Handler> postHandlers = new HashMap<>();
    public final StaticFil staticFiles = new StaticFil();


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
            String inputLine;
            byte[] outputLine;

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

            String requestedMovie;
            if (method.equalsIgnoreCase("GET")) {
                try {
                    if (request.equalsIgnoreCase("/")) {
                        outputLine = (staticFiles.getFile("/apps/form.html")).getBytes();
                    } else if (staticFiles.check(request)) {
                        System.out.println("ESTÁ EN STATIC");
                        outputLine = (staticFiles.getFile(request)).getBytes();
                    } else {
                        outputLine = (getHandlers.get(request).getResponse()).getBytes();
                    }
                }
                catch (NullPointerException e) {
                    outputLine = ("").getBytes();
                }
            } else /*if (method.equalsIgnoreCase("POST"))*/ {
                try {
                    if (request.startsWith("/form?")) {
                        requestedMovie = request.replace("/form?s=", "");
                        outputLine = ("HTTP/1.1 200 OK\r\n" +
                                "Content-type: application/json\r\n" +
                                "\r\n"
                                + getHello(requestedMovie.toLowerCase())).getBytes();
                    } else {
                        System.out.println("DEVOLVIENDO: " + postHandlers.get(request).getResponse());
                        outputLine = (postHandlers.get(request).getResponse()).getBytes();
                    }
                } catch (NullPointerException e) {
                    outputLine = ("").getBytes();
                }
            }

            out.println(outputLine);
            out.close();
            in.close();
            clientSocket.close();
        }
        serverSocket.close();
    }
    public interface Route {
        String handle(String req, Handler han);
    }



    public void addService(String key, Rest service) {
        services.put(key, service);
    }
    private byte[] getStaticFile(String Name)  {
        Rest rest = services.get(Name);
        byte[] header = rest.getHeader();
        byte[] body = rest.getBody();
        byte[] i = new byte[header.length + body.length ];
        for (int index = 0; index < header.length; index++){
            i[index]= header[index];
        }for(int index = header.length; index< i.length; index++){
            i[index]= body[index-header.length];
        }
        return i;
    }


    /**
     * Adds a GET request handler for a specified path.
     * @param path
     * @param route
     */
    public void get(String path, Route route) {
        Handler han= new Handler();
        han.body(route.handle("req", han));
        getHandlers.put(path, han);
    }

    /**
     * Adds a POST request handler for a specified path.
     * @param path
     * @param route
     */
    public void post(String path, Route route) {
        Handler han= new Handler();
        han.body(route.handle("req", han));
        postHandlers.put(path, han);
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


}

