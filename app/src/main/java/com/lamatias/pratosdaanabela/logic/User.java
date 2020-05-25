package com.lamatias.pratosdaanabela.logic;

import java.util.Iterator;

public interface User {
    String getName();

    void insertFood(Food food);

    void deleteFood(Food food);

    Iterator<Food> getFoods();
}
