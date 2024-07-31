package com.moli.file.poi.controller;

import com.moli.cache.common.ReturnData;
import com.moli.file.poi.service.PoiService;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;

/**
 * @author moli
 * @time 2024-07-30 17:53:30
 */
@RestController
@RequestMapping("file")
public class PoiExcelController {

    @Resource
    private PoiService poiService;

    /**
     * 从 excel 中导入信息
     */
    @PostMapping("/excel/upload")
    public ReturnData<String> upload(@RequestPart(value = "file") MultipartFile file) {
        try {
            poiService.upload(file.getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
            return ReturnData.fail(e.getMessage());
        }
        return ReturnData.ok();
    }

    /**
     * 导出用户信息到excel中
     */
    @GetMapping("/excel/download")
    public void download(HttpServletResponse response) {
        try {
            SXSSFWorkbook workbook = poiService.generateExcelWorkbook();
            response.reset();
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-disposition",
                    "attachment;filename=user_excel_" + System.currentTimeMillis() + ".xlsx");
            OutputStream os = response.getOutputStream();
            workbook.write(os);
            workbook.dispose();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
