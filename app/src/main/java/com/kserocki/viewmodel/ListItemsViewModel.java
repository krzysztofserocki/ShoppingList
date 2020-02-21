package com.kserocki.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.kserocki.repository.Item.ItemRepository;
import com.kserocki.repository.ListItems;

import java.util.List;

public class ListItemsViewModel extends AndroidViewModel {
    private ItemRepository itemRepository;
    private LiveData<List<ListItems>> allListItems;

    public ListItemsViewModel(@NonNull Application application) {
        super(application);
        itemRepository = ItemRepository.getInstance(application);
        allListItems = itemRepository.getAllListItemsOrderedByDate();
    }

    public void insert(String listName, boolean isArchived, List<String> itemNames) {
        itemRepository.insert(listName, isArchived, itemNames);
    }

//    public void update(ItemEntity car) {
//        itemRepository.update(car);
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
}
