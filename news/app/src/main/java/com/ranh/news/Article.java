package com.ranh.news;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.Serializable;
import java.time.Instant;
import java.util.Date;


public class Article implements Serializable {
    private String sourceName;
    private String author;
    private String title;
    private String description;
    private String url;
    private String urlToImage;
    private String publishedAt;

    public Article(String sourceName, String author, String title, String description, String url, String urlToImage, String publishedAt) {
        this.sourceName=sourceName;
        this.author = author;
        this.title = title;
        this.description = description;
        this.url = url;
        this.urlToImage = urlToImage;
        this.publishedAt = publishedAt;
    }

    public String getSourceName(){return sourceName;}

    public String getAuthor() {

        if(author.length()>20 || author.equals("null")){
            return "";
        }
        return author;
    }

    public String getTitle() {

        if(title.equals("null")){
            return "";
        }
        return title;
    }

    public String getDescription() {
        if(description.equals("null")){
            return "";
        }
        return description;
    }

    public String getUrl() {
        return url;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getPublishedAt() {
        Instant instant = Instant.parse( publishedAt);
        Date d = Date.from( instant ) ;
        return d.toString();
    }


}
