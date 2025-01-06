package org.vudroid.core.codec;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.RectF;

import org.vudroid.core.entity.Hyperlink;
import org.vudroid.core.entity.ReflowBean;

import java.util.List;

public interface CodecPage {

    int getWidth();

    int getHeight();

    //Bitmap renderBitmap(int width, int height, RectF pageSliceBounds);
    Bitmap renderBitmap(Rect cropBound, int width, int height, RectF pageSliceBounds, float scale);

    void recycle();

    boolean isRecycle();

    List<Hyperlink> getPageLinks();

    List<ReflowBean> getReflowBean();

    void loadPage(int pageNumber);
}
