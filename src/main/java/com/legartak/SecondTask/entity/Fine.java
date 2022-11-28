package com.legartak.SecondTask.entity;

public class Fine {
    private Type type;
    private int fineAmount;

    public Fine() {
    }

    public Fine(Type type, int fineAmount) {
        this.type = type;
        this.fineAmount = fineAmount;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public int getFineAmount() {
        return fineAmount;
    }

    public void setFineAmount(int fineAmount) {
        this.fineAmount = fineAmount;
    }
}
