package co.edu.escuelaing;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        try {
            HttpServer httpServer = new HttpServer();
            httpServer.start();
        } catch (IOException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        System.out.println( "Hello World!" );
    }
}
