package com.kserocki.repository.List;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

@Entity(tableName = "list")
public class ListEntity {

    @NonNull
    @ColumnInfo(name = "created_at")
    private final String createdAt;
    @NonNull
    @ColumnInfo(name = "name")
    private final String name;
    @NonNull
    @ColumnInfo(name = "is_archived")
    private final boolean isArchived;
    @PrimaryKey(autoGenerate = true)
    public long id;

    public ListEntity(@NotNull String name, @NotNull boolean isArchived, @NotNull String createdAt) {
        this.name = name;
        this.isArchived = isArchived;
        this.createdAt = createdAt;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isArchived() {
        return isArchived;
    }

    @NonNull
    public String getName() {
        return name;
    }

    @NonNull
    public String getCreatedAt() {
        return createdAt;
    }

}
