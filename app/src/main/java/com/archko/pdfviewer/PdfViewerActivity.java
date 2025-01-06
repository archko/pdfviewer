package com.archko.pdfviewer;

import org.vudroid.core.DecodeService;
import org.vudroid.core.DecodeServiceBase;
import org.vudroid.pdfdroid.codec.PdfContext;

public class PdfViewerActivity extends BaseViewerActivity {
    @Override
    protected DecodeService createDecodeService() {
        return new DecodeServiceBase(new PdfContext());
    }
}
