package com.example.tabapplication.model;

public class OrderItem {
    private int id;
    private Item item;
    private int number,numChairs;

    public int getNumChairs() {
        return numChairs;
    }

    public void setNumChairs(int numChairs) {
        this.numChairs = numChairs;
    }

    public OrderItem(int id, Item item, int number) {
        this.id = id;
        this.item = item;
        this.number = number;
    }

    public OrderItem() {
    }

    public OrderItem(int id, Item item, int number, int numChairs) {
        this.id = id;
        this.item = item;
        this.number = number;
        this.numChairs = numChairs;
    }
//    public OrderItem(int id, Item item, int number ) {
//        this.id = id;
//        this.item = item;
//        this.number = number;
//    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

}
