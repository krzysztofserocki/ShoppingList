package com.kserocki.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.kserocki.repository.Item.ItemEntity;
import com.kserocki.repository.Item.ItemRepository;
import com.kserocki.repository.List.ListItems;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class ListItemsViewModel extends AndroidViewModel {
    private ItemRepository itemRepository;


    public ListItemsViewModel(@NonNull Application application) {
        super(application);
        itemRepository = ItemRepository.getInstance(application);
    }

    // ========================
    // =====    GETTERS   =====
    // ========================

    public LiveData<List<ListItems>> getArchivedListItems(boolean isArchived) {
        return itemRepository.getListByIsArchived(isArchived);
    }

    public LiveData<List<ItemEntity>> getOneListItems(int listId) {
        return itemRepository.getOneListItems(listId);
    }

    // ========================
    // =====    UPDATE    =====
    // ========================

    public void changeStateOfItem(ItemEntity itemEntity, boolean isSelected) {
        itemRepository.changeStateOfItem(itemEntity, isSelected);
    }

    public void updateListNameById(String listName, int listId) {
        itemRepository.updateListNameById(listName, listId);
    }

    public void updateListIsArchivedById(int listId, boolean isArchived) {
        itemRepository.updateListIsArchivedById(listId, isArchived);
    }


    // ========================
    // =====    INSERT    =====
    // ========================

    public ItemEntity insertItemEntity(String itemName, boolean isSelected, int listId) {
        try {
            return itemRepository.insertItemEntity(itemName, isSelected, listId);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int insertNewList() {
        try {
            return itemRepository.insertNewList();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // ========================
    // =====    DELETE    =====
    // ========================

    public void deleteList(ListItems listItems) {
        itemRepository.deleteListItems(listItems);
    }

    public void deleteItemById(ItemEntity itemEntity) {
        itemRepository.deleteItemById(itemEntity);
    }

}
