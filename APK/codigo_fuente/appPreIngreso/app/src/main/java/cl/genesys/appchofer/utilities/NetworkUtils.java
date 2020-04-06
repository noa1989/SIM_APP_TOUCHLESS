package cl.genesys.appchofer.utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class NetworkUtils {
    public static String getJSONFromAPI (String url){
        String output = "";
        try {
            URL apiEnd = new URL(url);
            int responseCode;
            HttpURLConnection connection;
            InputStream is;

            connection = (HttpURLConnection) apiEnd.openConnection();
            connection.setRequestMethod("GET");
            connection.setReadTimeout(15000);
            connection.setConnectTimeout(15000);
            connection.connect();

            responseCode = connection.getResponseCode();
            if(responseCode < HttpURLConnection.HTTP_BAD_REQUEST){
                is = connection.getInputStream();
            }else {
                is = connection.getErrorStream();
            }

            output = convertISToString(is);
            is.close();
            connection.disconnect();

        }  catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return output;
    }

    private static String convertISToString(InputStream is){
        StringBuffer buffer = new StringBuffer();

        try {

            BufferedReader br;
            String row;

            br = new BufferedReader(new InputStreamReader(is));
            while ((row = br.readLine())!= null){
                buffer.append(row);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }
}
