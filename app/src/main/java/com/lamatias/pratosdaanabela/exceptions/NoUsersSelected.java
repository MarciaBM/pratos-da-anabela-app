package com.lamatias.pratosdaanabela.exceptions;

import java.io.Serializable;

public class NoUsersSelected extends Exception implements Serializable {
    public NoUsersSelected(){
        super("Por favor selecione um utilizador.");
    }
}
