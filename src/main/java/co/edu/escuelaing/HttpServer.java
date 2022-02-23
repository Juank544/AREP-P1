package co.edu.escuelaing;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer {
    public void start() throws IOException, InvocationTargetException, IllegalAccessException {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(35000);
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

            String lugar = "London";
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
                        + "Bienvenido al servicio de clima"
                        + "</body>"
                        + "</html>";
            } else if (file.startsWith("/consulta?lugar=")){
                lugar = file.substring((16));
                System.out.println("_______________________________________________");
                System.out.println(lugar);
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
                        + ""
                        + "Lugar"
                        + "</body>"
                        + "</html>";
            } else {
                outputLine = null;
            }
            out.println(outputLine);

            out.close();
            in.close();
            clientSocket.close();
        }
        serverSocket.close();
    }
}
