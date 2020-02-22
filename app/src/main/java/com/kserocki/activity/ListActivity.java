package com.kserocki.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kserocki.R;
import com.kserocki.adapter.OneListAdapter;
import com.kserocki.repository.Item.ItemEntity;
import com.kserocki.viewmodel.ListItemsViewModel;
import com.shashank.sony.fancytoastlib.FancyToast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ListActivity extends AppCompatActivity {

    private ListItemsViewModel listItemsViewModel;
    private OneListAdapter oneListAdapter;

    @BindView(R.id.save_list_btn)
    Button saveListBtn;
    @BindView(R.id.cancel_btn)
    Button cancelBtn;
    @BindView(R.id.list_name_txt)
    EditText listNameTxt;
    @BindView(R.id.item_name_txt)
    EditText itemNameTxt;
    @BindView(R.id.one_list_recycler)
    RecyclerView recyclerView;
    int listId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_list);
        ButterKnife.bind(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        oneListAdapter = new OneListAdapter(this);
        recyclerView.setAdapter(oneListAdapter);

        listItemsViewModel = new ListItemsViewModel(getApplication());

        listId = getIntent().hasExtra(EXTRA_LIST_ID) ? getIntent().getIntExtra(EXTRA_LIST_ID, 0) : listItemsViewModel.insertNewList();
        if (getIntent().hasExtra(EXTRA_LIST_NAME))
            listNameTxt.setText(getIntent().getStringExtra(EXTRA_LIST_NAME));

        listItemsViewModel.getOneListItems(listId).observe(this, listItems -> oneListAdapter.submitList(listItems));
    }

    @OnClick({R.id.cancel_btn, R.id.add_item_btn, R.id.save_list_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cancel_btn:
                finish();
                break;
            case R.id.add_item_btn:
                String itemName = itemNameTxt.getText().toString().trim();
                if (itemName.length() > 0 && listId != 0) {
                    ItemEntity itemEntity = listItemsViewModel.insertItemEntity(itemName, false, listId);
                    if (itemEntity != null) {
                        hideKeyboard();
                        itemNameTxt.setText("");
                    }
                }
                break;
            case R.id.save_list_btn:
                String listName = listNameTxt.getText().toString().trim();
                if (listName.length() > 0)
                    listItemsViewModel.updateListNameById(listName, listId);
                else
                    FancyToast.makeText(this, getString(R.string.empty_list_name_error), FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                break;
        }
    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputMethodManager != null) {
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }


    public static final String EXTRA_LIST_ID = "EXTRA_LIST_ID";
    public static final String EXTRA_LIST_NAME = "EXTRA_LIST_NAME";

    public void changeStateOfItem(ItemEntity itemEntity, boolean isSelected) {
        listItemsViewModel.changeStateOfItem(itemEntity, isSelected);
    }
}
