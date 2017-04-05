package br.com.appdreams.estreladocabula.utils;

import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Paulo on 04/04/2017.
 */

public class SendNotifi {
    public final static String AUTH_KEY_FCM = "AAAAtPcsDXg:APA91bFa1ICKFh-wLfP52MwgkHhDiC26pDsyYmFCmAN2MiO1tqMlMX2ibveABhaNCJiHs-hCLQ8gAoFBUFECAnhB3Ctz-Q3ZT06qTbqMQoNtC8n1vH91Jb8A2ooUAw1JzY8-aVkfwlLO";
    public final static String API_URL_FCM = "https://fcm.googleapis.com/fcm/send";
    // userDeviceIdKey is the device id you will query from your database
    public static void pushFCMNotification(String userDeviceIdKey) throws     Exception{

        String authKey = AUTH_KEY_FCM;   // You FCM AUTH key
        String FMCurl = API_URL_FCM;

        URL url = new URL(FMCurl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        //conn.setUseCaches(false);
        conn.setRequestMethod("POST");
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setRequestProperty("Connection", "keep-alive");
        conn.setUseCaches(false);


        conn.setRequestProperty("Authorization","key="+authKey);
        conn.setRequestProperty("Content-Type","application/json");

        JSONObject json = new JSONObject();
        json.put("to",userDeviceIdKey.trim());
        JSONObject info = new JSONObject();

        info.put("title", "Notificatoin Title - IOS");   // Notification title
        info.put("body", "Hello Test notification - IOS"); // Notification body
        info.put("badge", "1");
        json.put("notification", info);
        json.put("priority", "high");

        System.out.println("json : " +json);

        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
        wr.write(json.toString());
        wr.flush();

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        Log.i("PAULO",response.toString());
    }
    public static void main(String main[])
    {
        try {
            SendNotifi.pushFCMNotification("evHyC_fusco:APA91bGhhcD-4xwAH4YQkBqKVQ4Gznzbq1sByjLpXa_7hjozkpUyt3t29h54zPic5hUR-u-fMNNS_qlU-WbXcZkmQ9jWmdQk3ylR6mFUrN3TrRLNueGyRwXZlTLK48kSesSzrY6fKVDS");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}