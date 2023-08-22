package com.test.bottomnavigation;

public class ResepModel {
    String id,author;

    public ResepModel(String id,String author){
        this.id = id;
        this.author = author;
    }
    public ResepModel(){

    }
    public String getId(){
        return  id;
    }
    public void setId(String id){
        this.id = id;
    }

    public String getAuthor(){
        return  author;
    }
    public void setAuthor(String author){
        this.author = author;
    }


}
