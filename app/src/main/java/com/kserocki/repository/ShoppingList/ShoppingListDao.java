package com.kserocki.repository.ShoppingList;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.kserocki.model.ShoppingList;

import java.util.List;

@Dao
public interface ShoppingListDao {

    @Insert
    void insert(ShoppingList shoppingList);

    @Update
    void update(ShoppingList shoppingList);

    @Delete
    void delete(ShoppingList shoppingList);

    @Query("DELETE FROM shopping_list")
    void deleteAllShoppingLists();

    @Query("SELECT * FROM shopping_list ORDER BY createdAt DESC")
    LiveData<List<ShoppingList>> getAllCarsSortedByDate();

}
