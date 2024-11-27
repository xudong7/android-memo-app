package com.example.memoapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.memoapp.utils.ImageUtils;

import java.io.IOException;

public class AddEditMemoActivity extends AppCompatActivity {

    public static final String EXTRA_ID = "com.example.memoapp.EXTRA_ID";
    public static final String EXTRA_TITLE = "com.example.memoapp.EXTRA_TITLE";
    public static final String EXTRA_CONTENT = "com.example.memoapp.EXTRA_CONTENT";
    public static final String EXTRA_IMAGE_PATH = "com.example.memoapp.EXTRA_IMAGE_PATH";

    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText editTextTitle;
    private EditText editTextContent;
    private ImageView imageView;
    private String imagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_memo);

        editTextTitle = findViewById(R.id.edit_text_title);
        editTextContent = findViewById(R.id.edit_text_content);
        imageView = findViewById(R.id.image_view);
        Button buttonChooseImage = findViewById(R.id.btn_choose_image);
        Button buttonSave = findViewById(R.id.btn_save);

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ID)) {
            setTitle("Edit Memo");
            editTextTitle.setText(intent.getStringExtra(EXTRA_TITLE));
            editTextContent.setText(intent.getStringExtra(EXTRA_CONTENT));
            imagePath = intent.getStringExtra(EXTRA_IMAGE_PATH);

            if (imagePath != null) {
                Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
                imageView.setImageBitmap(bitmap);
            }
        } else {
            setTitle("Add Memo");
        }

        buttonChooseImage.setOnClickListener(v -> openImagePicker());
        buttonSave.setOnClickListener(v -> saveMemo());
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                imageView.setImageBitmap(bitmap);

                // Save image to internal storage and get path
                imagePath = ImageUtils.saveImageToInternalStorage(this, bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveMemo() {
        String title = editTextTitle.getText().toString();
        String content = editTextContent.getText().toString();

        if (title.trim().isEmpty() || content.trim().isEmpty()) {
            Toast.makeText(this, "Please insert a title and content", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_TITLE, title);
        data.putExtra(EXTRA_CONTENT, content);
        data.putExtra(EXTRA_IMAGE_PATH, imagePath);

        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        if (id != -1) {
            data.putExtra(EXTRA_ID, id);
        }

        setResult(RESULT_OK, data);
        finish();
    }
}