package com.ranh.news;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class ArticleDownloadRunnable implements Runnable {
    //download stocks symbol and company name from stockURL, save it to markets.json

    private static final String TAG = "ArticleDownloadRunnable";

    private ArticleService as;
    private static final String articleURL="https://newsapi.org/v2/499dcca6de414d66bc829ef0c33a5f72top-headlines?sources=";
    private static final String api="&language=en&apiKey=499dcca6de414d66bc829ef0c33a5f72";
    private String newsid;


    ArticleDownloadRunnable(ArticleService as, String id) {
        this.as = as;
        newsid=id;
    }


    @Override
    public void run() {
        Log.d(TAG, "run: "+newsid);

        String dataURL="https://newsapi.org/v2/top-headlines?sources="+newsid
                +"&language=en&apiKey=499dcca6de414d66bc829ef0c33a5f72";
        Log.d(TAG, "run: "+dataURL);

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

        as.setAlist(processResults(sb.toString()));
    }

    public ArrayList<Article> processResults(String s) {
        ArrayList<Article> alist=new ArrayList<>();
        try{
            JSONObject jsonmain=new JSONObject(s);
            int length=jsonmain.getInt("totalResults");
            int min=10;

            JSONArray articleArray=jsonmain.getJSONArray("articles");
            if(articleArray.length()<10){
                min=articleArray.length();
            }
            for(int i=0; i<min;i++){

                JSONObject Jarticle=(JSONObject)articleArray.get(i);
                JSONObject source=Jarticle.getJSONObject("source");
                String sourcename=source.getString("name");
                String author=Jarticle.getString("author");
                String title=Jarticle.getString("title");
                String url=Jarticle.getString("url");
                String urlToImage=Jarticle.getString("urlToImage");
                String time=Jarticle.getString("publishedAt");
                String description=Jarticle.getString("content");
                Article a=new Article(sourcename,author,title,description,url,urlToImage,time);
                alist.add(a);

            }
            Log.d(TAG, "processResults: "+alist.size());
            return alist;


        }
        catch (Exception e){
            e.printStackTrace();
        }

        return null;


    }


}
