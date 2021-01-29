package com.example.firebasepushnotify;

public class Areas {
    private String id;
    private String  name;

    public Areas(String id, String  name )
    {
        this.id = id;
        this.name = name;

    }
    public String getId(){
        return id;
    }

    public String getName()
    {
        return name;
    }

}
