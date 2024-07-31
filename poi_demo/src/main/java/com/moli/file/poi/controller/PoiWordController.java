package com.moli.file.poi.controller;

import com.moli.file.poi.service.PoiService;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;

/**
 * @author moli
 * @time 2024-07-31 16:51:22
 */
@RestController
@RequestMapping("file")
public class PoiWordController {

    @Resource
    private PoiService poiService;

    /**
     * 下载 word 文档
     */
    @GetMapping("/word/download")
    public void download(HttpServletResponse response) {
        try {
            XWPFDocument document = poiService.generateWordXWPFDocument();
            response.reset();
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-disposition",
                    "attachment;filename=user_world_" + System.currentTimeMillis() + ".docx");
            OutputStream os = response.getOutputStream();
            document.write(os);
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
