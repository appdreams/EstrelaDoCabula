package br.com.appdreams.estreladocabula.utils;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Paulo on 04/04/2017.
 */

public class PushNotifictionHelper {

    public final static String AUTH_KEY_FCM = "AAAAtPcsDXg:APA91bFa1ICKFh-wLfP52MwgkHhDiC26pDsyYmFCmAN2MiO1tqMlMX2ibveABhaNCJiHs-hCLQ8gAoFBUFECAnhB3Ctz-Q3ZT06qTbqMQoNtC8n1vH91Jb8A2ooUAw1JzY8-aVkfwlLO";
    public final static String API_URL_FCM = "https://fcm.googleapis.com/fcm/send";

    public static String sendPushNotification(String deviceToken)throws IOException {
        Log.i("PAULO", "fsaddfsa");
        String result = "";
        URL url = new URL(API_URL_FCM);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setUseCaches(false);
        conn.setDoInput(true);
        conn.setDoOutput(true);

        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization", "key=" + AUTH_KEY_FCM);
        conn.setRequestProperty("Content-Type", "application/json");

        JSONObject json = new JSONObject();



        // body
        try {
            json.put("to", deviceToken.trim());
            JSONObject info = new JSONObject();
            info.put("title", "notification title"); // Notification title
            info.put("body", "message body"); // Notification
            json.put("notification", info);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {

            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(json.toString());
            wr.flush();

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

            String output;
            Log.i("PAULO", "Output from Server ....");
            while ((output = br.readLine()) != null) {
                Log.i("PAULO", output);
            }
            result = "SUCCESS";
        } catch (Exception e) {
            e.printStackTrace();
            result = "FAILURE";
        }
        Log.i("PAULO", "GCM Notification is sent successfully");

        return result;
    }
}