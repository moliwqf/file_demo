package com.moli.file.ezex.service;

import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author moli
 * @time 2024-07-31 15:05:06
 */
public interface EasyExcelService {
    /**
     * 返回excel文件
     */
    void downloadExcel(ServletOutputStream outputStream);

    /**
     * 上传 excel 文件
     */
    void upload(InputStream inputStream)  throws IOException;

    /**
     * 填充excel模版
     */
    void fillExcelTemplate(ServletOutputStream outputStream);
}
