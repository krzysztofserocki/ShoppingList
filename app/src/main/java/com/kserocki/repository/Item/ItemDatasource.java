package com.kserocki.repository.Item;

import androidx.annotation.NonNull;

import java.util.List;

public interface ItemDatasource {

    interface LoadItemsCallback {

        void onItemsLoaded(List<String> items);

        void onNoListsFound();

    }

    void saveItems(@NonNull String name, @NonNull List<String> itemNames);

    void getItems(@NonNull ItemDatasource.LoadItemsCallback callback);

}
