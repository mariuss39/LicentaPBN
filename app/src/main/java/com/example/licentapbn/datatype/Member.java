package com.example.licentapbn.datatype;

import java.io.Serializable;
import java.util.List;

public class Member implements Serializable {
    private String name;
    private String email;
    private String phoneNumber;
    private List<String> itemsId;
    private String id;
    private String imageUrl;
    private List<Item> itemsOwned;
    private boolean isExpanded;

    public Member() {
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }

    public List<Item> getItemsOwned() {
        return itemsOwned;
    }

    public void setItemsOwned(List<Item> itemsOwned) {
        this.itemsOwned = itemsOwned;
    }

    public Member(String name, String email, String phoneNumber, List<String> itemsId, String id, String imageUrl) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.itemsId = itemsId;
        this.id = id;
        this.imageUrl = imageUrl;
    }

    public Member(String name, String phoneNumber, String imageUrl, List<Item> itemsOwned) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.imageUrl = imageUrl;
        this.itemsOwned = itemsOwned;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public List<String> getItemsId() {
        return itemsId;
    }

    public void setItemsId(List<String> itemsId) {
        this.itemsId = itemsId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Member{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", itemsId=" + itemsId +
                ", id='" + id + '\'' +
                '}';
    }
}


