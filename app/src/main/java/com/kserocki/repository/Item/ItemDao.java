package com.kserocki.repository.Item;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.kserocki.repository.List.ListEntity;
import com.kserocki.repository.List.ListItems;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public abstract class ItemDao {

    @Query("SELECT * FROM item_list WHERE list_id = :listId")
    public abstract LiveData<List<ItemEntity>> getOneListItems(int listId);

    @Transaction
    @Query("SELECT * FROM list ORDER BY created_at DESC")
    public abstract LiveData<List<ListItems>> getListOfItems();

    @Transaction
    public void insertListItems(ListEntity listEntity, List<ItemEntity> itemEntities) {
        final long companyId = insertListEntity(listEntity);

        for (ItemEntity itemEntity : itemEntities) {
            itemEntity.setListId(companyId);
            insertItemEntity(itemEntity);
        }
    }

    @Transaction
    public void updateListItems(long listId, List<ItemEntity> itemEntities) {
        deleteAllItemsByListId(listId);
        for (ItemEntity itemEntity : itemEntities) {
            itemEntity.setListId(listId);
            insertItemEntity(itemEntity);
        }
    }

    @Query("DELETE FROM item_list WHERE list_id = :listId")
    public abstract void deleteAllItemsByListId(long listId);

    @Insert(onConflict = REPLACE)
    public abstract Long insertListEntity(ListEntity list);


    @Transaction
    @Query("SELECT * FROM list WHERE list.is_archived = :isArchived ORDER BY created_at DESC")
    public abstract LiveData<List<ListItems>> getListByIsArchived(boolean isArchived);

    @Query("UPDATE item_list SET is_selected = :isSelected WHERE id = :itemEntityId")
    public abstract void changeStateOfItem(long itemEntityId, boolean isSelected);

    @Insert
    public abstract Long insertItemEntity(ItemEntity itemEntity);

    @Query("UPDATE list SET name = :listName WHERE id = :listId")
    public abstract void updateListNameById(String listName, int listId);

    public void deleteListItems(ListItems listItems) {
        for (ItemEntity itemEntity : listItems.getItemsList()) {
            deleteItemEntity(itemEntity);
        }
        deleteListEntity(listItems.getList());
    }

    @Delete
    public abstract void deleteListEntity(ListEntity listEntity);

    @Delete
    public abstract void deleteItemEntity(ItemEntity itemEntity);

    @Query("UPDATE list SET is_archived = :isArchived WHERE id = :listId")
    public abstract void updateListIsArchivedById(int listId, boolean isArchived);

    @Query("SELECT * FROM item_list WHERE id = :itemId")
    public abstract LiveData<ItemEntity> getOneItemEntity(Long itemId);

    @Query("SELECT * FROM item_list")
    public abstract LiveData<List<ItemEntity>> getAllItemEntities();
}
