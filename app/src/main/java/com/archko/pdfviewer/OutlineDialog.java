//package com.archko.pdfviewer;
//
//import android.app.Dialog;
//import android.content.Context;
//import android.util.DisplayMetrics;
//import android.util.Log;
//import android.view.Gravity;
//import android.view.View;
//import android.view.WindowManager;
//import android.widget.ArrayAdapter;
//import android.widget.ListView;
//import android.widget.SeekBar;
//import android.widget.TextView;
//
//import com.artifex.mupdf.fitz.Outline;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//import androidx.annotation.NonNull;
//
//public class OutlineDialog extends Dialog {
//
//    public interface OutlineListener {
//        void selected(int page, boolean dismiss);
//    }
//
//    public static class Item {
//        public String title;
//        public int page;
//
//        public Item(String title, int page) {
//            this.title = title;
//            this.page = page;
//        }
//
//        public String toString() {
//            return String.format("%s - %s", page, title);
//        }
//    }
//
//    protected ArrayAdapter<Item> adapter;
//    private final List<Item> items = new ArrayList<>();
//    private boolean initOutline = false;
//    private ListView listView;
//    private TextView progress_txt;
//    private SeekBar seek_bar;
//    private int currentPage;
//    private int total;
//    private OutlineListener outlineListener;
//    private static final String pattern_str = "(#page=)(\\d+)(&)";
//    private final Pattern pattern = Pattern.compile(pattern_str);
//
//    public OutlineDialog(@NonNull Context context) {
//        super(context, R.style.ADialog_Bottom);
//        setContentView(R.layout.dialog_toc);
//        listView = findViewById(R.id.listview);
//        progress_txt = findViewById(R.id.progress_txt);
//        seek_bar = findViewById(R.id.seek_bar);
//        findViewById(R.id.close_btn).setOnClickListener(v -> toggleTocList());
//
//        adapter = new ArrayAdapter<>(context, R.layout.dialog_toc_item);
//        listView.setAdapter(adapter);
//        listView.setOnItemClickListener((parent, view, position, id) -> onListItemClick(position, id));
//
//        seek_bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                currentPage = progress;
//                updateProgress();
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//                if (null != outlineListener) {
//                    outlineListener.selected(seekBar.getProgress(), false);
//                }
//            }
//        });
//
//        //resize();
//
//        setCanceledOnTouchOutside(true);
//    }
//
//    private void resize() {
//        WindowManager.LayoutParams params = getWindow().getAttributes();
//        if (adapter.getCount() == 0) {
//            params.height = Utils.INSTANCE.dp2px(getContext(), 44);
//        } else {
//            DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
//            params.height = (int) (displayMetrics.heightPixels * 0.75);
//        }
//        getWindow().setAttributes(params);
//        getWindow().setGravity(Gravity.BOTTOM);
//    }
//
//    private void toggleTocList() {
//        if (listView.getVisibility() == View.VISIBLE) {
//            listView.setVisibility(View.GONE);
//        } else {
//            listView.setVisibility(View.VISIBLE);
//        }
//    }
//
//    public void clear() {
//        items.clear();
//        outlineListener = null;
//        currentPage = -1;
//        total = 1;
//        initOutline = false;
//    }
//
//    //item.uri:#page=2&zoom=nan,0,0
//    public void initOutlinesIfNeed(Outline[] outlines, int total, OutlineListener outlineListener) {
//        if (initOutline) {
//            return;
//        }
//
//        this.total = total;
//        this.outlineListener = outlineListener;
//        items.clear();
//        adapter.clear();
//        adapter.notifyDataSetChanged();
//
//        initOutline = true;
//        if (null == outlines || outlines.length == 0) {
//            return;
//        }
//        processOutline(outlines);
//        adapter.clear();
//        adapter.addAll(items);
//        adapter.notifyDataSetChanged();
//    }
//
//    private void updateProgress() {
//        seek_bar.setMax(total);
//        progress_txt.setText(String.format("%s/%s", currentPage, total));
//    }
//
//    private void processOutline(Outline[] outlines) {
//        if (outlines != null && outlines.length > 0) {
//            for (int i = 0; i < outlines.length; i++) {
//                Outline outline = outlines[i];
//                Matcher matcher = pattern.matcher(outline.uri);
//                if (matcher.find()) {
//                    int page = Integer.parseInt(matcher.group(0).replace("#page=", "").replace("&", ""));
//                    Item item = new Item(outline.title, page);
//                    items.add(item);
//                }
//
//                if (outline.down != null) {
//                    processOutline(outline.down);
//                }
//            }
//        }
//    }
//
//    /**
//     * @param currentPage
//     */
//    public void setCurrPage(int currentPage) {
//        this.currentPage = currentPage;
//        int found = -1;
//        for (int i = 0; i < items.size(); ++i) {
//            Item item = items.get(i);
//            if (found < 0 && item.page >= currentPage) {
//                found = i;
//            }
//        }
//        if (found >= 0) {
//            Log.d("PDFSDK", "found:" + found);
//            listView.setSelection(found);
//        }
//        adapter.notifyDataSetChanged();
//    }
//
//    @Override
//    public void show() {
//        super.show();
//        resize();
//        updateProgress();
//    }
//
//    protected void onListItemClick(int position, long id) {
//        Item item = adapter.getItem(position);
//        if (null != outlineListener) {
//            outlineListener.selected(item.page, true);
//        }
//    }
//}
