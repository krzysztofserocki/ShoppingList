package com.kserocki.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

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

    @BindView(R.id.lists_recycler)
    RecyclerView recyclerView;
    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottomNavigationView;
    @BindView(R.id.main_title_txt)
    TextView mainTitleTxt;

    private ListItemsViewModel listItemsViewModel;
    private ListItemsAdapter listItemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setRecyclerView();

        listItemsViewModel = new ViewModelProvider(this).get(ListItemsViewModel.class);

        setBottomNav();
    }

    @SuppressLint("SetTextI18n")
    private void setBottomNav() {
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.menu_current:
                    listItemsViewModel.getArchivedListItems(false).observe(MainActivity.this, listItems -> listItemsAdapter.submitList(listItems));
                    mainTitleTxt.setText(getString(R.string.app_name) + " - " + getString(R.string.current));
                    break;
                case R.id.menu_archived:
                    listItemsViewModel.getArchivedListItems(true).observe(MainActivity.this, listItems -> listItemsAdapter.submitList(listItems));
                    mainTitleTxt.setText(getString(R.string.app_name) + " - " + getString(R.string.archived));
                    break;
            }
            return false;
        });

        bottomNavigationView.setSelectedItemId(R.id.menu_current);
        mainTitleTxt.setText(getString(R.string.app_name) + " - " + getString(R.string.current));
    }

    private void setRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        listItemsAdapter = new ListItemsAdapter(this);
        recyclerView.setAdapter(listItemsAdapter);
    }

    @OnClick({R.id.add_new_list_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.add_new_list_btn:
                startActivity(new Intent(this, ListKtActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
        }
    }

    public void deleteList(ListItems listItems) {
        listItemsViewModel.deleteList(listItems);
    }
}
