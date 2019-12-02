package com.example.flexbattle;

import java.io.Serializable;

public class ShopGameItem implements Serializable {

    private String game_title;

    private int game_price;

    private int photo;

    private String description;

    private int user_money;

    private String user_login;

    public String getGame_title() {
        return game_title;
    }

    public void setGame_title(String game_title) {
        this.game_title = game_title;
    }

    public int getGame_price() {
        return game_price;
    }

    public void setGame_price(int game_price) {
        this.game_price = game_price;
    }

    public int getPhoto() {
        return photo;
    }

    public void setPhoto(int photo) {
        this.photo = photo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getUser_money() {
        return user_money;
    }

    public void setUser_money(int user_money) {
        this.user_money = user_money;
    }

    public String getUser_login() {
        return user_login;
    }

    public void setUser_login(String user_login) {
        this.user_login = user_login;
    }

    public ShopGameItem(String title, Integer price, String description){
        this.game_title = title;
        this.game_price = price;
        this.description = description;

    }

    public ShopGameItem(String login, int money){
        this.user_login = login;
        this.user_money = money;
    }
}
