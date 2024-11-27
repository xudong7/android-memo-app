package com.example.memoapp.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.memoapp.R;
import com.example.memoapp.data.Memo;

import java.io.File;

public class MemoAdapter extends ListAdapter<Memo, MemoAdapter.MemoHolder> {

    private OnItemClickListener itemClickListener;
    private OnDeleteClickListener deleteClickListener;

    public MemoAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Memo> DIFF_CALLBACK = new DiffUtil.ItemCallback<Memo>() {
        @Override
        public boolean areItemsTheSame(@NonNull Memo oldItem, @NonNull Memo newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Memo oldItem, @NonNull Memo newItem) {
            return oldItem.getTitle().equals(newItem.getTitle()) &&
                    oldItem.getContent().equals(newItem.getContent()) &&
                    oldItem.getImagePath().equals(newItem.getImagePath());
        }
    };

    @NonNull
    @Override
    public MemoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.memo_item, parent, false);
        return new MemoHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MemoHolder holder, int position) {
        Memo currentMemo = getItem(position);
        holder.textViewTitle.setText(currentMemo.getTitle());
        holder.textViewContent.setText(currentMemo.getContent());

        // Load image if available
        if (currentMemo.getImagePath() != null) {
            File imgFile = new File(currentMemo.getImagePath());
            if (imgFile.exists()) {
                Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                holder.imageView.setImageBitmap(bitmap);
            }
        } else {
            holder.imageView.setImageResource(R.drawable.ic_placeholder);
        }

        // Handle delete button click
        holder.btnDelete.setOnClickListener(v -> {
            if (deleteClickListener != null) {
                deleteClickListener.onDeleteClick(currentMemo);
            }
        });

        // Handle item click for editing
        holder.itemView.setOnClickListener(v -> {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(currentMemo);
            }
        });
    }

    class MemoHolder extends RecyclerView.ViewHolder {
        private TextView textViewTitle;
        private TextView textViewContent;
        private ImageView imageView;
        private Button btnDelete;

        public MemoHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.text_view_title);
            textViewContent = itemView.findViewById(R.id.text_view_content);
            imageView = itemView.findViewById(R.id.image_view);
            btnDelete = itemView.findViewById(R.id.btn_delete);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Memo memo);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(Memo memo);
    }

    public void setOnDeleteClickListener(OnDeleteClickListener listener) {
        this.deleteClickListener = listener;
    }
}