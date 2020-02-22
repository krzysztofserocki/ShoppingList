package com.kserocki.adapter;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.kserocki.R;
import com.kserocki.activity.ListActivity;
import com.kserocki.repository.Item.ItemEntity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OneListAdapter extends ListAdapter<ItemEntity, OneListAdapter.ListHolder> {

    private boolean isListArchived;

    private static final DiffUtil.ItemCallback<ItemEntity> DIFF_CALLBACK = new DiffUtil.ItemCallback<ItemEntity>() {
        @Override
        public boolean areItemsTheSame(@NonNull ItemEntity oldItem, @NonNull ItemEntity newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull ItemEntity oldItem, @NonNull ItemEntity newItem) {
            return oldItem.getName().equals(newItem.getName()) &&
                    oldItem.getListId() == newItem.getListId();
        }
    };
    private ListActivity listActivity;

    public OneListAdapter(ListActivity listActivity, boolean isListArchived) {
        super(DIFF_CALLBACK);
        this.listActivity = listActivity;
        this.isListArchived = isListArchived;
    }

    @NonNull
    @Override
    public OneListAdapter.ListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_item, parent, false);
        return new OneListAdapter.ListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OneListAdapter.ListHolder holder, int position) {
        ItemEntity itemEntity = getItem(position);

        holder.itemCheckbox.setText(itemEntity.getName());
        holder.itemCheckbox.setChecked(itemEntity.isSelected());
        if (itemEntity.isSelected())
            holder.itemCheckbox.setPaintFlags(holder.itemCheckbox.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        else
            holder.itemCheckbox.setPaintFlags(Paint.HINTING_OFF);

        holder.itemCheckbox.setOnCheckedChangeListener((compoundButton, b) -> {
            listActivity.changeStateOfItem(itemEntity, b);
            if (b)
                holder.itemCheckbox.setPaintFlags(holder.itemCheckbox.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            else
                holder.itemCheckbox.setPaintFlags(Paint.HINTING_OFF);
        });

        holder.deleteItemBtn.setOnClickListener(view -> listActivity.deleteItem(itemEntity));

        if (isListArchived) {
            holder.deleteItemBtn.setEnabled(false);
            holder.itemCheckbox.setEnabled(false);
        } else {
            holder.deleteItemBtn.setEnabled(true);
            holder.itemCheckbox.setEnabled(true);
        }

    }

    class ListHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_checkbox)
        CheckBox itemCheckbox;
        @BindView(R.id.delete_item_btn)
        ImageButton deleteItemBtn;

        ListHolder(@NonNull View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public void setListArchived(boolean listArchived) {
        isListArchived = listArchived;
        notifyDataSetChanged();
    }
}
