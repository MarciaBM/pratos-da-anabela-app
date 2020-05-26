package com.lamatias.pratosdaanabela.exceptions;

import java.io.Serializable;

public class FoodAlreadyExists extends Exception implements Serializable {
    public FoodAlreadyExists(){
        super("Esta comida já existe.");
    }
}
