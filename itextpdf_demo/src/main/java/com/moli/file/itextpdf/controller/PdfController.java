package com.moli.file.itextpdf.controller;

import com.moli.file.itextpdf.service.PdfService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;

/**
 * @author moli
 * @time 2024-07-31 17:48:51
 */
@RestController
@RequestMapping("file")
public class PdfController {

    @Resource
    private PdfService pdfService;

    @GetMapping("/pdf/download")
    public void download(HttpServletResponse response) {
        try {
            response.reset();
            response.setContentType("application/pdf");
            response.setHeader("Content-disposition",
                    "attachment;filename=user_pdf_" + System.currentTimeMillis() + ".pdf");

            OutputStream os = response.getOutputStream();
            pdfService.generateItextPdfDocument(os);
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
