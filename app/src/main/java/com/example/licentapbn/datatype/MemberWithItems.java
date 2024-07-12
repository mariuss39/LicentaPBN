package com.example.licentapbn.datatype;

import java.util.List;

public class MemberWithItems {
    private String name;
    private String id;
    private String email;
    private String phoneNumber;
    private String imageUrl;
    private List<Item> items;

    public MemberWithItems( String id, String email,String name, String phoneNumber, String imageUrl) {
        this.name = name;
        this.id = id;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.imageUrl = imageUrl;
    }

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
