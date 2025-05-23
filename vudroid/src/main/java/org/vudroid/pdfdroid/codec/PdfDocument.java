package org.vudroid.pdfdroid.codec;

import com.artifex.mupdf.fitz.Document;
import com.artifex.mupdf.fitz.Outline;
import com.artifex.mupdf.fitz.Quad;

import org.vudroid.core.entity.ReflowBean;
import org.vudroid.core.codec.CodecDocument;
import org.vudroid.core.codec.CodecPage;
import org.vudroid.core.codec.OutlineLink;
import org.vudroid.core.codec.PageTextBox;
import org.vudroid.core.codec.SearchResult;

import java.util.ArrayList;
import java.util.List;

public class PdfDocument implements CodecDocument {

    private Document document;

    public void setDocument(Document document) {
        this.document = document;
    }

    public Document getDocument() {
        return document;
    }

    public CodecPage getPage(int pageNumber) {
        return PdfPage.createPage(document, pageNumber);
    }

    public int getPageCount() {
        return document.countPages();
    }

    public static PdfDocument openDocument(String fname, String pwd) {
        PdfDocument pdfDocument = new PdfDocument();
        Document core = null;
        System.out.println("Trying to open " + fname);
        try {
            core = Document.openDocument(fname);
            //if (IntentFile.INSTANCE.isReflowable(fname)) {
            //    int w = Utils.getScreenWidthPixelWithOrientation(App.Companion.getInstance());
            //    int h = Utils.getScreenHeightPixelWithOrientation(App.Companion.getInstance());
            //    core.layout(w, h, 32);
            //}
            pdfDocument.setDocument(core);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return pdfDocument;
    }

    @Override
    protected void finalize() throws Throwable {
        recycle();
        super.finalize();
    }

    public synchronized void recycle() {
        if (null != document) {
            document.destroy();
        }
    }

    @Override
    public List<OutlineLink> loadOutline() {
        Outline[] outlines = document.loadOutline();
        List<OutlineLink> links = new ArrayList<>();
        if (outlines != null) {
            downOutline(document, outlines, links);
        }
        return links;
    }

    @Override
    public List<ReflowBean> decodeReflowText(int index) {
        List<ReflowBean> beans = new ArrayList<>();
        //MupdfDocument.Companion.decodeReflowText(index, document);
        return beans;
    }

    @Override
    public List<SearchResult> search(String text, int pageNum) {
        List<SearchResult> searchResults = new ArrayList<>();
        int count = document.countPages();
        for (int i = 0; i < count; i++) {
            CodecPage page = getPage(i);
            Object[] results = ((PdfPage) page).page.search(text);
            if (results == null || results.length == 0) {
                continue;
            }

            List<PageTextBox> boxes = new ArrayList<>();
            for (Object result : results) {
                Quad[] quads = (Quad[]) result;
                for (Quad q : quads) {
                    com.artifex.mupdf.fitz.Rect fitzRect = q.toRect();
                    PageTextBox rectF = new PageTextBox(fitzRect.x0, fitzRect.y0, fitzRect.x1, fitzRect.y1);
                    boxes.add(rectF);
                }
            }
            StringBuilder sb = new StringBuilder();
            byte[] result = ((PdfPage) page).page.textAsText("preserve-whitespace,inhibit-spaces");
            if (null != result) {
                List<ReflowBean> reflowBeans = new ArrayList<>();//
                // ParseTextMain.INSTANCE.parseAsTextList(result, i);
                if (reflowBeans != null && !reflowBeans.isEmpty()) {
                    for (ReflowBean bean : reflowBeans) {
                        sb.append(bean.getData()).append(" ");
                    }
                }
            }

            SearchResult searchResult = new SearchResult(i, boxes, sb.toString());
            searchResults.add(searchResult);
        }

        return searchResults;
    }

    public static void downOutline(Document core, Outline[] outlines, List<OutlineLink> links) {
        if (null != outlines) {
            for (Outline outline : outlines) {
                int page = core.pageNumberFromLocation(core.resolveLink(outline));
                OutlineLink link = new OutlineLink(outline.title, page, 0);
                if (outline.down != null) {
                    Outline[] child = outline.down;
                    downOutline(core, child, links);
                }
                links.add(link);
            }
        }
    }
}
