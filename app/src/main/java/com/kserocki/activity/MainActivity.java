package com.kserocki.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.kserocki.R;
import com.kserocki.adapter.ListItemsAdapter;
import com.kserocki.repository.List.ListItems;
import com.kserocki.viewmodel.ListItemsViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private ListItemsViewModel listItemsViewModel;
    private ListItemsAdapter listItemsAdapter;
    @BindView(R.id.lists_recycler)
    RecyclerView recyclerView;
    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        listItemsAdapter = new ListItemsAdapter(this);
        recyclerView.setAdapter(listItemsAdapter);

//        String listName = "Grocery shop";
//        List<ItemHelper> items = new ArrayList<>();
//        items.add(new ItemHelper("Apples", false));
//        items.add(new ItemHelper("Peaches", true));
//        items.add(new ItemHelper("Strawberries", false));
        //listItemsViewModel.insertList(listName, false, items);



        listItemsViewModel = new ViewModelProvider(this).get(ListItemsViewModel.class);
        //listItemsViewModel.getAllListItems().observe(this, listItems -> listItemsAdapter.submitList(listItems));

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.menu_current:
                    listItemsViewModel.getArchivedListItems(false).observe(MainActivity.this, listItems -> listItemsAdapter.submitList(listItems));
                    break;
                case R.id.menu_archived:
                    listItemsViewModel.getArchivedListItems(true).observe(MainActivity.this, listItems -> listItemsAdapter.submitList(listItems));
                    break;
            }
            return false;
        });
    }

    @OnClick({R.id.add_new_list_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.add_new_list_btn:
                startActivity(new Intent(this, ListActivity.class));
                break;
        }
    }

    public void deleteList(ListItems listItems) {
        listItemsViewModel.deleteList(listItems);
    }
}
