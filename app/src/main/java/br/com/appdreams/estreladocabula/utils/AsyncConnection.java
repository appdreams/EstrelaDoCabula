package br.com.appdreams.estreladocabula.utils;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by Paulo on 06/04/2017.
 */

public class AsyncConnection extends AsyncTask<String, Void, Void>
{
    @Override
    protected Void doInBackground(String... strings)
    {

        final String userDeviceIdKey = strings[0];
        URL url = null;
        try
        {
            url = new URL("https://fcm.googleapis.com/fcm/send");

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setUseCaches(false);
            conn.setDoInput(true);
            conn.setDoOutput(true);

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization","key=AAAAtPcsDXg:APA91bFa1ICKFh-wLfP52MwgkHhDiC26pDsyYmFCmAN2MiO1tqMlMX2ibveABhaNCJiHs-hCLQ8gAoFBUFECAnhB3Ctz-Q3ZT06qTbqMQoNtC8n1vH91Jb8A2ooUAw1JzY8-aVkfwlLO");
            conn.setRequestProperty("Content-Type","application/json");

            JSONObject json = new JSONObject();
            json.put("to",userDeviceIdKey.trim());
            JSONObject info = new JSONObject();
            info.put("title", "Enviando msg");   // Notification title
            info.put("body", "cadastrou"); // Notification body
            json.put("notification", info);

            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(json.toString());
            wr.flush();
            conn.getInputStream();

        } catch (MalformedURLException e)
        {
            e.printStackTrace();
        } catch (ProtocolException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
        return null;
    }

}