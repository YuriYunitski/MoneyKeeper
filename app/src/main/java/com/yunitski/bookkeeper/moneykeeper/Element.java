package com.yunitski.bookkeeper.moneykeeper;


//этот класс нужен для создания элементов в recyclerview
public class Element {
    String value;
    String totalValue;
    String date;
    int operation;
    String currency;
    String category;


    public Element(String value, String totalValue, String date, int operation, String currency, String category) {
        this.value = value;
        this.totalValue = totalValue;
        this.date = date;
        this.operation = operation;
        this.currency = currency;
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public int getOperation() {
        return operation;
    }

    public void setOperation(int operation) {
        this.operation = operation;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(String totalValue) {
        this.totalValue = totalValue;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
