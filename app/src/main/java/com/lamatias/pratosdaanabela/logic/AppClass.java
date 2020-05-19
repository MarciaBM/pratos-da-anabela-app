package com.lamatias.pratosdaanabela.logic;

import com.lamatias.pratosdaanabela.exceptions.FoodAlreadyExists;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class AppClass implements App, Serializable {

    private SortedMap<String,Food> foods;
    private Map<Names,User> users;

    public AppClass (){
        foods = new TreeMap<>();
        users = new HashMap<>();
        users.put(Names.B,new UserClass("Bia"));
        users.put(Names.G,new UserClass("Gonçalo"));
        users.put(Names.M,new UserClass("Márcia"));
        users.put(Names.PB,new UserClass("Pedro Biléu"));
        users.put(Names.PG,new UserClass("Pedro Grilo"));
    }

    @Override
    public User getUser(Names name){
        return users.get(name);
    }

    @Override
    public void insertFood(String food, Iterator<User> users) throws FoodAlreadyExists {
        if(foods.containsKey(food))
            throw new FoodAlreadyExists();
        foods.put(food, new FoodClass(food,users));
    }

    @Override
    public Iterator<Food> getFoodsIterator(){
        return foods.values().iterator();
    }

    @Override
    public void deleteFood(Food food){
        Iterator<User> users = food.getUsers();
        while (users.hasNext())
            users.next().deleteFood(food.getFood());
        foods.remove(food.getFood());
    }

    @Override
    public Iterator<User> getUsers(){
        return users.values().iterator();
    }
}
