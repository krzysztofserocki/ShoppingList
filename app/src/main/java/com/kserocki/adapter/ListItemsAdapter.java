package com.kserocki.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.kserocki.R;
import com.kserocki.activity.ListActivity;
import com.kserocki.activity.MainActivity;
import com.kserocki.repository.List.ListItems;
import com.shashank.sony.fancytoastlib.FancyToast;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListItemsAdapter extends ListAdapter<ListItems, ListItemsAdapter.ListHolder> {
    private MainActivity mainActivity;

    public ListItemsAdapter(MainActivity mainActivity) {
        super(DIFF_CALLBACK);
        this.mainActivity = mainActivity;
    }

    private static final DiffUtil.ItemCallback<ListItems> DIFF_CALLBACK = new DiffUtil.ItemCallback<ListItems>() {
        @Override
        public boolean areItemsTheSame(@NonNull ListItems oldItem, @NonNull ListItems newItem) {
            return oldItem.list.getId() == newItem.list.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull ListItems oldItem, @NonNull ListItems newItem) {
            return oldItem.list.getCreatedAt().equals(newItem.list.getCreatedAt()) &&
                    oldItem.list.getName().equals(newItem.list.getName()) &&
                    oldItem.itemsList.size() == newItem.itemsList.size();
        }
    };


    @NonNull
    @Override
    public ListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shopping_lists_item, parent, false);
        return new ListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListHolder holder, int position) {
        ListItems listItems = getItem(position);

        holder.listNameTextView.setText(listItems.list.getName() + " - [" + listItems.itemsList.size() + "]");
        holder.isArchivedCheckBox.setChecked(listItems.list.isArchived());

        holder.isArchivedCheckBox.setOnCheckedChangeListener((compoundButton, b) ->
                FancyToast.makeText(mainActivity, b + "", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show()
        );

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(mainActivity, ListActivity.class);
            intent.putExtra(ListActivity.EXTRA_LIST_ID, listItems.list.getId());
            intent.putExtra(ListActivity.EXTRA_LIST_NAME, listItems.list.getName());
            mainActivity.startActivity(intent);
        });

    }

    public ListItems getListAt(int position) {
        return getItem(position);
    }

    class ListHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.list_name)
        TextView listNameTextView;
        @BindView(R.id.is_archived)
        CheckBox isArchivedCheckBox;

        ListHolder(@NonNull View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

}
