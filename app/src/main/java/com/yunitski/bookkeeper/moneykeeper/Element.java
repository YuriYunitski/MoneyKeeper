package com.yunitski.bookkeeper.moneykeeper;

public class Element {
    String value;
    String totalValue;
    String date;
    int operation;
    String currency;


    public Element(String value, String totalValue, String date, int operation, String currency) {
        this.value = value;
        this.totalValue = totalValue;
        this.date = date;
        this.operation = operation;
        this.currency = currency;
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
