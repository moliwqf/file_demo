package com.moli.file.poi.service.impl;

import com.moli.cache.entity.User;
import com.moli.file.poi.service.PoiService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.util.Units;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author moli
 * @time 2024-07-30 17:54:34
 */
@Slf4j
@Service
public class PoiServiceImpl implements PoiService {
    private static final int POSITION_ROW = 1;
    private static final int POSITION_COL = 1;

    @Override
    public XWPFDocument generateWordXWPFDocument() {
        XWPFDocument doc = new XWPFDocument();

        // Title
        createTitle(doc, "Java 全栈知识体系");

        // Chapter 1
        createChapterH1(doc, "1. 知识准备");
        createChapterH2(doc, "1.1 什么是POI");
        createParagraph(doc, "Apache POI 是创建和维护操作各种符合Office Open XML（OOXML）标准和微软的OLE 2复合文档格式（OLE2）的Java API。用它可以使用Java读取和创建,修改MS Excel文件.而且,还可以使用Java读取和创建MS Word和MSPowerPoint文件。更多请参考[官方文档](https://poi.apache.org/index.html)");
        createChapterH2(doc, "1.2 POI中基础概念");
        createParagraph(doc, "生成xls和xlsx有什么区别？POI对Excel中的对象的封装对应关系？");

        // Chapter 2
        createChapterH1(doc, "2. 实现案例");
        createChapterH2(doc, "2.1 用户列表示例");
        createParagraph(doc, "以导出用户列表为例");

        // 表格
        List<User> userList = getUserList();
        XWPFParagraph paragraph = doc.createParagraph();
        XWPFTable table = paragraph.getDocument().createTable(userList.size(), 5);
        table.setWidth(500);
        table.setCellMargins(20, 20, 20, 20);

        //表格属性
        CTTblPr tablePr = table.getCTTbl().addNewTblPr();
        //表格宽度
        CTTblWidth width = tablePr.addNewTblW();
        width.setW(BigInteger.valueOf(8000));

        for (int i = 0; i < userList.size(); i++) {
            List<XWPFTableCell> tableCells = table.getRow(i).getTableCells();
            tableCells.get(0).setText(userList.get(i).getId() + "");
            tableCells.get(1).setText(userList.get(i).getUserName());
            tableCells.get(2).setText(userList.get(i).getEmail());
            tableCells.get(3).setText(userList.get(i).getPhoneNumber() + "");
            tableCells.get(4).setText(userList.get(i).getDescription());
        }

        createChapterH2(doc, "2.2 图片导出示例");
        createParagraph(doc, "以导出图片为例");
        // 图片
        InputStream stream = null;
        try {
            XWPFParagraph paragraph2 = doc.createParagraph();
            Resource resource = new ClassPathResource("13200155.png");
            stream = new FileInputStream(resource.getFile());
            XWPFRun run = paragraph2.createRun();
            run.addPicture(stream, Document.PICTURE_TYPE_PNG, "Generated", Units.toEMU(256), Units.toEMU(256));
        } catch (IOException | InvalidFormatException e) {
            e.printStackTrace();
        }

        return doc;
    }

    private void createTitle(XWPFDocument doc, String content) {
        XWPFParagraph title = doc.createParagraph();
        title.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun r1 = title.createRun();
        r1.setBold(true);
        r1.setFontFamily("宋体");
        r1.setText(content);
        r1.setFontSize(22);
    }

    private void createChapterH1(XWPFDocument doc, String content) {
        XWPFParagraph actTheme = doc.createParagraph();
        actTheme.setAlignment(ParagraphAlignment.LEFT);
        XWPFRun runText1 = actTheme.createRun();
        runText1.setBold(true);
        runText1.setText(content);
        runText1.setFontSize(18);
    }

    private void createChapterH2(XWPFDocument doc, String content) {
        XWPFParagraph actType = doc.createParagraph();
        XWPFRun runText2 = actType.createRun();
        runText2.setBold(true);
        runText2.setText(content);
        runText2.setFontSize(15);
    }

    private void createParagraph(XWPFDocument doc, String content) {
        XWPFParagraph actType = doc.createParagraph();
        XWPFRun runText2 = actType.createRun();
        runText2.setText(content);
        runText2.setFontSize(11);
    }


    @Override
    public void upload(InputStream inputStream) throws IOException {
        XSSFWorkbook book = new XSSFWorkbook(inputStream);
        XSSFSheet sheet = book.getSheetAt(0);
        // add some validation here

        // parse data
        int cols;
        for (int i = POSITION_ROW; i < sheet.getLastRowNum(); i++) {
            XSSFRow row = sheet.getRow(i + 1); // 表头不算
            cols = POSITION_COL;
            User user = User.builder()
                    .id(getCellLongValue(row.getCell(cols++)))
                    .userName(getCellStringValue(row.getCell(cols++)))
                    .email(getCellStringValue(row.getCell(cols++)))
                    .phoneNumber(Long.parseLong(getCellStringValue(row.getCell(cols++))))
                    .description(getCellStringValue(row.getCell(cols++)))
                    .build();
            log.info(user.toString());
        }

        book.close();
    }

    private String getCellStringValue(XSSFCell cell) {
        try {
            if (null != cell) {
                return String.valueOf(cell.getStringCellValue());
            }
        } catch (Exception e) {
            return String.valueOf(getCellIntValue(cell));
        }
        return "";
    }

    private long getCellLongValue(XSSFCell cell) {
        try {
            if (null != cell) {
                return Long.parseLong("" + (long) cell.getNumericCellValue());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0L;
    }

    private int getCellIntValue(XSSFCell cell) {
        try {
            if (null != cell) {
                return Integer.parseInt("" + (int) cell.getNumericCellValue());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * @return SXSSFWorkbook
     */
    @Override
    public SXSSFWorkbook generateExcelWorkbook() {
        SXSSFWorkbook workbook = new SXSSFWorkbook();
        Sheet sheet = workbook.createSheet();

        int rows = POSITION_ROW;
        int cols = POSITION_COL;

        // 表头
        Row head = sheet.createRow(rows++);
        String[] columns = new String[]{"ID", "Name", "Email", "Phone", "Description"};
        int[] colWidths = new int[]{2000, 3000, 5000, 5000, 8000};
        CellStyle headStyle = getHeadCellStyle(workbook);
        for (int i = 0; i < columns.length; ++i) {
            sheet.setColumnWidth(cols, colWidths[i]);
            addCellWithStyle(head, cols++, headStyle).setCellValue(columns[i]);
        }

        // 表内容
        CellStyle bodyStyle = getBodyCellStyle(workbook);
        for (User user : getUserList()) {
            cols = POSITION_COL;
            Row row = sheet.createRow(rows++);
            addCellWithStyle(row, cols++, bodyStyle).setCellValue(user.getId());
            addCellWithStyle(row, cols++, bodyStyle).setCellValue(user.getUserName());
            addCellWithStyle(row, cols++, bodyStyle).setCellValue(user.getEmail());
            addCellWithStyle(row, cols++, bodyStyle).setCellValue(String.valueOf(user.getPhoneNumber()));
            addCellWithStyle(row, cols++, bodyStyle).setCellValue(user.getDescription());
        }
        return workbook;
    }

    private Cell addCellWithStyle(Row row, int colPosition, CellStyle cellStyle) {
        Cell cell = row.createCell(colPosition);
        cell.setCellStyle(cellStyle);
        return cell;
    }

    private List<User> getUserList() {
        return Collections.singletonList(User.builder()
                .id(1L).userName("pdai").email("pdai@pdai.tech").phoneNumber(121231231231L)
                .description("hello world")
                .build());
    }

    private CellStyle getHeadCellStyle(Workbook workbook) {
        CellStyle style = getBaseCellStyle(workbook);

        // fill
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        return style;
    }

    private CellStyle getBodyCellStyle(Workbook workbook) {
        return getBaseCellStyle(workbook);
    }

    private CellStyle getBaseCellStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();

        // font
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);

        // align
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.TOP);

        // border
        style.setBorderBottom(BorderStyle.THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(BorderStyle.THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderRight(BorderStyle.THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(BorderStyle.THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());

        return style;
    }
}
