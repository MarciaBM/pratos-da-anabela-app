package com.lamatias.pratosdaanabela.logic;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class FoodClass implements Food, Serializable {

    private String food;
    private List<User> users;

    public FoodClass (String food, Iterator<User> users){
        this.food = food;
        this.users = new LinkedList<>();
        while (users.hasNext())
            this.users.add(users.next());
    }

    @Override
    public String getFood(){
        return food;
    }

    @Override
    public Iterator<User> getUsers(){
        return users.iterator();
    }
}
