package com.company;

import java.util.List;
import java.util.*;

public class Book {
    private String title;
    private String autor;
    private int year;
    private double prize;
    private int count;

    public Book(){
        title=null;
        autor=null;
        year=0;
        prize=0;
        count=0;
    }

    public Book(String title, String autor, int year, double prize, int count){
        this.title = title;
        this.autor = autor;
        this.year = year;
        this.prize = prize; //jeżeli ktoś nie chce wyporzyczyć, tylko kupic na własność
        this.count = count;
    }

    public String getTitle() {
        return title;
    }

    public String getAutor() { return autor; }

    public int getYear() {
        return year;
    }

    public double getPrize() {
        return prize;
    }

    public int getCount() { return count; }
    public void setCount(int i) { this.count = i;}

    @Override
    public String toString() {
        return "Book{" +
                "title='" + title + '\'' +
                ", autor=" + autor +
                ", year=" + year +
                ", prize=" + prize +
                ", count=" + count +
                '}';
    }

    List<Book> bookList = new ArrayList<Book>();

    public void Add(Book book){
        bookList.add(book);
    }

    public void Delete(Book book){
        bookList.remove(book);
    }

    public void DeleteAt(int x){
        bookList.remove(x);
    }

    public int Count(){
        return bookList.size();
    }

}
