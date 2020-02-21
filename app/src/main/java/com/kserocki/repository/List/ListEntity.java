package com.kserocki.repository.List;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

@Entity(tableName = "list")
public class ListEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @NonNull
    @ColumnInfo(name = "created_at")
    private final String createdAt;

    @NonNull
    @ColumnInfo(name = "name")
    private final String name;

    @NonNull
    @ColumnInfo(name = "is_archived")
    private final boolean isArchived;

    public ListEntity(@NotNull String name, @NotNull boolean isArchived, @NotNull String createdAt) {
        this.name = name;
        this.isArchived = isArchived;
        this.createdAt = createdAt;
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
