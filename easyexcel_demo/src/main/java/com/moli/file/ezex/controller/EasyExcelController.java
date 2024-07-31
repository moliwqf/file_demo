package com.moli.file.ezex.controller;

import com.moli.cache.common.ReturnData;
import com.moli.file.ezex.service.EasyExcelService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

/**
 * @author moli
 * @time 2024-07-31 15:04:20
 */
@RestController
@RequestMapping("ezexl")
public class EasyExcelController {

    @Resource
    private EasyExcelService easyExcelService;

    /**
     * 填充模版
     * @param response
     */
    @PostMapping("/excel/fill")
    public void fillTemplate(HttpServletResponse response) {
        try {
            response.reset();
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-disposition",
                    "attachment;filename=user_excel_template_" + System.currentTimeMillis() + ".xlsx");
            easyExcelService.fillExcelTemplate(response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 上传文件
     */
    @PostMapping("/excel/upload")
    public ReturnData<String> upload(@RequestPart(value = "file") MultipartFile file) {
        try {
            easyExcelService.upload(file.getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
            return ReturnData.fail(e.getMessage());
        }
        return ReturnData.ok();
    }

    /**
     * 下载文件
     */
    @GetMapping("/excel/download")
    public void download(HttpServletResponse response) {
        try {
            response.reset();
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-disposition",
                    "attachment;filename=user_excel_" + System.currentTimeMillis() + ".xlsx");
            easyExcelService.downloadExcel(response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
