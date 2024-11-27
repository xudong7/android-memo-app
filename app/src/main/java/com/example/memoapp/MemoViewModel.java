package com.example.memoapp;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.memoapp.data.Memo;
import com.example.memoapp.data.MemoDatabase;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MemoViewModel extends AndroidViewModel {

    private final MemoDatabase database;
    private final LiveData<List<Memo>> allMemos;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public MemoViewModel(@NonNull Application application) {
        super(application);
        database = MemoDatabase.getInstance(application);
        allMemos = database.memoDao().getAllMemos();
    }

    // Expose LiveData for observing memos in the UI
    public LiveData<List<Memo>> getAllMemos() {
        return allMemos;
    }

    // Insert operation in background thread
    public void insert(Memo memo) {
        executorService.execute(() -> database.memoDao().insert(memo));
    }

    // Update operation in background thread
    public void update(Memo memo) {
        executorService.execute(() -> database.memoDao().update(memo));
    }

    // Delete operation in background thread
    public void delete(Memo memo) {
        executorService.execute(() -> database.memoDao().delete(memo));
    }
}