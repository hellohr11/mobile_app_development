package com.ranh.news;

import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

@RequiresApi(api = Build.VERSION_CODES.O)
public class SourceLoaderRunnable implements Runnable {

    private MainActivity mainActivity;
    private static final String TAG = "MediaDownloadRunnable";

    private static final String dataURL="https://newsapi.org/v2/sources?language=en&country=us&category=&apiKey=499dcca6de414d66bc829ef0c33a5f72";


    public SourceLoaderRunnable(MainActivity ma) {
        Log.d(TAG, "MediaLoaderRunnable: main");
        mainActivity = ma;
    }

    private void processResults(String s) {
        Log.d(TAG, "processResults: ");
        final ArrayList<Media> mediaList = parseJSON(s);
        if (mediaList != null)  {
            Log.d(TAG, "processResults: medialist not empty");
            mainActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mainActivity.updateData(mediaList);
                }
            });
        }
    }

    public void run() {
        Log.d(TAG, "run: run");

        Uri dataUri = Uri.parse(dataURL);
        String urlToUse = dataUri.toString();

        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(urlToUse);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.addRequestProperty("User-Agent","");
            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader((new InputStreamReader(is)));

            String line;
            while ((line = reader.readLine()) != null) {
                Log.d(TAG, "run: append");
                sb.append(line).append('\n');
            }

        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        processResults(sb.toString());
    }


    private ArrayList<Media> parseJSON(String s) {

        Log.d(TAG, "parseJSON: ");
        ArrayList<Media> mediaList = new ArrayList<>();
        try {
            JSONObject jObjm=new JSONObject(s);

            JSONArray jObjMain=jObjm.getJSONArray("sources");

            for (int i = 0; i < jObjMain.length(); i++) {
                Log.d(TAG, "parseJSON: i");
                JSONObject jCountry = (JSONObject) jObjMain.get(i);
                String name = jCountry.getString("name");
                String id = jCountry.getString("id");
                String category = jCountry.getString("category");
                String url = jCountry.getString("url");

                mediaList.add(
                        new Media(id,name,url,category));

            }
            return mediaList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
