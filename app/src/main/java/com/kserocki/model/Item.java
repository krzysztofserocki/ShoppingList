package com.kserocki.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Entity
@Getter
public class Item {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private int myListId;
    private String name;

    public Item(int myListId, String name) {
        this.myListId = myListId;
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }
}
