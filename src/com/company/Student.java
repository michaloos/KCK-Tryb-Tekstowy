package com.company;

import java.util.*;
import java.util.List;

public class Student {
    private String name;
    private String nazwisko;
    private int nr_ideksu;
    private int rok_studiow;
    private int ilosc_wyporz_ksiazek;

    public Student(String name, String nazwisko, int numer, int rok_studiow, int ilosc_wyporz_ksiazek){
        this.name = name;
        this.nazwisko = nazwisko;
        this.nr_ideksu = numer;
        this.rok_studiow = rok_studiow;
        this.ilosc_wyporz_ksiazek = ilosc_wyporz_ksiazek;
    }

    public String getName(){
        return name;
    }

    public String getNazwisko(){
        return nazwisko;
    }

    public int getNr_ideksu(){
        return nr_ideksu;
    }

    public int getRok_studiow(){
        return rok_studiow;
    }

    public int getIlosc_wyporz_ksiazek() { return ilosc_wyporz_ksiazek; }

}
