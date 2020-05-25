package com.lamatias.pratosdaanabela.logic;

import com.lamatias.pratosdaanabela.exceptions.FoodAlreadyExists;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class AppClass implements App, Serializable {

    public static String B = "Bia";
    public static String G = "Gonçalo";
    public static String M = "Márcia";
    public static String PB = "Pedro Biléu";
    public static String PG = "Pedro Grilo";
    private SortedMap<String, Food> foods;
    private Map<String, User> users;

    public AppClass() {
        foods = new TreeMap<>();
        users = new HashMap<>();
        users.put(B, new UserClass(B));
        users.put(G, new UserClass(G));
        users.put(M, new UserClass(M));
        users.put(PB, new UserClass(PB));
        users.put(PG, new UserClass(PG));
    }

    @Override
    public User getUser(String name) {
        return users.get(name);
    }

    @Override
    public void insertFood(String food, Iterator<User> users) throws FoodAlreadyExists {
        List<User> u = new LinkedList<>();
        while (users.hasNext())
            u.add(users.next());
        if (foods.containsKey(food))
            throw new FoodAlreadyExists();
        Food f = new FoodClass(food, u.iterator());
        foods.put(food, f);
        Iterator<User> it = u.iterator();
        while (it.hasNext())
            it.next().insertFood(f);
    }

    @Override
    public Iterator<Food> getFoodsIterator() {
        return foods.values().iterator();
    }

    @Override
    public void deleteFood(Food food) {
        Iterator<User> users = food.getUsers();
        while (users.hasNext())
            users.next().deleteFood(food);
        foods.remove(food.getFood());
    }

    @Override
    public Iterator<User> getUsers() {
        return users.values().iterator();
    }

    @Override
    public Iterator<Food> getUsersFood(String name) {
        User u = users.get(name);
        return u.getFoods();
    }
}
