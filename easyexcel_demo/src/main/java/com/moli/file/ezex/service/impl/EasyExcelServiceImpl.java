package com.moli.file.ezex.service.impl;

import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.enums.WriteDirectionEnum;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.fill.FillConfig;
import com.alibaba.excel.write.metadata.fill.FillWrapper;
import com.moli.file.ezex.entity.User;
import com.moli.file.ezex.service.EasyExcelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * @author moli
 * @time 2024-07-31 15:05:41
 */
@Slf4j
@Service
public class EasyExcelServiceImpl implements EasyExcelService {

    @Override
    public void downloadExcel(ServletOutputStream outputStream) {
        EasyExcelFactory.write(outputStream, User.class).sheet("User").doWrite(this::getUserList);
    }

    @Override
    public void upload(InputStream inputStream) throws IOException {
        List<User> cachedDataList = new ArrayList<>();
        // ReadListener不是必须的，它主要的设计是读取excel数据的后置处理(并考虑一次性读取到内存潜在的内存泄漏问题)
        EasyExcelFactory.read(inputStream, User.class, new ReadListener<User>() {

            @Override
            public void invoke(User user, AnalysisContext analysisContext) {
                cachedDataList.add(user);
            }

            @Override
            public void doAfterAllAnalysed(AnalysisContext analysisContext) {
                cachedDataList.forEach(user -> log.info(user.toString()));
            }
        }).sheet().doRead();
    }

    private List<User> getUserList() {
        List<User> res = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            res.add(User.builder()
                    .id(1L).userName("pdai").email("pdai@pdai.tech").phoneNumber(121231231231L)
                    .description("hello world")
                    .build());
        }
        return res;
    }

    @Override
    public void fillExcelTemplate(ServletOutputStream outputStream) {
        // 确保文件可访问，这个例子的excel模板，放在根目录下面
        String templateFileName = "D:\\JAVA入门\\file_demo\\easyexcel_demo\\src\\main\\resources\\user_excel_template.xlsx";
        // 方案1
        try (ExcelWriter excelWriter = EasyExcelFactory.write(outputStream).withTemplate(templateFileName).build()) {
            WriteSheet writeSheet = EasyExcelFactory.writerSheet().build();
            FillConfig fillConfig = FillConfig.builder().direction(WriteDirectionEnum.HORIZONTAL).build();
            // 如果有多个list 模板上必须有{前缀.} 这里的前缀就是 userList，然后多个list必须用 FillWrapper包裹
            excelWriter.fill(new FillWrapper("userList", getUserList()), fillConfig, writeSheet);
            excelWriter.fill(new FillWrapper("userList", getUserList()), fillConfig, writeSheet);

            excelWriter.fill(new FillWrapper("userList2", getUserList()), writeSheet);
            excelWriter.fill(new FillWrapper("userList2", getUserList()), writeSheet);

            Map<String, Object> map = new HashMap<>();
            map.put("user", "pdai");
            map.put("date", new Date());

            excelWriter.fill(map, writeSheet);
        }
    }
}
