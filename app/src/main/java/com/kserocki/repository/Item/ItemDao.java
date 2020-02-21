package com.kserocki.repository.Item;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.kserocki.repository.List.ListEntity;
import com.kserocki.repository.ListItems;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public abstract class ItemDao {

    @Query("SELECT * FROM list")
    public abstract List<ListEntity> selectAllCompanies();

    @Transaction
    @Query("SELECT * FROM list ORDER BY created_at DESC")
    public abstract LiveData<List<ListItems>> getListOfItems();

    @Transaction
    public void insertListItems(ListEntity companyEntity, List<ItemEntity> itemEntities) {

        // Save rowId of inserted CompanyEntity as companyId
        final long companyId = insert(companyEntity);

        // Set companyId for all related employeeEntities
        for (ItemEntity itemEntity : itemEntities) {
            itemEntity.setListId(companyId);
            insert(itemEntity);
        }

    }

    @Update
    public abstract void update(ItemEntity itemEntity);

    @Insert(onConflict = REPLACE)
    public abstract long insert(ListEntity list);

    @Insert
    public abstract void insert(ItemEntity itemEntity);

}
