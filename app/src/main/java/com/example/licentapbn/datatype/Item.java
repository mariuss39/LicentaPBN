package com.example.licentapbn.datatype;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Item implements Serializable{
        private String name;
        private String id;
        private String description;
        private String weight;
        private String size;
        private boolean free; // daca e available sau unavailable
        private String memberId;
        private String memberName;
        private boolean isExpanded;
        private String imageUrl;
        private boolean isStatusVisible; // daca e VIZIBIL AVAILABLE/UNAVAILABLE
        private boolean isReservationVisible; // daca e VIZIBIL REZERVAT/NEREZERVAT
        private boolean isReserved; // daca e Rezervat sau Nerezervat
        private Map<String, List<String>> reservationsMap=new HashMap<>();

        public Item() {
        }

    public boolean isReserved() {
        return isReserved;
    }

    public void setReserved(boolean reserved) {
        isReserved = reserved;
    }

    public boolean isReservationVisible() {
        return isReservationVisible;
    }

    public void setReservationVisible(boolean reserveable) {
        isReservationVisible = reserveable;
    }

    public Map<String, List<String>> getReservationsMap() {
        return reservationsMap;
    }

    public void setReservationsMap(Map<String, List<String>> reservationsMap) {
        this.reservationsMap = reservationsMap;
    }

    public String getMemberName() {
        return memberName;
    }

    public boolean isStatusVisible() {
        return isStatusVisible;
    }

    public void setStatusVisible(boolean statusVisible) {
        isStatusVisible = statusVisible;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }

    public Item(String name, String imageUrl) {
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public Item(String name, String id, String description, String weight, String size, boolean free, String memberId, String imageUrl) {
        this.name = name;
        this.id = id;
        this.description = description;
        this.weight = weight;
        this.size = size;
        this.free = free;
        this.memberId = memberId;
        this.isExpanded=false;
        this.imageUrl=imageUrl;
        this.isStatusVisible=false;
        this.isReservationVisible=false;
        this.isReserved=false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public boolean isFree() {
        return free;
    }

    public void setFree(boolean free) {
        this.free = free;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    @Override
    public String toString() {
        return "Item{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", description='" + description + '\'' +
                ", weight='" + weight + '\'' +
                ", size='" + size + '\'' +
                ", free=" + free +
                ", memberId='" + memberId + '\'' +
                ", memberName='" + memberName + '\'' +
                ", isExpanded=" + isExpanded +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
