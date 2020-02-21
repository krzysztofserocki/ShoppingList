package com.kserocki.repository.Item;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.kserocki.model.ItemHelper;
import com.kserocki.repository.List.ListEntity;
import com.kserocki.repository.List.ListItems;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;

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
    public void insert(String listName, boolean isArchived, List<ItemHelper> items) {
        new InsertListItemsAsyncTask(listName, isArchived, itemDao).execute(items);
    }

    @SuppressWarnings("unchecked")
    public void updateListItems(long listId, List<ItemEntity> itemEntities) {
        new UpdateListItemsAsyncTask(listId, itemDao).execute(itemEntities);
    }

    public LiveData<List<ListItems>> getAllListItemsOrderedByDate() {
        return allListItems;
    }

    public LiveData<List<ListItems>> getListByIsArchived(boolean isArchived) {
        return itemDao.getListByIsArchived(isArchived);
    }

    public LiveData<List<ItemEntity>> getOneListItems(int listId) {
        return itemDao.selectItemEntityByListId(listId);
    }

    public ItemEntity insertItemEntity(String itemName, boolean isSelected, long listId) throws ExecutionException, InterruptedException {
        return new InsertItemEntityAsyncTask(itemName, isSelected, listId, itemDao).execute().get();
    }

    public int insertNewList() throws ExecutionException, InterruptedException {
        return new InsertNewListAsyncTask(itemDao).execute().get().intValue();
    }

    public void updateListNameById(String listName, int listId) {
        new UpdateListNameByIdAsyncTask(listName, listId, itemDao).execute();
    }

    private static class UpdateListNameByIdAsyncTask extends AsyncTask<Void, Void, Void> {
        private ItemDao itemDao;
        private String listName;
        private int listId;

        private UpdateListNameByIdAsyncTask(String listName, int listId, ItemDao itemDao) {
            this.itemDao = itemDao;
            this.listId = listId;
            this.listName = listName;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            itemDao.updateListNameById(listName, listId);
            return null;
        }
    }

    private static class InsertNewListAsyncTask extends AsyncTask<Void, Void, Long> {
        private ItemDao itemDao;

        private InsertNewListAsyncTask(ItemDao itemDao) {
            this.itemDao = itemDao;
        }

        @Override
        protected Long doInBackground(Void... voids) {
            ListEntity listEntity = new ListEntity("list_" + getRandomNumberInRange(1000, 9999), false, getCurrentDate());
            return itemDao.insert(listEntity);
        }
    }

    private static int getRandomNumberInRange(int min, int max) {
        return new Random().nextInt((max - min) + 1) + min;
    }

    private static class InsertItemEntityAsyncTask extends AsyncTask<Void, Void, ItemEntity> {
        private ItemDao itemDao;
        private String itemName;
        private boolean isSelected;
        private long listId;

        private InsertItemEntityAsyncTask(String itemName, boolean isSelected, long listId, ItemDao itemDao) {
            this.itemDao = itemDao;
            this.isSelected = isSelected;
            this.itemName = itemName;
            this.listId = listId;
        }

        @Override
        protected ItemEntity doInBackground(Void... voids) {
            ItemEntity itemEntity = new ItemEntity(itemName, isSelected);

            itemEntity.setListId(listId);
            long itemId = itemDao.insertItemEntity(itemEntity);
            itemEntity.setId(itemId);

            return itemEntity;
        }
    }

    private static class UpdateListItemsAsyncTask extends AsyncTask<List<ItemEntity>, Void, Void> {
        private ItemDao itemDao;
        private long listId;

        private UpdateListItemsAsyncTask(long listId, ItemDao itemDao) {
            this.listId = listId;
            this.itemDao = itemDao;
        }

        @Override
        protected Void doInBackground(List<ItemEntity>... itemEntities) {
            List<ItemEntity> itemEntity = itemEntities[0];
            itemDao.updateListItems(listId, itemEntity);
            return null;
        }
    }

    private static class InsertListItemsAsyncTask extends AsyncTask<List<ItemHelper>, Void, Void> {
        private ItemDao itemDao;
        private String listName;
        private boolean isArchived;

        private InsertListItemsAsyncTask(String listName, boolean isArchived, ItemDao itemDao) {
            this.itemDao = itemDao;
            this.isArchived = isArchived;
            this.listName = listName;
        }

        @Override
        protected Void doInBackground(List<ItemHelper>... items) {
            List<ItemEntity> itemEntities = new ArrayList<>();
            for (ItemHelper item : items[0])
                itemEntities.add(new ItemEntity(item.getName(), item.isSelected()));

            itemDao.insertListItems(new ListEntity(listName, isArchived, getCurrentDate()), itemEntities);
            return null;
        }
    }

    public void changeStateOfItem(ItemEntity itemEntity, boolean isSelected) {
        new ChangeStateOfItemAsyncTask(isSelected, itemDao).execute(itemEntity);
    }

    private static class ChangeStateOfItemAsyncTask extends AsyncTask<ItemEntity, Void, Void> {
        private ItemDao itemDao;
        private boolean isSelected;

        private ChangeStateOfItemAsyncTask(boolean isSelected, ItemDao itemDao) {
            this.itemDao = itemDao;
            this.isSelected = isSelected;
        }

        @Override
        protected Void doInBackground(ItemEntity... items) {
            ItemEntity itemEntity = items[0];

            itemDao.changeStateOfItem(itemEntity.getId(), isSelected);
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
            itemDao.updateItemEntity(entities[0]);
            return null;
        }
    }

    private static String getCurrentDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(new Date());
    }

}
