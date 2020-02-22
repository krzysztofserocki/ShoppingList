package com.kserocki.repository.List;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.kserocki.repository.Item.ItemEntity;

import java.util.List;

public class ListItems {

    @Embedded
    private ListEntity list;

    @Relation(parentColumn = "id", entityColumn = "list_id", entity = ItemEntity.class)
    private List<ItemEntity> itemsList;

    public ListEntity getList() {
        return list;
    }

    public void setList(ListEntity list) {
        this.list = list;
    }

    public List<ItemEntity> getItemsList() {
        return itemsList;
    }

    public void setItemsList(List<ItemEntity> itemsList) {
        this.itemsList = itemsList;
    }
}
