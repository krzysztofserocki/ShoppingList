package com.kserocki.repository.Item;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.kserocki.repository.List.ListEntity;
import com.kserocki.repository.ListItems;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ItemRepository {

    private static volatile ItemRepository INSTANCE;
    private ItemDao itemDao;
    private LiveData<List<ListItems>> allListItems;

    private ItemRepository(@NonNull Context context) {
        itemDao = ItemDatabase.getInstance(context).itemDao();
        allListItems = itemDao.getListOfItems();
    }

    public static ItemRepository getInstance(@NonNull Context context) {
        if (INSTANCE == null) {
            synchronized (ItemRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ItemRepository(context.getApplicationContext());
                }
            }
        }
        return INSTANCE;
    }

    @SuppressWarnings("unchecked")
    public void insert(String listName, boolean isArchived, List<String> itemNames) {
        new InsertListItemsAsyncTask(listName, isArchived, itemDao).execute(itemNames);
    }

    @SuppressWarnings("unchecked")
    public void updateItem(ItemEntity itemEntity) {
        new UpdateItemAsyncTask(itemDao).execute(itemEntity);
    }

    public LiveData<List<ListItems>> getAllListItemsOrderedByDate() {
        return allListItems;
    }

    private static class InsertListItemsAsyncTask extends AsyncTask<List<String>, Void, Void> {
        private ItemDao itemDao;
        private String listName;
        private boolean isArchived;

        private InsertListItemsAsyncTask(String listName, boolean isArchived, ItemDao itemDao) {
            this.itemDao = itemDao;
            this.isArchived = isArchived;
            this.listName = listName;
        }
        @Override
        protected Void doInBackground(List<String>... strings) {
            List<ItemEntity> itemEntities = new ArrayList<>(strings[0].size());
            for (String name : strings[0])
                itemEntities.add(new ItemEntity(name));

            itemDao.insertListItems(new ListEntity(listName, isArchived, getCurrentDate()), itemEntities);
            return null;
        }
    }

    private static class UpdateItemAsyncTask extends AsyncTask<ItemEntity, Void, Void> {
        private ItemDao itemDao;

        private UpdateItemAsyncTask(ItemDao itemDao) {
            this.itemDao = itemDao;
        }
        @Override
        protected Void doInBackground(ItemEntity... entities) {
            itemDao.update(entities[0]);
            return null;
        }
    }

    private static String getCurrentDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(new Date());
    }

}
