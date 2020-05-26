package com.lamatias.pratosdaanabela.exceptions;

import java.io.Serializable;

public class EmptyFieldsException extends Exception implements Serializable {
    public EmptyFieldsException(){
        super("Campos vazios!");
    }
}
