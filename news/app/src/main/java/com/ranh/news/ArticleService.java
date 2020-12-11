package com.ranh.news;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.io.Serializable;
import java.util.ArrayList;

public class ArticleService extends Service implements Serializable {
    private static final String TAG = "ArticleService";
    //public static final String DATA_EXTRA = "DATA_EXTRA";
    public static final String DATA_ARTICLE = "ARTICLE";
    private boolean running = true;
    private ArrayList<Article> alist=new ArrayList<>();

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Creating new thread for my service
        //ALWAYS write your long running tasks
        // in a separate thread, to avoid an ANR issue

        String sid = intent.getStringExtra("source id");
        ArticleDownloadRunnable alr = new ArticleDownloadRunnable(this,sid);
        new Thread(alr).start();
        return Service.START_NOT_STICKY;
    }

    public void setAlist(ArrayList<Article> articlelist) {
        alist.clear();
        alist.addAll(articlelist);
        sendBroadcast(alist);

    }

    private void sendBroadcast(ArrayList<Article> a) {
        Intent intent = new Intent();
        intent.setAction(MainActivity.MESSAGE_FROM_SERVICE);
        intent.putExtra(DATA_ARTICLE, a);
        sendBroadcast(intent);
    }

    @Override
    public void onDestroy() {
        // Send a message when destroyed
        running = false;
        super.onDestroy();
    }

}
