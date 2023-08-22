package org.arep;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.*;
import java.io.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * server gets the movie search services
 */
public class HttpServer {

    private static final String USER_AGENT = "Mozilla/5.0";
    private static final String URL_STRING = "http://www.omdbapi.com/";
    private static final String KEY_STRING = "f8ed47c";
    public static final ConcurrentHashMap<String, String> cache = new ConcurrentHashMap<>();

    /**
     * main is the method stars the server and requests search
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
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
            String requestedMovie = null;
            if (request.startsWith("/form?") && method.equals("POST")) {
                requestedMovie = request.replace("/form?name=", "");
                outputLine = "HTTP/1.1 200 OK\r\n" +
                        "Content-type: application/json\r\n"+
                        "\r\n"
                        + getHello(requestedMovie.toLowerCase());
            } else {
                outputLine = getDefaultIndex();
            }
            out.println(outputLine);
            out.close();
            in.close();
            clientSocket.close();
        }
        serverSocket.close();
    }

    /**
     * method gethellos that gets the movie by the user, from API or cache
     * @param movie
     * @return
     * @throws IOException
     */
    public static String getHello(String movie) throws IOException {
        String Movie = "";
        if (cache.containsKey(movie)) {
            Movie = cache.get(movie);
            return Movie;
        }
        String formato = String.format(URL_STRING, movie,KEY_STRING);
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
        return Movie;
    }

    /**
     *method that returns the page to the user in the web
     * @return
     */
    public static String getDefaultIndex() {
        return "HTTP/1.1 200 OK\r\n" +
                "Content-type: text/html\r\n" +
                "\r\n" +
                "<!DOCTYPE html>\n" +
                "<html>\n" +
                "  <head>\n" +
                "    <title>Búsqueda</title>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <style>\n" +
                "\n" +
                "      * {\n" +
                "        font-family: \" Georgia\", serif;\n" +
                "        background-color: #b8bac2;\n" +
                "      }\n" +
                "\n" +
                "      h1 {\n" +
                "        padding: 10px 50px 20px;\n" +
                "        margin: 15px 0px;\n" +
                "      }\n" +
                "\n" +
                "      .form {\n" +
                "        padding: 51px;\n" +
                "      }\n" +
                "\n" +
                "      .form label {\n" +
                "        margin: 15px 2px;\n" +
                "      }\n" +
                "\n" +
                "      .form input {\n" +
                "        margin: 5px 2px;\n" +
                "        padding: 8px;\n" +
                "        font-size: 20px;\n" +
                "        border-radius: 3px;\n" +
                "        border: 1px solid rgba(0, 0, 0, 0);\n" +
                "        box-shadow: 0 6px 10px 0 rgba(0, 0, 0 , .15);\n" +
                "        transition: all 200ms ease;\n" +
                "      }\n" +
                "\n" +
                "      .form input:hover {\n" +
                "        border: 1px solid rgba(0, 0, 0, 0.281);\n" +
                "        box-shadow: 0 6px 10px 0 rgba(0, 0, 0 , .22);\n" +
                "      }\n" +
                "\n" +
                "      .form input:focus {\n" +
                "        outline: none !important;\n" +
                "        border: 1px solid #5badc9;\n" +
                "      }\n" +
                "\n" +
                "      .btn {\n" +
                "        color: white;\n" +
                "        background-color: #428f81;\n" +
                "        transition: all 200ms ease;\n" +
                "        cursor: pointer;\n" +
                "      }\n" +
                "\n" +
                "      .btn:hover {\n" +
                "        background-color: #5badc9;\n" +
                "      }\n" +
                "\n" +
                "      .container {\n" +
                "        margin: 20px 5px;\n" +
                "        padding: 8px;\n" +
                "      }\n" +
                "\n" +
                "    </style>\n" +
                "  </head>\n" +
                "\n" +
                "  <body>\n" +
                "    <h1>Buscar Película</h1>\n" +
                "    <form class=\"form\" action=\"/form\">\n" +
                "      <label for=\"postname\">Nombre:</label><br>\n" +
                "      <input type=\"text\" id=\"postname\" name=\"name\" value=\"\" placeholder=\"Ingresa el nombre\" required><br>\n" +
                "      <input class=\"btn\" type=\"button\" value=\"Search\" onclick=\"loadPostMsg(postname)\">\n" +
                "    </form>\n" +
                "      <div class=\"container\" id=\"postrespmsg\"></div>\n" +
                "      <script>\n" +
                "        function displayJson(json, div) {\n" +
                "            for (const key of Object.keys(json)) {\n" +
                "                if (key == \"Ratings\") {\n" +
                "                    div.innerHTML += \"Ratings: \"\n" +
                "                    for (const ratingKey of Object.keys(json[key])) {\n" +
                "                        div.innerHTML += json[key][ratingKey][\"Source\"] + \": \" + json[key][ratingKey][\"Value\"] + \", \";\n" +
                "                    }\n" +
                "                    div.innerHTML += \"<br/>\"\n" +
                "                } else {\n" +
                "                    div.innerHTML += key + \": \" + json[key] + \"<br/>\";\n" +
                "                }\n" +
                "            }\n" +
                "        }\n" +
                "\n" +
                "        function loadPostMsg(name){\n" +
                "            let movie;\n" +
                "            let url = \"/form?name=\" + name.value;\n" +
                "            fetch (url, {method: 'POST'})\n" +
                "                .then(response => response.json())\n" +
                "                .then(y => {\n" +
                "                    let msg = document.getElementById(\"postrespmsg\");\n" +
                "                    msg.innerHTML = \"\";\n" +
                "                    console.log(y);\n" +
                "                    displayJson(y, msg);\n" +
                "                } /*document.getElementById(\"postrespmsg\").innerHTML = y*/);\n" +
                "          }\n" +
                "      </script>\n" +
                "  </body>\n" +
                "</html>";
    }
}

