package co.edu.escuelaing.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Clima {

    public static String getClima(String lugar){
        StringBuffer stringBuffer = new StringBuffer();
        try {
            URL url = new URL("http://api.openweathermap.org/data/2.5/weather?q="+lugar+"&appid=71c846a1f1673915ae94e089fde7a9f3");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line = bufferedReader.readLine();

            while (line!= null){
                stringBuffer.append(line);
                line = bufferedReader.readLine();
                System.out.println(line+"-----------------------------------------------------");
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return stringBuffer.toString();
    }
}
