package com.richardgrable.budgettracker.models;

public class ResponseModel<T> {
    public boolean success;
    public String token;
    public String action;
    public String message;
    public T result;
}
