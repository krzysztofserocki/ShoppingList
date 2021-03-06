package com.kserocki.activity

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.switchmaterial.SwitchMaterial
import com.kserocki.R
import com.kserocki.adapter.OneListAdapter
import com.kserocki.repository.Item.ItemEntity
import com.kserocki.viewmodel.ListItemsViewModel
import com.shashank.sony.fancytoastlib.FancyToast


class ListKtActivity : Activity(), LifecycleOwner {

    private lateinit var lifecycleRegistry: LifecycleRegistry

    override fun getLifecycle(): Lifecycle {
        return lifecycleRegistry
    }

    private lateinit var listItemsViewModel: ListItemsViewModel
    private lateinit var oneListAdapter: OneListAdapter

    private var listId = 0

    private lateinit var oneListRecycler: RecyclerView
    private lateinit var listNameTxt: EditText
    private lateinit var itemNameTxt: EditText
    private lateinit var isArchivedSwitch: SwitchMaterial
    private lateinit var addItemBtn: Button
    private lateinit var saveListBtn: Button
    private lateinit var cancelBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_list)

        /**
         * SETTING UP COMPONENTS
         */

        oneListRecycler = findViewById(R.id.one_list_recycler)
        listNameTxt = findViewById(R.id.list_name_txt)
        itemNameTxt = findViewById(R.id.item_name_txt)
        isArchivedSwitch = findViewById(R.id.is_archived_switch)
        addItemBtn = findViewById(R.id.add_item_btn)
        saveListBtn = findViewById(R.id.save_list_btn)
        cancelBtn = findViewById(R.id.cancel_btn)
        lifecycleRegistry = LifecycleRegistry(this)

        val isListArchived =
                if (intent.hasExtra(EXTRA_LIST_IS_ARCHIVED))
                    intent.getBooleanExtra(EXTRA_LIST_IS_ARCHIVED, false)
                else
                    false

        /**
         * SETTING UP RECYCLER AND ADAPTER
         */

        oneListRecycler.layoutManager = LinearLayoutManager(this)
        oneListRecycler.setHasFixedSize(true)
        oneListAdapter = OneListAdapter(this, isListArchived)
        oneListRecycler.adapter = oneListAdapter

        listItemsViewModel = ListItemsViewModel(application)


        /**
         * If there is not listId from intent, it creates new List with random name and return id of it.
         */

        listId = if (intent.hasExtra(EXTRA_LIST_ID)) intent.getIntExtra(EXTRA_LIST_ID, 0) else listItemsViewModel.insertNewList()
        if (intent.hasExtra(EXTRA_LIST_NAME))
            listNameTxt.setText(intent.getStringExtra(EXTRA_LIST_NAME))


        /**
         * SETTING UP SWITCH
         */

        isArchivedSwitch.isChecked = isListArchived
        checkIsArchived(isListArchived)

        isArchivedSwitch.setOnCheckedChangeListener { _, isArchived ->
            listItemsViewModel.updateListIsArchivedById(listId, isArchived)
            oneListAdapter.setListArchived(isArchived)
            checkIsArchived(isArchived)
        }



        listItemsViewModel.getOneListItems(listId).observe(this, Observer { itemEntities -> oneListAdapter.submitList(itemEntities) })

        /**
         * SETTING UP BUTTONS
         */

        cancelBtn.setOnClickListener { finish() }
        addItemBtn.setOnClickListener {
            val itemName = itemNameTxt.text.toString().trim { it <= ' ' }
            if (listId != 0)
                if (itemName.isNotEmpty()) {
                    val itemEntity = listItemsViewModel.insertItemEntity(itemName, false, listId)
                    if (itemEntity != null) {
                        hideKeyboard()
                        itemNameTxt.setText("")
                    }
                } else
                    FancyToast.makeText(this, getString(R.string.too_short_item_name), FancyToast.LENGTH_SHORT, FancyToast.CONFUSING, false).show()
            else
                FancyToast.makeText(this, getString(R.string.need_to_save_list), FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show()
        }
        saveListBtn.setOnClickListener {
            val listName = listNameTxt.text.toString().trim { it <= ' ' }
            if (listName.isNotEmpty()) {
                hideKeyboard()
                listItemsViewModel.updateListNameById(listName, listId)
                FancyToast.makeText(this, getString(R.string.successfully_changed_name_of_list), FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show()
            } else {
                FancyToast.makeText(this, getString(R.string.empty_list_name_error), FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show()
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    private fun hideKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private fun checkIsArchived(isArchived: Boolean) {
        if (isArchived) {
            itemNameTxt.isEnabled = false
            listNameTxt.isEnabled = false
            addItemBtn.isEnabled = false
            saveListBtn.isEnabled = false
        } else {
            listNameTxt.isEnabled = true
            addItemBtn.isEnabled = true
            itemNameTxt.isEnabled = true
            saveListBtn.isEnabled = true
        }
    }


    private val EXTRA_LIST_ID = "EXTRA_LIST_ID"
    private val EXTRA_LIST_IS_ARCHIVED = "EXTRA_LIST_IS_ARCHIVED"
    private val EXTRA_LIST_NAME = "EXTRA_LIST_NAME"

    fun changeStateOfItem(itemEntity: ItemEntity, isSelected: Boolean) {
        listItemsViewModel.changeStateOfItem(itemEntity, isSelected)
    }

    fun deleteItem(itemEntity: ItemEntity) {
        listItemsViewModel.deleteItemById(itemEntity)
    }

}