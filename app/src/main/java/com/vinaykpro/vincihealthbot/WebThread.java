package com.vinaykpro.vincihealthbot;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class WebThread extends AsyncTask<String, Void, String> {
    private Context mContext;
    public WebThread(Context mContext) {
        this.mContext = mContext;
    }
    @Override
    protected String doInBackground(String... strings) {
        JSONObject jobj=null;
        try {
            String text = "";
            BufferedReader reader = null;
            URL url;
            if(strings[0].equals("parse")) {
                url = new URL("https://api.infermedica.com/v3/parse");
            } else {
                url = new URL("https://api.infermedica.com/v3/diagnosis");
            }
             // /diagnosis
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.addRequestProperty("App-Id", "7439e2e6"); //85483e12 old
            conn.addRequestProperty("App-Key", "cd68bb4e81a0a74324cc0894ee09e519"); // 210ed69f6527610d571a883331fb8582 old
            conn.addRequestProperty("Content-Type", "application/json");
            OutputStream os = conn.getOutputStream();
            JSONObject obj = new JSONObject();


            //l.add(new JSONObject().put("id","s_388").put("choice_id","present"));
            //obj.put("evidence",l);
            JSONObject parseobj = new JSONObject();
            parseobj.put("text",strings[1]);
            parseobj.put("age",new JSONObject().put("value",30));
            //os.write("{ \"sex\": \"male\", \"age\": { \"value\": 30 }, \"evidence\": [  { \"id\": \"s_388\", \"choice_id\": \"present\" }] }".getBytes(StandardCharsets.UTF_8));
            //os.write("{\"text\":\"pimples\",\"age\":{\"value\":30}}".getBytes(StandardCharsets.UTF_8));
            //os.write(obj.toString().getBytes(StandardCharsets.UTF_8));

            //String s1 = "\"\\[",s2 = "]\"";
            //String s = obj.toString().replaceAll("\\\\","").replaceAll(s1,"[").replaceAll(s2,"]");
            //Log.d("Value",s);
            if(strings[0].equals("parse"))
            { os.write(parseobj.toString().getBytes(StandardCharsets.UTF_8)); }
            else {
                obj.put("sex", "male");
                obj.put("age", new JSONObject().put("value", 30));
                JSONArray evidence = new JSONArray();
                Log.d("testval1",strings[1]);
                String[] arr = strings[1].split("\\{vin\\}");
                for (String s : arr) {
                    String[] sarr = s.split("\\|");
                    sarr[0] = sarr[0].replaceAll("\\\\","");
                    sarr[1] = sarr[1].replaceAll("\\\\","");
                    sarr[2] = sarr[2].replaceAll("\\\\","");
                    if(sarr[2].equals("initial")) {
                        evidence.put(new JSONObject().put("id", sarr[0]).put("choice_id", sarr[1]).put("source", "initial"));
                    } else {
                        evidence.put(new JSONObject().put("id", sarr[0]).put("choice_id", sarr[1]));
                    }
                }
                obj.put("evidence", evidence);
                obj.put("extras", new JSONObject().put("disable_groups", true));
                Log.d("testval",obj.toString());
                os.write(obj.toString().getBytes(StandardCharsets.UTF_8));
            }
            //Log.d("Success Text","{ \"sex\": \"male\", \"age\": { \"value\": 30 }, \"evidence\": [  { \"id\": \"s_388\", \"choice_id\": \"present\" }] }");
            os.close();
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                // Append server response in string
                sb.append(line + "\n");
            }
            text = sb.toString();
            reader.close();
            jobj = new JSONObject(text);
            return jobj.toString();
        }
        catch (Exception e) {
            e.printStackTrace();
            return e.toString();
        }
    }

    @Override
    protected void onPostExecute(String result) {
        // result holds what you return from doInBackground;
        MainActivity cont = (MainActivity) mContext;
        cont.setText(result);
        //cont.main.setText(result.toString());
    }
}
