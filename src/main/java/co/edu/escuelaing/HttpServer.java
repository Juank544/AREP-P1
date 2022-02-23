package co.edu.escuelaing;

import co.edu.escuelaing.service.Clima;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer {
    private static Clima clima;
    private static String lugar = "London";

    public void start() throws IOException, InvocationTargetException, IllegalAccessException {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(getPort());

        } catch (IOException e) {
            System.err.println("Could not listen on port: 35000.");
            System.exit(1);
        }

        boolean running = true;
        while (running){
            Socket clientSocket = null;
            try {
                System.out.println("Listo para recibir ...");
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                System.err.println("Accept failed.");
                System.exit(1);
            }

            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String inputLine, outputLine;
            boolean primeraLinea = true;
            String file = "";

            while ((inputLine = in.readLine()) != null) {
                System.out.println("Received: " + inputLine);
                if (primeraLinea){
                    file = inputLine.split(" ")[1];
                    System.out.println("File: " + file);
                    primeraLinea = false;
                }
                if (!in.ready()) {
                    break;
                }
            }

            if (file.startsWith("/clima")){
                outputLine = "HTTP/1.1 200 OK\r\n"
                        +"Content-Type: text/html\r\n"
                        +"\r\n"
                        +"<!DOCTYPE html>"
                        + "<html>"
                        + "<head>"
                        + "<meta charset=\"UTF-8\">"
                        + "<title>Clima</title>\n"
                        + "</head>"
                        + "<body>"
                        + "<h1>Bienvenido al servicio de clima</h1>"
                        + "<h3>Ingrese la ciudad que le gustaría consultar</h3>"
                        + "<form action=\"/consulta\">\n" +
                        "  <label for=\"lugar\">First name:</label><br>\n" +
                        "  <input type=\"text\" id=\"lugar\" name=\"lugar\" value=\"London\"><br>\n" +
                        "  <input type=\"submit\" value=\"Consultar\">\n" +
                        "</form>"
                        + "<br>"
                        + "</body>"
                        + "</html>";
            } else if (file.startsWith("/consulta?lugar=")){
                lugar = file.substring((16));
                String conexion = Clima.getClima(lugar);
                //System.out.println(lugar+"-------------------------------------------------");
                outputLine = "HTTP/1.1 200 OK\r\n"
                        +"Content-Type: text/html\r\n"
                        +"\r\n"
                        +"<!DOCTYPE html>"
                        + "<html>"
                        + "<head>"
                        + "<meta charset=\"UTF-8\">"
                        + "<title>Clima del lugar dado</title>\n"
                        + "</head>"
                        + "<body>"
                        + "<h1>El clima en el lugar dado es:</h1>"
                        + conexion
                        + "<br>"
                        + "<a href='/clima'>Página principal</a>"
                        + "</body>"
                        + "</html>";
            } else {
                outputLine = "HTTP/1.1 200 OK\r\n"
                        +"Content-Type: text/html\r\n"
                        +"\r\n"
                        +"<!DOCTYPE html>"
                        + "<html>"
                        + "<head>"
                        + "<meta charset=\"UTF-8\">"
                        + "<title>Error</title>\n"
                        + "</head>"
                        + "<body>"
                        + "<h1>Index del servicio de clima</h1>"
                        + "<h3>Use el siguiente botón para ir al dashboard</h3>"
                        + "<br>"
                        + "<a href='/clima'>Página principal</a>"
                        + "</body>"
                        + "</html>";
            }
            out.println(outputLine);

            out.close();
            in.close();
            clientSocket.close();
        }
        serverSocket.close();
    }

    static int getPort() {
        if (System.getenv("PORT") != null) {
            return Integer.parseInt(System.getenv("PORT"));
        }
        return 35000; //returns default port if heroku-port isn't set(i.e. on localhost)
    }
}
