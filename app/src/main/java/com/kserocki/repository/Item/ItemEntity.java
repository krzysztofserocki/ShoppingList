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
    public long id;
    @ColumnInfo(name = "list_id")
    private long listId;
    @ColumnInfo(name = "is_selected")
    private boolean isSelected;
    @NonNull
    @ColumnInfo(name = "name")
    private final String name;

    public ItemEntity(@NonNull String name, @NonNull boolean isSelected) {
        this.name = name;
        this.isSelected = isSelected;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isSelected() {
        return isSelected;
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
