package com.tunglain.atmj02;

import org.json.JSONException;
import org.json.JSONObject;

public class Transaction {
    String account;
    String date;
    int amount;
    int type;

    public Transaction(String account, String date, int amount, int type) {
        this.account = account;
        this.date = date;
        this.amount = amount;
        this.type = type;
    }

    public Transaction(JSONObject jsonObject) {
        try {
            account = jsonObject.getString("account");
            date = jsonObject.getString("date");
            amount = jsonObject.getInt("amount");
            type = jsonObject.getInt("type");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
