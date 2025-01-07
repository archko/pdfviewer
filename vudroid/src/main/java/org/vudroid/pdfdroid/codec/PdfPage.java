package org.vudroid.pdfdroid.codec;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;

import org.vudroid.core.cache.BitmapPool;
import org.vudroid.core.codec.CodecPage;
import org.vudroid.core.entity.Hyperlink;
import org.vudroid.core.entity.ReflowBean;

import java.util.ArrayList;
import java.util.List;

import io.legere.pdfiumandroid.PdfTextPage;

public class PdfPage implements CodecPage {

    private long pageHandle = -1;
    io.legere.pdfiumandroid.PdfPage page;
    int pdfPageWidth;
    int pdfPageHeight;
    private io.legere.pdfiumandroid.PdfDocument doc;

    public PdfPage(io.legere.pdfiumandroid.PdfDocument core, long pageHandle) {
        this.pageHandle = pageHandle;
        this.doc = core;
    }

    public int getWidth() {
        if (pdfPageWidth == 0) {
            pdfPageWidth = page.getPageWidthPoint();
        }
        return pdfPageWidth;
    }

    public int getHeight() {
        if (pdfPageHeight == 0) {
            pdfPageHeight = page.getPageHeightPoint();
        }
        return pdfPageHeight;
    }

    public void loadPage(int pageno) {
        page = doc.openPage(pageno);
    }

    public void setPage(io.legere.pdfiumandroid.PdfPage page) {
        this.page = page;
    }

    public io.legere.pdfiumandroid.PdfPage getPage() {
        return page;
    }

    public static PdfPage createPage(io.legere.pdfiumandroid.PdfDocument core, int pageno) {
        PdfPage pdfPage = new PdfPage(core, pageno);
        pdfPage.page = core.openPage(pageno);
        return pdfPage;
    }

    /**
     * 解码
     *
     * @param width           一个页面的宽
     * @param height          一个页面的高
     * @param pageSliceBounds 每个页面的边框
     * @param scale           缩放级别
     * @return 位图
     */
    public Bitmap renderBitmap(Rect cropBound, int width, int height, RectF pageSliceBounds, float scale) {
        Matrix matrix = new Matrix();
        //matrix.postScale(1f * width / getWidth(), 1f * -height / getHeight());
        //matrix.postTranslate(0, height);
        matrix.postTranslate(-pageSliceBounds.left * width, -pageSliceBounds.top * height);
        matrix.postScale(1 / pageSliceBounds.width(), 1 / pageSliceBounds.height());

        Bitmap bitmap = BitmapPool.getInstance().acquire(width, height);

        RectF renderBounds = new RectF(0f, 0f, width, height);
        matrix.mapRect(renderBounds);
        Rect bounds = new Rect();
        renderBounds.round(bounds);

        page.renderPageBitmap(bitmap,
                bounds.left, bounds.top, bounds.width(), bounds.height(),
                false, false);

        return bitmap;
    }

    public List<Hyperlink> getPageLinks() {
        List<Hyperlink> hyperlinks = new ArrayList<>();
        List<io.legere.pdfiumandroid.PdfDocument.Link> links = page.getPageLinks();
        if (!links.isEmpty()) {
            for (io.legere.pdfiumandroid.PdfDocument.Link link : links) {
                Hyperlink hyper = new Hyperlink();
                hyper.setPage((int) pageHandle);
                hyper.setBbox(new Rect((int) link.getBounds().left, (int) link.getBounds().top, (int) link.getBounds().right, (int) link.getBounds().bottom));

                if (link.getDestPageIdx() != null && link.getDestPageIdx() >= 0) {
                    hyper.setLinkType(Hyperlink.LINKTYPE_PAGE);
                } else {
                    hyper.setUrl(link.getUri());
                    hyper.setLinkType(Hyperlink.LINKTYPE_URL);
                }
                hyperlinks.add(hyper);
            }
        }

        return hyperlinks;
    }

    public List<ReflowBean> getReflowBean() {
        List<ReflowBean> reflowBeans = new ArrayList<>();
        PdfTextPage textPage = page.openTextPage();
        int charCount = textPage.textPageCountChars();
        if (charCount > 0) {
            String text = textPage.textPageGetText(0, charCount);
            ReflowBean reflowBean = new ReflowBean(text, ReflowBean.TYPE_STRING, String.valueOf(pageHandle));
            reflowBeans.add(reflowBean);

        }
        return reflowBeans;
    }

    public synchronized void recycle() {
        if (pageHandle >= 0) {
            pageHandle = -1;
            if (page != null) {
                page.close();
            }
        }
    }

    @Override
    public boolean isRecycle() {
        return pageHandle == -1;
    }

    @Override
    protected void finalize() throws Throwable {
        recycle();
        super.finalize();
    }
}

