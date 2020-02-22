package com.kserocki;

import android.content.Context;

import androidx.room.Room;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.kserocki.repository.Item.ItemDao;
import com.kserocki.repository.Item.ItemDatabase;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;


@RunWith(AndroidJUnit4.class)
public class ItemDaoTests {

    private ItemDao itemDao;
    private ItemDatabase db;

    @Before
    public void createDb() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        db = Room.inMemoryDatabaseBuilder(context, ItemDatabase.class).build();
        itemDao = db.itemDao();
    }

    @After
    public void closeDb() {
        db.close();
    }
}