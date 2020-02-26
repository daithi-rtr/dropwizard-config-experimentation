package com.example.brands;

public class Brand {

    private final Long id;
    private final String name;

    public Brand(Long id, String name){
        this.id = id;
        this.name = name;
    }

    public Long getId(){
        return this.id;
    }

    public String getName(){
        return this.name;
    }
}
