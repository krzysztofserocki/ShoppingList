package com.kserocki.model;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShoppingListWithItems {
    @Embedded
    public ShoppingList shoppingList;
    @Relation(
            parentColumn = "listId",
            entityColumn = "myListId"
    )
    public List<Item> items;



}
