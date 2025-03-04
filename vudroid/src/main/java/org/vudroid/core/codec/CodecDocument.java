package org.vudroid.core.codec;

import java.util.List;

import org.vudroid.core.entity.ReflowBean;

public interface CodecDocument {
    CodecPage getPage(int pageNumber);

    int getPageCount();

    void recycle();

    List<OutlineLink> loadOutline();

    List<ReflowBean> decodeReflowText(int index);

    List<SearchResult> search(String text, int pageNum);
}
