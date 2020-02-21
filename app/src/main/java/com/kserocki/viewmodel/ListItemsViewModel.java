package com.kserocki.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.kserocki.model.ItemHelper;
import com.kserocki.repository.Item.ItemEntity;
import com.kserocki.repository.Item.ItemRepository;
import com.kserocki.repository.List.ListEntity;
import com.kserocki.repository.List.ListItems;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class ListItemsViewModel extends AndroidViewModel {
    private ItemRepository itemRepository;
    private MutableLiveData<ListEntity> mutableLiveData = new MutableLiveData<>();
    private LiveData<List<ListItems>> allListItems;

    public ListItemsViewModel(@NonNull Application application) {
        super(application);
        itemRepository = ItemRepository.getInstance(application);
        allListItems = itemRepository.getAllListItemsOrderedByDate();
    }

    public void insertList(String listName, boolean isArchived, List<ItemHelper> items) {
        itemRepository.insert(listName, isArchived, items);
    }

    public void updateListItems(long listId, List<ItemEntity> itemEntity) {
        itemRepository.updateListItems(listId, itemEntity);
    }

//    public void updateItemEntity(ItemEntity car) {
//        itemRepository.updateItemEntity(car);
//    }

//    public void delete(Car car) {
//        itemRepository.delete(car);
//    }
//
//    public void deleteAllCars() {
//        itemRepository.deleteAllCars();
//    }

    public LiveData<List<ListItems>> getAllListItems() {
        return allListItems;
    }

    public LiveData<List<ListItems>> getArchivedListItems(boolean isArchived) {
        return itemRepository.getListByIsArchived(isArchived);
    }

    public LiveData<List<ItemEntity>> getOneListItems(int listId) {
        return itemRepository.getOneListItems(listId);
    }

    public void changeStateOfItem(ItemEntity itemEntity, boolean isSelected) {
        itemRepository.changeStateOfItem(itemEntity, isSelected);
    }

    public ItemEntity insertItemEntity(String itemName, boolean isSelected, int listId) {
        try {
            return itemRepository.insertItemEntity(itemName, isSelected, listId);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int insertNewList() {
        try {
            return itemRepository.insertNewList();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void updateListNameById(String listName, int listId) {
        itemRepository.updateListNameById(listName, listId);
    }
}
