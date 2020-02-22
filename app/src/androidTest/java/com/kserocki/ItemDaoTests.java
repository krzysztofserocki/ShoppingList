package com.kserocki;

import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.room.Room;
import androidx.test.platform.app.InstrumentationRegistry;

import com.kserocki.repository.Item.ItemDao;
import com.kserocki.repository.Item.ItemDatabase;
import com.kserocki.repository.Item.ItemEntity;
import com.kserocki.repository.List.ListEntity;
import com.kserocki.repository.List.ListItems;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertNotEquals;

@RunWith(MockitoJUnitRunner.class)
public class ItemDaoTests {

    /**
     * Room calculates the LiveData's value lazily when there is an observer
     * So I use getValue utility method which adds an observer to get the value
     * As you can see it works fine. When there wasn't getValue,
     * I was trying to use Mockito.verify(observer).onChange,
     * but unfortunately it was always null.
     *
     * Hope you will like it ;)
     */

    @Mock
    Observer<List<ListItems>> observer;

    @Rule
    public TestRule rule = new InstantTaskExecutorRule();

    private ItemDatabase database;
    private ItemDao dao;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        Context context = InstrumentationRegistry.getInstrumentation().getContext();
        database = Room.inMemoryDatabaseBuilder(context, ItemDatabase.class)
                .allowMainThreadQueries().build();

        dao = database.itemDao();
    }

    @After
    public void tearDown() {
        database.close();
    }

    @Test
    public void insertDao() throws InterruptedException {
        ListEntity listEntity = new ListEntity("12345", false, "2020-02-22 18:24:20");
        List<ItemEntity> list = new ArrayList<>();
        list.add(new ItemEntity("Test", false));

        LiveData<List<ListItems>> listLiveData = dao.getListOfItems();
        int sizeBeforeInsert = getValue(listLiveData).size();
        dao.insertListItems(listEntity, list);
        int sizeAfterInsert = getValue(listLiveData).size();

        assertNotEquals(sizeBeforeInsert, sizeAfterInsert);
//        Mockito.verify(observer).onChanged(listOfListItems);
    }

    @Test
    public void updateDao() {
        ListEntity listEntity = new ListEntity("12345", false, "2020-02-22 18:24:20");

        long insertedListId = dao.insertListEntity(listEntity);

        int sizeOfChangedLists = dao.updateListNameById("New name", (int) insertedListId);

        assertNotEquals(0, sizeOfChangedLists);
    }

    @Test
    public void deleteDao() {
        ListEntity listEntity = new ListEntity("12345", false, "2020-02-22 18:24:20");

        long insertedListId = dao.insertListEntity(listEntity);
        listEntity.setId(insertedListId);

        int sizeOfChangedLists = dao.deleteListEntity(listEntity);

        assertNotEquals(0, sizeOfChangedLists);
    }

    public static <T> T getValue(LiveData<T> liveData) throws InterruptedException {
        final Object[] objects = new Object[1];
        final CountDownLatch latch = new CountDownLatch(1);

        Observer observer = new Observer() {
            @Override
            public void onChanged(@Nullable Object o) {
                objects[0] = o;
                latch.countDown();
                liveData.removeObserver(this);
            }
        };
        liveData.observeForever(observer);
        latch.await(2, TimeUnit.SECONDS);
        return (T) objects[0];
    }

}
