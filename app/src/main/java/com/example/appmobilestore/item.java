package com.example.appmobilestore;

public class item {
    private String name;
    private String tipo;
    private String id;
    item(String n,String id,String t){
        name=n;
        this.id=id;
        tipo=t;
    }

    public String getName() {
        return name;
    }
    public String getTipo() {
        return tipo;
    }
    public String getId() {
        return id;
    }
}
