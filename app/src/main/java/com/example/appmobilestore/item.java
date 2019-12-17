package com.example.appmobilestore;

public class item {
    private String name;
    private String id;
    item(String n,String id){
        name=n;
        this.id=id;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }
}
