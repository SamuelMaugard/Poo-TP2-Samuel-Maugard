package com.example.android_tp01;

public class Preference {
    String surnom="Samuel";

    public String obtenirSurnom() {
        return surnom ;
    }

    public void changerSurnom(String surnom) {
        this.surnom = surnom;
    }
}
