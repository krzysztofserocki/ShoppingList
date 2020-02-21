package com.kserocki.repository.Item;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.kserocki.repository.List.ListEntity;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "item_list",
        foreignKeys = @ForeignKey(
                entity = ListEntity.class,
                parentColumns = "id",
                childColumns = "list_id",
                onDelete = CASCADE),
        indices = @Index("list_id"))
public class ItemEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo(name = "list_id")
    private long listId;
    @NonNull
    @ColumnInfo(name = "name")
    private final String name;

    public ItemEntity(@NonNull String name) {
        this.name = name;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public long getListId() {
        return listId;
    }

    public void setListId(long listId) {
        this.listId = listId;
    }
}
