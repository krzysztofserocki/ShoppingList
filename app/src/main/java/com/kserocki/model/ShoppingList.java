package com.kserocki.model;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Entity(tableName = "shopping_list")
@Getter
public class ShoppingList {

    @PrimaryKey(autoGenerate = true)
    private int listId;
    private String createdAt;
    private boolean isArchived;
    @Embedded
    private List<Item> items;

    public ShoppingList(String createdAt, boolean isArchived, List<Item> items) {
        this.createdAt = createdAt;
        this.isArchived = isArchived;
        this.items = items;
    }

    public void setListId(int listId) {
        this.listId = listId;
    }
}
