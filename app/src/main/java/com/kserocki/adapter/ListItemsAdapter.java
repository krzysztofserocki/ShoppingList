package com.kserocki.adapter;

import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.kserocki.R;
import com.kserocki.activity.ListActivity;
import com.kserocki.activity.ListKtActivity;
import com.kserocki.activity.MainActivity;
import com.kserocki.repository.List.ListItems;
import com.shashank.sony.fancytoastlib.FancyToast;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListItemsAdapter extends ListAdapter<ListItems, ListItemsAdapter.ListHolder> {
    private static final DiffUtil.ItemCallback<ListItems> DIFF_CALLBACK = new DiffUtil.ItemCallback<ListItems>() {
        @Override
        public boolean areItemsTheSame(@NonNull ListItems oldItem, @NonNull ListItems newItem) {
            return oldItem.getList().getId() == newItem.getList().getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull ListItems oldItem, @NonNull ListItems newItem) {
            return oldItem.getList().getCreatedAt().equals(newItem.getList().getCreatedAt()) &&
                    oldItem.getList().getName().equals(newItem.getList().getName()) &&
                    oldItem.getItemsList().size() == newItem.getItemsList().size();
        }
    };
    private MainActivity mainActivity;

    public ListItemsAdapter(MainActivity mainActivity) {
        super(DIFF_CALLBACK);
        this.mainActivity = mainActivity;
    }

    @NonNull
    @Override
    public ListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shopping_lists_item, parent, false);
        return new ListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListHolder holder, int position) {
        ListItems listItems = getItem(position);

        holder.listNameTextView.setText(listItems.getList().getName() + " - [" + listItems.getItemsList().size() + "]");

        if (listItems.getList().isArchived())
            holder.listNameTextView.setPaintFlags(holder.listNameTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        else
            holder.listNameTextView.setPaintFlags(Paint.HINTING_OFF);

        holder.deleteBtn.setOnClickListener(view -> {
            mainActivity.deleteList(listItems);
            FancyToast.makeText(mainActivity, mainActivity.getString(R.string.successfully_deleted_list), FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
        });

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(mainActivity, ListKtActivity.class);
            intent.putExtra(ListActivity.EXTRA_LIST_ID, (int) listItems.getList().getId());
            intent.putExtra(ListActivity.EXTRA_LIST_NAME, listItems.getList().getName());
            intent.putExtra(ListActivity.EXTRA_LIST_IS_ARCHIVED, listItems.getList().isArchived());
            mainActivity.startActivity(intent);
            mainActivity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });

    }

    class ListHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.list_name)
        TextView listNameTextView;
        @BindView(R.id.delete_btn)
        ImageButton deleteBtn;

        ListHolder(@NonNull View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

}
