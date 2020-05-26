package com.lamatias.pratosdaanabela.logic;

import java.util.Iterator;

public interface Food {
    String getFood();

    Iterator<User> getUsers();

    boolean isEatenBy(User user);
}
