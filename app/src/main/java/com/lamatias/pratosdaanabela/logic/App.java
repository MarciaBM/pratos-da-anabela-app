package com.lamatias.pratosdaanabela.logic;

import com.lamatias.pratosdaanabela.exceptions.FoodAlreadyExists;

import java.util.Iterator;

public interface App {

    User getUser(String name);

    void insertFood(String food, Iterator<User> users) throws FoodAlreadyExists;

    Iterator<Food> getFoodsIterator();

    void deleteFood(Food food);

    Iterator<User> getUsers();

    Iterator<Food> getUsersFood(String name);
}
