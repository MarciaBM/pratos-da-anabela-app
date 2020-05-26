package com.lamatias.pratosdaanabela.logic;

import com.lamatias.pratosdaanabela.exceptions.FoodAlreadyExists;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
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
    private Food lastRandom;

    public AppClass() {
        foods = new TreeMap<>();
        users = new HashMap<>();
        users.put(B, new UserClass(B));
        users.put(G, new UserClass(G));
        users.put(M, new UserClass(M));
        users.put(PB, new UserClass(PB));
        users.put(PG, new UserClass(PG));
        lastRandom = null;
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
        for (User user : u) user.insertFood(f);
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

    @Override
    public String getFoodByUsers(List<User> users) {
        List<Food> food = new LinkedList<>();
        for (Food f : foods.values()) {
            Iterator<User> it2 = users.iterator();
            boolean notEaten = true;
            while (it2.hasNext()) {
                if (!f.isEatenBy(it2.next()))
                    notEaten = false;
            }
            if (notEaten)
                food.add(f);
        }

        if (food.isEmpty())
            return "Nenhuma ideia :(";
        else if (food.size() == 1)
            return food.get(0).getFood() + " (única hipótese)";
        else {
            Random rand = new Random();
            Food random;
            do {
                random = food.get(rand.nextInt(food.size()));
            } while (random == lastRandom);
            lastRandom = random;
            return random.getFood();
        }
    }
}
