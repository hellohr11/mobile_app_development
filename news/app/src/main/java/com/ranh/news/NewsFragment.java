package com.ranh.news;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import java.io.ObjectInputStream;
import java.util.Locale;


public class NewsFragment extends Fragment {

    private ObjectInputStream.GetField Picasso;
    private static final String TAG = "NewsFragment";
    public NewsFragment(){

    }

    static NewsFragment newInstance(Article article, int index, int max)
    {
        Log.d(TAG, "newInstance: ");
        NewsFragment f = new NewsFragment();
        Bundle bdl = new Bundle(1);
        bdl.putSerializable("News_DATA", article);
        bdl.putSerializable("INDEX", index);
        bdl.putSerializable("TOTAL_COUNT", max);
        f.setArguments(bdl);
        return f;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View fragment_layout = inflater.inflate(R.layout.fragment_news, container, false);

        Log.d(TAG, "onCreateView: ");
        Bundle args = getArguments();
        if (args != null) {
            final Article currentArticle = (Article) args.getSerializable("News_DATA");
            if (currentArticle == null) {
                return null;
            }
            int index = args.getInt("INDEX");
            int total = args.getInt("TOTAL_COUNT");

            TextView title = fragment_layout.findViewById(R.id.title);

            title.setText(currentArticle.getTitle());
            title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickFlag(currentArticle.getUrl());
                }
            });
            TextView author = fragment_layout.findViewById(R.id.author);
            author.setText(currentArticle.getAuthor());

            TextView time = fragment_layout.findViewById(R.id.time);
            time.setText(currentArticle.getPublishedAt());

            TextView description = fragment_layout.findViewById(R.id.description);
            description.setText(currentArticle.getDescription());
            description.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickFlag(currentArticle.getUrl());
                }
            });

            TextView pageNum = fragment_layout.findViewById(R.id.page_num);
            pageNum.setText(String.format(Locale.US, "%d of %d", index, total));

            ImageView imageView = fragment_layout.findViewById(R.id.imageView);
            com.squareup.picasso.Picasso.get().load(currentArticle.getUrlToImage()).into(imageView);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickFlag(currentArticle.getUrl());
                }
            });


            return fragment_layout;
        } else {
            return null;
        }


    }


    private void clickFlag(String name) {

        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(name));
        startActivity(i);

    }
}