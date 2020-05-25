package com.lamatias.pratosdaanabela.logic;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class UserClass implements User, Serializable {

    private String name;
    private List<Food> foods;

    public UserClass(String name){
        this.name = name;
        foods = new LinkedList<>();
    }

    @Override
    public String getName(){
        return name;
    }

    @Override
    public void insertFood(Food food) {
        foods.add(food);
    }

    @Override
    public void deleteFood(Food food) {
        foods.remove(food);
    }

    @Override
    public Iterator<Food> getFoods() {
        return foods.iterator();
    }
}
