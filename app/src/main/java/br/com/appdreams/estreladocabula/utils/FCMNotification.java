package br.com.appdreams.estreladocabula.utils;

import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Paulo on 03/04/2017.
 */

public class FCMNotification {

    // Method to send Notifications from server to client end.
    public final static String AUTH_KEY_FCM = "AAAAtPcsDXg:APA91bFa1ICKFh-wLfP52MwgkHhDiC26pDsyYmFCmAN2MiO1tqMlMX2ibveABhaNCJiHs-hCLQ8gAoFBUFECAnhB3Ctz-Q3ZT06qTbqMQoNtC8n1vH91Jb8A2ooUAw1JzY8-aVkfwlLO";
    public final static String API_URL_FCM = "https://fcm.googleapis.com/fcm/send";

    public static void pushFCMNotification(String DeviceIdKey) throws Exception {

        String authKey = AUTH_KEY_FCM; // You FCM AUTH key
        String FMCurl = API_URL_FCM;

        URL url = new URL(FMCurl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        Log.i("PAULO",authKey);
        Log.i("PAULO",FMCurl);
        Log.i("PAULO",DeviceIdKey);
        conn.setUseCaches(false);
        conn.setDoInput(true);
        conn.setDoOutput(true);

        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization", "key=" + authKey);
        conn.setRequestProperty("Content-Type", "application/json");

        JSONObject data = new JSONObject();
        //data.put("to", DeviceIdKey.trim());
        JSONObject info = new JSONObject();
        //info.put("title", "FCM Notificatoin Title"); // Notification title
        //info.put("body", "Hello First Test notification"); // Notification body
        //data.put("notification", info);/**/

        Log.i("PAULO",data.toString());


        JSONObject jsonObj = new JSONObject("{\n" +
                "    \"to\":\"evHyC_fusco:APA91bGhhcD-4xwAH4YQkBqKVQ4Gznzbq1sByjLpXa_7hjozkpUyt3t29h54zPic5hUR-u-fMNNS_qlU-WbXcZkmQ9jWmdQk3ylR6mFUrN3TrRLNueGyRwXZlTLK48kSesSzrY6fKVDS\",\n" +
                "    \"notification\":\n" +
                "    {\n" +
                "        \"title\":\"FCM Notificatoin Title\",\n" +
                "        \"body\":\"Hello First Test notification\"\n" +
                "    }\n" +
                "}\n");
        data.put("data", jsonObj);

        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
        wr.write(data.toString());
        wr.flush();
        wr.close();

        int responseCode = conn.getResponseCode();
        Log.i("PAULO","Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

    }

    @SuppressWarnings("static-access")
    public static void main(String[] args) throws Exception {
        //FCMNotification obj = new FCMNotification();
        //obj.pushFCMNotification("evHyC_fusco:APA91bGhhcD-4xwAH4YQkBqKVQ4Gznzbq1sByjLpXa_7hjozkpUyt3t29h54zPic5hUR-u-fMNNS_qlU-WbXcZkmQ9jWmdQk3ylR6mFUrN3TrRLNueGyRwXZlTLK48kSesSzrY6fKVDS");
    }/**/
}