package com.example.licentapbn.datatype;

import java.util.List;

public class MemberWithItems {
    private String name;
    private String phoneNumber;
    private String imageUrl;
    private List<Item> items;

    public MemberWithItems(String name, String phoneNumber, String imageUrl) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.imageUrl = imageUrl;
    }

    public MemberWithItems(String name, String phoneNumber, List<Item> itemsOwned,String imageUrl) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public MemberWithItems() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

}
