package com.moli.file.poi.service;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author moli
 * @time 2024-07-30 17:54:20
 */
public interface PoiService {

    SXSSFWorkbook generateExcelWorkbook();

    void upload(InputStream inputStream) throws IOException;

    XWPFDocument generateWordXWPFDocument();

}
