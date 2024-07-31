package com.moli.file.poitl.controller;

import com.deepoove.poi.XWPFTemplate;
import com.moli.file.poitl.service.PoiTlService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;

/**
 * @author moli
 * @time 2024-07-31 17:26:24
 */
@RestController
@RequestMapping("file")
public class PoiTlController {

    @Resource
    private PoiTlService poiTlService;

    @GetMapping("/word/download")
    public void download(HttpServletResponse response) {
        try {
            XWPFTemplate document = poiTlService.generateWordXWPFTemplate();
            response.reset();
            response.setContentType("application/octet-stream");
            response.setHeader("Content-disposition",
                    "attachment;filename=user_word_" + System.currentTimeMillis() + ".docx");
            OutputStream os = response.getOutputStream();
            document.write(os);
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
