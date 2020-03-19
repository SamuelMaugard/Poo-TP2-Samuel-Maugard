package com.example.android_tp01;

public interface Chat {
    public String obtenirTextTape();
    public void ajouterMessage (String userName,String msg);
    public void ajouterMessage (String msg,int color);
}
