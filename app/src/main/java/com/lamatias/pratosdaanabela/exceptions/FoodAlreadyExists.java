package com.lamatias.pratosdaanabela.exceptions;

public class FoodAlreadyExists extends Exception {
    public FoodAlreadyExists(){
        super("Esta comida já existe.");
    }
}
