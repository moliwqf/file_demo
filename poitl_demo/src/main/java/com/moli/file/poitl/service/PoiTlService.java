package com.moli.file.poitl.service;

import com.deepoove.poi.XWPFTemplate;

import java.io.IOException;

/**
 * @author moli
 * @time 2024-07-31 17:26:53
 */
public interface PoiTlService {
    /**
     * 生成word根据模版
     */
    XWPFTemplate generateWordXWPFTemplate() throws IOException;
}
