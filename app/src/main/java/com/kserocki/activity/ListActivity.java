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

import com.google.android.material.switchmaterial.SwitchMaterial;
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
    @BindView(R.id.add_item_btn)
    Button addItemBtn;
    @BindView(R.id.list_name_txt)
    EditText listNameTxt;
    @BindView(R.id.item_name_txt)
    EditText itemNameTxt;
    @BindView(R.id.is_archived_switch)
    SwitchMaterial isArchivedSwitch;
    @BindView(R.id.one_list_recycler)
    RecyclerView recyclerView;
    int listId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_list);
        ButterKnife.bind(this);

        boolean isListArchived = getIntent().hasExtra(EXTRA_LIST_IS_ARCHIVED) ? getIntent().getBooleanExtra(EXTRA_LIST_IS_ARCHIVED, false) : false;


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        oneListAdapter = new OneListAdapter(this, isListArchived);
        recyclerView.setAdapter(oneListAdapter);

        listItemsViewModel = new ListItemsViewModel(getApplication());

        listId = getIntent().hasExtra(EXTRA_LIST_ID) ? getIntent().getIntExtra(EXTRA_LIST_ID, 0) : listItemsViewModel.insertNewList();
        if (getIntent().hasExtra(EXTRA_LIST_NAME))
            listNameTxt.setText(getIntent().getStringExtra(EXTRA_LIST_NAME));

        isArchivedSwitch.setChecked(isListArchived);
        checkIsArchived(isListArchived);

        isArchivedSwitch.setOnCheckedChangeListener((compoundButton, isArchived) -> {
            listItemsViewModel.updateListIsArchivedById(listId, isArchived);
            oneListAdapter.setListArchived(isArchived);
            checkIsArchived(isArchived);
        });

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
                if (listId != 0)
                    if (itemName.length() > 0) {
                        ItemEntity itemEntity = listItemsViewModel.insertItemEntity(itemName, false, listId);
                        if (itemEntity != null) {
                            hideKeyboard();
                            itemNameTxt.setText("");
                        }
                    } else
                        FancyToast.makeText(this, getString(R.string.too_short_item_name), FancyToast.LENGTH_SHORT, FancyToast.CONFUSING, false).show();
                else
                    FancyToast.makeText(this, getString(R.string.need_to_save_list), FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
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

    private void checkIsArchived(boolean isArchived) {
        if (isArchived) {
            itemNameTxt.setEnabled(false);
            listNameTxt.setEnabled(false);
            addItemBtn.setEnabled(false);
            saveListBtn.setEnabled(false);
        } else {
            listNameTxt.setEnabled(true);
            addItemBtn.setEnabled(true);
            itemNameTxt.setEnabled(true);
            saveListBtn.setEnabled(true);
        }
    }


    public static final String EXTRA_LIST_ID = "EXTRA_LIST_ID";
    public static final String EXTRA_LIST_IS_ARCHIVED = "EXTRA_LIST_IS_ARCHIVED";
    public static final String EXTRA_LIST_NAME = "EXTRA_LIST_NAME";

    public void changeStateOfItem(ItemEntity itemEntity, boolean isSelected) {
        listItemsViewModel.changeStateOfItem(itemEntity, isSelected);
    }

    public void deleteItem(ItemEntity itemEntity) {
        listItemsViewModel.deleteItemById(itemEntity);
    }
}
