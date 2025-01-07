package com.archko.pdfviewer;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_OPEN_PDF = 0x100;
    ImageView mImageView;
    ViewGroup rootView;
    private int margin = 0;
    private Toast toast = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StatusBarHelper.INSTANCE.hideSystemUI(this);

        setContentView(R.layout.activity_main);
        View btn = findViewById(R.id.btn);
        mImageView = findViewById(R.id.imageView);
        rootView = findViewById(R.id.rootView);

        if (margin <= 0) {
            margin = ViewConfiguration.get(this).getScaledTouchSlop() * 2;
        } else {
            margin = (int) (margin * 0.03);
        }
        toast = new Toast(this);
        toast.setDuration(Toast.LENGTH_SHORT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                        .setData(Uri.parse("package:" + getPackageName()));
                startActivity(intent);
            }
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                } else {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            1);
                }
            }
        }

        btn.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("application/pdf");
            startActivityForResult(intent, REQUEST_CODE_OPEN_PDF);
        });

        if (null != getIntent()) {
            String path = getIntent().getStringExtra("path");
            if (TextUtils.isEmpty(path)) {
                Uri uri = getIntent().getData();
                path = IntentFile.getFilePathByUri(this, uri);
                loadPdf(path);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            String path = IntentFile.getFilePathByUri(MainActivity.this, data.getData());
            Log.d("PDFSDK", "select file: " + path);
            loadPdf(path);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (null != intent && intent.getData() != null) {
            Uri uri = intent.getData();
            String path = IntentFile.getFilePathByUri(this, uri);
            loadPdf(path);
        }
    }

    private void loadPdf(String path) {
        final Intent intent = new Intent(this, PdfViewerActivity.class);
        intent.putExtra("path", path);
        startActivity(intent);
    }

    private void tap(MotionEvent e) {
        /*View documentView = pdfViewerRecyclerView;
        if (pdfViewerRecyclerView.tryHyperlink(e)) {
            return;
        }

        int top = documentView.getHeight() / 4;
        int bottom = documentView.getHeight() * 3 / 4;

        //Log.d("PDFSDK", "tap:$e,top:$top, bottom:$bottom, e.y:${e.y}");

        if (e.getY() < top) {
            int scrollY = documentView.getScrollY();
            scrollY -= documentView.getHeight();
            documentView.scrollBy(0, scrollY + margin);
        } else if (e.getY() > bottom) {
            int scrollY = documentView.getScrollY();
            scrollY += documentView.getHeight();
            documentView.scrollBy(0, scrollY - margin);
        } else {
            LinearLayoutManager layoutManager = (LinearLayoutManager) pdfViewerRecyclerView.getLayoutManager();
            PdfPageAdapter adapter = (PdfPageAdapter) pdfViewerRecyclerView.getAdapter();
            int position = layoutManager.findLastVisibleItemPosition();
            toast.setText(String.format("page:%s/%s", position, adapter.getItemCount()));
            toast.show();
        }*/
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}
