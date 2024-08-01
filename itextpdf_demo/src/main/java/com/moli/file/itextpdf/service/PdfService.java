package com.moli.file.itextpdf.service;


import com.itextpdf.text.Document;

import java.io.OutputStream;

/**
 * @author moli
 * @time 2024-07-30 17:54:20
 */
public interface PdfService {
    /**
     * 生成pdf
     */
    Document generateItextPdfDocument(OutputStream os) throws Exception;
}
