package com.lamatias.pratosdaanabela.logic;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class UserClass implements User, Serializable {

    private String name;
    private List<String> foods;

    public UserClass(String name){
        this.name = name;
        foods = new LinkedList<>();
    }

    @Override
    public String getName(){
        return name;
    }

    @Override
    public void insertFood(String food){
        foods.add(food);
    }

    @Override
    public void deleteFood(String food){
        foods.remove(food);
    }
}
