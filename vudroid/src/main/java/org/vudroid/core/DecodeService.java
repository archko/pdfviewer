package org.vudroid.core;

import android.graphics.Bitmap;
import android.graphics.RectF;
import android.view.View;

import org.vudroid.core.codec.CodecDocument;
import org.vudroid.core.codec.CodecPage;
import org.vudroid.core.codec.OutlineLink;
import org.vudroid.core.codec.PageTextBox;
import org.vudroid.core.entity.APage;
import org.vudroid.core.utils.APageSizeLoader;

import java.util.List;

public interface DecodeService {

    void setContainerView(View containerView);

    CodecDocument open(String path, boolean cachePage);

    APageSizeLoader.PageSizeBean getPageSizeBean();

    void decodePage(String decodeKey, PageTreeNode node, boolean crop, int pageNumber, DecodeCallback decodeCallback, float zoom, RectF pageSliceBounds);

    void stopDecoding(String decodeKey);

    void setOriention(int oriention);

    int getEffectivePagesWidth(int page, boolean crop);

    int getEffectivePagesHeight(int index, boolean crop);

    int getPageCount();

    int getPageWidth(int pageIndex, boolean crop);

    int getPageHeight(int pageIndex, boolean crop);

    @Deprecated
    CodecPage getPage(int pageIndex);

    APage getAPage(int pageIndex);

    List<OutlineLink> getOutlines();

    void recycle();

    Bitmap decodeThumb(int page);

    interface DecodeCallback {
        void decodeComplete(Bitmap bitmap, boolean isThumb, Object args);

        boolean shouldRender(int pageNumber, boolean isFullPage);
    }

    interface SearchCallback {
        void result(List<PageTextBox> result, int index);
    }
}
