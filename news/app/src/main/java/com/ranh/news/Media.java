package com.ranh.news;


import java.io.Serializable;

public class Media
        implements Serializable { // Needed to add as extra


    private String id;
    private String name;
    private String url;
    private String category;
    private Integer color;


    Media(String id, String name, String url, String category){
        this.id=id;
        this.name=name;
        this.url=url;
        this.category=category;
        if(category.equalsIgnoreCase("business")){
            color=0xFF5733;
        }else if(category.equalsIgnoreCase("entertainment")){
            color=0xFFD333;
        }else if(category.equalsIgnoreCase("general")){
            color=0x91FF33;
        }else if(category.equalsIgnoreCase("health")){
            color=0x33FFE5;
        }else if(category.equalsIgnoreCase("science")){
            color=0x3363FF;
        }else if(category.equalsIgnoreCase("sports")){
            color=0xFF33F8;

        }else if(category.equalsIgnoreCase("technology")){
            color=0xFF3386;
        }else{
            color=0x000000;
        }
    }

    Integer getColor(){return color;}

    String getId(){
        return id;
    }

    String getName(){
        return name;
    }

    String getUrl(){
        return url;
    }

    String getCategory(){
        return category;
    }

    public String toString() {
        return name;
    }


}