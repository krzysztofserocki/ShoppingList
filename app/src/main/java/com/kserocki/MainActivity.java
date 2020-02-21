package com.kserocki;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.kserocki.repository.Item.ItemEntity;
import com.kserocki.repository.ListItems;
import com.kserocki.viewmodel.ListItemsViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListItemsViewModel listItemsViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String listName = "Grocery shop";
        List<String> names = new ArrayList<>();
        names.add("Apples");
        names.add("Peaches");
        names.add("Strawberries");

        listItemsViewModel = ViewModelProviders.of(this).get(ListItemsViewModel.class);

        listItemsViewModel.insert(listName, false, names);

        listItemsViewModel.getAllListItems().observe(this, this::sout);

    }

    private void sout(List<ListItems> listItems) {
        for (ListItems listItems1 : listItems) {
            for (ItemEntity itemEntity : listItems1.itemsList)
                System.out.println(itemEntity.getName());
        }
    }
}
