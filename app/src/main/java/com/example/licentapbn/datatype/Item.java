package com.example.licentapbn.datatype;

import java.io.Serializable;

public class Item implements Serializable{
        private String name;
        private String id;

        public Item() {
        }

        public Item(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String nume) {
            this.name = nume;
        }
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        @Override
        public String toString() {
            return "Item{" +
                    "name='" + name + '\'' +
                    ", id='" + id + '\'' +
                    '}';
        }
}
