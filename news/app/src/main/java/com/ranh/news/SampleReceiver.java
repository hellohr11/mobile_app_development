package com.ranh.news;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;

class SampleReceiver extends BroadcastReceiver {

    private static final String TAG = "SampleReceiver";
    private MainActivity mainActivity;
    private ArrayList<Article> data=new ArrayList<>();

    public SampleReceiver(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();
        if (action == null)
            return;

        if (MainActivity.MESSAGE_FROM_SERVICE.equals(action)) {

            if (intent.hasExtra(ArticleService.DATA_ARTICLE))

                data = (ArrayList<Article>) intent.getSerializableExtra(ArticleService.DATA_ARTICLE);
                Log.d(TAG, "onReceive: "+data.size());

                mainActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mainActivity.setArticles(data);
                    }
                });


        } else {
            Log.d(TAG, "onReceive: Unknown broadcast received");
        }
    }
}
