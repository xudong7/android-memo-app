package com.example.memoapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.memoapp.adapter.MemoAdapter;
import com.example.memoapp.data.Memo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int ADD_MEMO_REQUEST = 1;
    private static final int EDIT_MEMO_REQUEST = 2;

    private MemoViewModel memoViewModel;
    private MemoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        adapter = new MemoAdapter();
        recyclerView.setAdapter(adapter);

        // Initialize ViewModel
        memoViewModel = new ViewModelProvider(this).get(MemoViewModel.class);
        memoViewModel.getAllMemos().observe(this, new Observer<List<Memo>>() {
            @Override
            public void onChanged(@Nullable List<Memo> memos) {
                adapter.submitList(memos);
            }
        });

        // Floating action button to add a new memo
        FloatingActionButton fabAdd = findViewById(R.id.fab_add);
        fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddEditMemoActivity.class);
            startActivityForResult(intent, ADD_MEMO_REQUEST);
        });

        // Handle click on memo items to edit
        adapter.setOnItemClickListener(memo -> {
            Intent intent = new Intent(MainActivity.this, AddEditMemoActivity.class);
            intent.putExtra(AddEditMemoActivity.EXTRA_ID, memo.getId());
            intent.putExtra(AddEditMemoActivity.EXTRA_TITLE, memo.getTitle());
            intent.putExtra(AddEditMemoActivity.EXTRA_CONTENT, memo.getContent());
            intent.putExtra(AddEditMemoActivity.EXTRA_IMAGE_PATH, memo.getImagePath());
            startActivityForResult(intent, EDIT_MEMO_REQUEST);
        });

        // Handle delete button click
        adapter.setOnDeleteClickListener(memo -> memoViewModel.delete(memo));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            String title = data.getStringExtra(AddEditMemoActivity.EXTRA_TITLE);
            String content = data.getStringExtra(AddEditMemoActivity.EXTRA_CONTENT);
            String imagePath = data.getStringExtra(AddEditMemoActivity.EXTRA_IMAGE_PATH);

            if (requestCode == ADD_MEMO_REQUEST) {
                Memo memo = new Memo();
                memo.setTitle(title);
                memo.setContent(content);
                memo.setImagePath(imagePath);

                // Insert new memo into database via ViewModel
                memoViewModel.insert(memo);
            } else if (requestCode == EDIT_MEMO_REQUEST) {
                int id = data.getIntExtra(AddEditMemoActivity.EXTRA_ID, -1);
                if (id == -1) {
                    return;
                }

                Memo memo = new Memo();
                memo.setId(id);
                memo.setTitle(title);
                memo.setContent(content);
                memo.setImagePath(imagePath);

                // Update existing memo in database via ViewModel
                memoViewModel.update(memo);
            }
        }
    }
}