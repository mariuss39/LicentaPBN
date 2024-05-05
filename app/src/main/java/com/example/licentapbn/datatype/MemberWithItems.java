package com.example.licentapbn.datatype;

import java.util.List;

public class MemberWithItems {
    private String name;
    private String phoneNumber;
    private List<Item> itemsOwned;
    private String imageUrl;
    private boolean isExpandable=false;

    public boolean isExpandable() {
        return isExpandable;
    }

    public void setExpandable(boolean expandable) {
        isExpandable = expandable;
    }

    public MemberWithItems(String name, String phoneNumber, List<Item> itemsOwned, String imageUrl) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.itemsOwned = itemsOwned;
        this.imageUrl = imageUrl;
    }

    public MemberWithItems(String name, String phoneNumber, List<Item> itemsOwned) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.itemsOwned = itemsOwned;
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

    public List<Item> getItemsOwned() {
        return itemsOwned;
    }

    public void setItemsOwned(List<Item> itemsOwned) {
        this.itemsOwned = itemsOwned;
    }
}
