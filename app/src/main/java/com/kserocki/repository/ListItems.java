package com.kserocki.repository;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.kserocki.repository.Item.ItemEntity;
import com.kserocki.repository.List.ListEntity;

import java.util.List;

public class ListItems {

    @Embedded
    public ListEntity list;

    @Relation(parentColumn = "id", entityColumn = "list_id", entity = ItemEntity.class)
    public List<ItemEntity> itemsList;

}
