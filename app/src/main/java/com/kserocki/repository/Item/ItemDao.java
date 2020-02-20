package com.kserocki.repository.Item;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.kserocki.model.Item;
import com.kserocki.model.ShoppingList;

import java.util.List;

@Dao
public interface ItemDao {

    @Insert
    void insert(Item item);

    @Update
    void update(Item shoppingList);

    @Delete
    void delete(Item shoppingList);

    @Query("DELETE FROM item")
    void deleteAllShoppingLists();

    @Query("SELECT * FROM item WHERE myListId")
    LiveData<List<Item>> getAllItem();

}
