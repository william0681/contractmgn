package com.zx.servicegateway.util;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.*;

public class ExcelUtil {

    /**
     * @param headName 标题
     * @param title    表头
     * @param data     内容
     * @throws IOException
     * @throws InvalidFormatException
     */
    @SuppressWarnings("all")
    public static void exportExcel(HttpServletResponse response, String headName, String[] title, List<LinkedHashMap<String, Object>> data, Map<String, Object> totals) throws IOException, InvalidFormatException {
        int titleLength = title.length;
        Workbook wb = new HSSFWorkbook();//创建一个excel
        Map<String, CellStyle> styles = createStyles(wb);
        Sheet sheet1 = wb.createSheet(" sheet1");//创建sheet
        Row headerRow = sheet1.createRow(0);
        headerRow.setHeightInPoints(40f);
        Cell cellHeader = headerRow.createCell(0);
        cellHeader.setCellValue(headName);
        cellHeader.setCellStyle(styles.get("header"));
        for (int i = 1; i < titleLength; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellStyle(styles.get("header"));
        }
        sheet1.addMergedRegion(new CellRangeAddress(0, 0, 0, titleLength - 1));
        Row row;
        Cell cell;
        int rowNum = 1;
        row = sheet1.createRow(rowNum);
        for (int c = 0; c < title.length; c++) {
            sheet1.setColumnWidth(c, 15 * 256);
            cell = row.createCell(c);
            cell.setCellValue(title[c]);
            cell.setCellStyle(styles.get("title"));
        }
        rowNum++;

        for (Map<String,Object> map : data) {
            row = sheet1.createRow(rowNum);
            int cellNum = 0;
            for (Object object : map.values()) {
                cell = row.createCell(cellNum);
                cell.setCellValue(object.toString());
                cell.setCellStyle(styles.get("content"));
                cellNum++;
            }
            rowNum++;

        }
        Sheet sheet2 = wb.createSheet(" sheet2");//创建sheet2
        for (int c = 0; c < 6; c++) {
            sheet2.setColumnWidth(c, 25 * 256);
        }
        Row headerRow2 = sheet2.createRow(0);
        headerRow2.setHeightInPoints(40f);
        Cell cellHeader2 = headerRow2.createCell(0);
        cellHeader2.setCellValue(headName);
        cellHeader2.setCellStyle(styles.get("header"));
        for (int i = 1; i < 6; i++) {
            Cell cell2 = headerRow2.createCell(i);
            cell2.setCellStyle(styles.get("header"));
        }
        sheet2.addMergedRegion(new CellRangeAddress(0, 0, 0, 5));
        Row titleRow2 = sheet2.createRow(1);
        cell = titleRow2.createCell(1);
        cell.setCellValue("合同金额");
        cell.setCellStyle(styles.get("title"));
        cell = titleRow2.createCell(2);
        cell.setCellValue("该时间段签订合同");
        cell.setCellStyle(styles.get("title"));
        cell = titleRow2.createCell(3);
        cell.setCellValue("累计签订合同");
        cell.setCellStyle(styles.get("title"));
        cell = titleRow2.createCell(4);
        cell.setCellValue("该时间段执行合同");
        cell.setCellStyle(styles.get("title"));
        cell = titleRow2.createCell(5);
        cell.setCellValue("累计执行合同");
        cell.setCellStyle(styles.get("title"));
        row = sheet2.createRow(2);
        cell = row.createCell(0);
        cell.setCellValue("合计金额");
        cell.setCellStyle(styles.get("title"));
        row = sheet2.createRow(3);
        cell = row.createCell(0);
        cell.setCellValue("合计份数");
        cell.setCellStyle(styles.get("title"));
        row = sheet2.getRow(2);
        cell = row.createCell(1);
        cell.setCellValue(totals.get("total_money").toString());
        cell.setCellStyle(styles.get("content"));
        row = sheet2.getRow(3);
        cell = row.createCell(1);
        cell.setCellValue(totals.get("total_count").toString());
        cell.setCellStyle(styles.get("content"));
        //该时间段签订合同
        Map<String, Object> period_contract = (Map<String, Object>) totals.get("period_contract");
        row = sheet2.getRow(2);
        cell = row.createCell(2);
        cell.setCellValue(period_contract.get("money").toString());
        cell.setCellStyle(styles.get("content"));
        row = sheet2.getRow(3);
        cell = row.createCell(2);
        cell.setCellValue(period_contract.get("count").toString());
        cell.setCellStyle(styles.get("content"));
        //累计签订合同
        if (!totals.get("grand_total_contract").equals("所选时间跨年，不可累计")) {
            Map<String, Object> grand_total_contract = (Map<String, Object>) totals.get("grand_total_contract");
            row = sheet2.getRow(2);
            cell = row.createCell(3);
            cell.setCellValue(grand_total_contract.get("money").toString());
            cell.setCellStyle(styles.get("content"));
            row = sheet2.getRow(3);
            cell = row.createCell(3);
            cell.setCellValue(grand_total_contract.get("count").toString());
            cell.setCellStyle(styles.get("content"));
        } else {
            row = sheet2.getRow(2);
            cell = row.createCell(3);
            cell.setCellValue("所选时间跨年，不可累计");
            cell.setCellStyle(styles.get("content"));
            row = sheet2.getRow(3);
            cell = row.createCell(3);
            cell.setCellValue("所选时间跨年，不可累计");
            cell.setCellStyle(styles.get("content"));
            sheet2.addMergedRegion(new CellRangeAddress(2, 3, 3, 3));
            Cell mergeCell = sheet2.getRow(2).getCell(3);
            HSSFCellStyle hssfCellStyle = creatMergeStyle(wb);
            mergeCell.setCellStyle(hssfCellStyle);
        }
        //该时间段执行合同
        Map<String, Object> period_execute_contract = (Map<String, Object>) totals.get("period_execute_contract");
        row = sheet2.getRow(2);
        cell = row.createCell(4);
        cell.setCellValue(period_execute_contract.get("money").toString());
        cell.setCellStyle(styles.get("content"));
        row = sheet2.getRow(3);
        cell = row.createCell(4);
        cell.setCellValue(period_execute_contract.get("count").toString());
        cell.setCellStyle(styles.get("content"));
        //累计执行合同
        if (!totals.get("grand_total_execute_contract").equals("所选时间跨年，不可累计")) {
            Map<String, Object> grand_total_execute_contract = (Map<String, Object>) totals.get("grand_total_execute_contract");
            row = sheet2.getRow(2);
            cell = row.createCell(5);
            cell.setCellValue(grand_total_execute_contract.get("money").toString());
            cell.setCellStyle(styles.get("content"));
            row = sheet2.getRow(3);
            cell = row.createCell(5);
            cell.setCellValue(grand_total_execute_contract.get("count").toString());
            cell.setCellStyle(styles.get("content"));
        } else {
            row = sheet2.getRow(2);
            cell = row.createCell(5);
            cell.setCellValue("所选时间跨年，不可累计");
            cell.setCellStyle(styles.get("content"));
            row = sheet2.getRow(3);
            cell = row.createCell(5);
            cell.setCellValue("所选时间跨年，不可累计");
            cell.setCellStyle(styles.get("content"));
            sheet2.addMergedRegion(new CellRangeAddress(2, 3, 5, 5));
            Cell mergeCell = sheet2.getRow(2).getCell(5);
            HSSFCellStyle hssfCellStyle = creatMergeStyle(wb);
            mergeCell.setCellStyle(hssfCellStyle);
        }


        String filename = headName + ".xls";
        if (wb instanceof XSSFWorkbook) filename += "x";
        response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));
        OutputStream outputStream = response.getOutputStream();
        wb.write(outputStream);
        outputStream.flush();
        outputStream.close();
//        String file = "E:\\ylwexcel\\"+headName+".xls";
//        if (wb instanceof XSSFWorkbook) file += "x";
//        FileOutputStream out = new FileOutputStream(file);
//        wb.write(out);
//        out.close();
//        wb.close();

    }


    @SuppressWarnings("all")
    public static void exportFinanceExcel(HttpServletResponse response, String headName, String[] title, List<LinkedHashMap<String, Object>> data, String sql) throws IOException {
        int titleLength = title.length;
        Workbook wb = new HSSFWorkbook();//创建一个excel
        Map<String, CellStyle> styles = createStyles(wb);
        Sheet sheet1 = wb.createSheet(" sheet1");//创建sheet
        Row headerRow = sheet1.createRow(0);
        headerRow.setHeightInPoints(35f);
        Cell cellHeader = headerRow.createCell(0);
        cellHeader.setCellStyle(styles.get("header"));
        cellHeader.setCellValue(headName);
        cellHeader.setCellStyle(styles.get("header"));
        sheet1.addMergedRegion(new CellRangeAddress(0, 0, 0, titleLength - 1));
        Row row;
        Cell cell;
        int rownum = 1;
        row = sheet1.createRow(rownum);
        for (int c = 0; c < title.length; c++) {
            sheet1.setColumnWidth(c, 20 * 256);
            cell = row.createCell(c);
            cell.setCellValue(title[c]);
            cell.setCellStyle(styles.get("title"));
        }
        rownum++;
        //标记应收款和实收款所在位置
        int actualMoneyCol=0,shouldMoneyCol=0;
        //统计总额
        Double totalActualMoney=0.0,totalShoudMoney=0.0,totalMoney=0.0,shouldButNotMoney=0.0;
        Double actualMoney=0.0,shouldMoney=0.0,money=0.0;
        for (Map<String, Object> map : data) {
            int firstRow = rownum;
            int flag = 0;
            if (sql.contains("1") && sql.contains("2")) {
                List<LinkedHashMap<String, Object>> receive = (List<LinkedHashMap<String, Object>>) map.get("receive");
                List<LinkedHashMap<String, Object>> tickets = (List<LinkedHashMap<String, Object>>) map.get("tickets");
                if (receive == null)
                    receive = new ArrayList<>();
                if (tickets == null)
                    tickets = new ArrayList<>();
                int i = 0;

                while (i < receive.size() || i < tickets.size() || i < 1) {
                    int colum = 0;
                    row = sheet1.createRow(rownum);
                    cell = row.createCell(colum);
                    if (i >= receive.size()) {
                        receive.add(new LinkedHashMap<>());
                    }
                    if (i >= tickets.size()) {
                        tickets.add(new LinkedHashMap<>());
                    }
                    cell.setCellValue(map.get("contract_name").toString());
                    cell.setCellStyle(styles.get("content"));
                    colum++;
                    cell = row.createCell(colum);
                    cell.setCellValue(map.get("contract_type").toString());
                    cell.setCellStyle(styles.get("content"));
                    colum++;
                    cell = row.createCell(colum);
                    cell.setCellValue(map.get("block").toString());
                    cell.setCellStyle(styles.get("content"));
                    colum++;
                    cell = row.createCell(colum);
                    cell.setCellValue(map.get("total_money").toString());
                    if(null!=map.get("total_money")){
                        money=(Double)map.get("total_money");
                    }
                    cell.setCellStyle(styles.get("content"));
                    colum++;
                    cell = row.createCell(colum);
                    cell.setCellValue(get(receive.get(i), "node_name").toString());
                    cell.setCellStyle(styles.get("content"));
                    colum++;
                    cell = row.createCell(colum);
                    cell.setCellValue(get(receive.get(i), "issue").toString());
                    cell.setCellStyle(styles.get("content"));
                    colum++;
                    cell = row.createCell(colum);
                    cell.setCellValue(get(receive.get(i), "should_money").toString());
                    cell.setCellStyle(styles.get("content"));
                    colum++;
                    cell = row.createCell(colum);
                    cell.setCellValue(get(receive.get(i), "actral_money").toString());
                    cell.setCellStyle(styles.get("content"));
                    colum++;
                    cell = row.createCell(colum);
                    cell.setCellValue(get(receive.get(i), "state").toString());
                    cell.setCellStyle(styles.get("content"));
                    colum++;
                    cell = row.createCell(colum);
                    cell.setCellValue(get(tickets.get(i), "issue").toString());
                    cell.setCellStyle(styles.get("content"));
                    colum++;
                    cell = row.createCell(colum);
                    cell.setCellValue(get(tickets.get(i), "should_money").toString());
                    cell.setCellStyle(styles.get("content"));
                    colum++;
                    cell = row.createCell(colum);
                    cell.setCellValue(get(tickets.get(i), "actral_money").toString());
                    cell.setCellStyle(styles.get("content"));
                    colum++;
                    cell = row.createCell(colum);
                    cell.setCellValue(get(tickets.get(i), "state").toString());
                    cell.setCellStyle(styles.get("content"));
                    colum++;
                    flag = colum;//标记后面需要合并的开始列；
                    if (sql.contains("3")) {
                        cell = row.createCell(colum);
                        cell.setCellValue(get(map, "should_total_money").toString());
                        cell.setCellStyle(styles.get("content"));
                        if(null!=map.get("should_total_money")){
                            shouldMoney=(Double)map.get("should_total_money");
                            System.out.println(i);
                        }else{
                            shouldMoney=0.0;
                        }
                        shouldMoneyCol=colum;
                        colum++;
                        cell = row.createCell(colum);
                        cell.setCellValue(get(map, "actural_total_money").toString());
                        cell.setCellStyle(styles.get("content"));
                        if(null!=map.get("actural_total_money")){
                            actualMoney=(Double)map.get("actural_total_money");
                        }else{
                            actualMoney=0.0;
                        }
                        actualMoneyCol=colum;
                        colum++;
                    }
                    if (sql.contains("4")) {
                        cell = row.createCell(colum);
                        cell.setCellValue(get(map, "should_total_ticket").toString());
                        cell.setCellStyle(styles.get("content"));
                        colum++;
                        cell = row.createCell(colum);
                        cell.setCellValue(get(map, "actural_total_ticket").toString());
                        cell.setCellStyle(styles.get("content"));
                    }
                    rownum++;
                    i++;

                }
                totalMoney+=money;
                totalActualMoney+=actualMoney;
                totalShoudMoney+=shouldMoney;

            }
            if (sql.contains("1") && !sql.contains("2")) {

                List<LinkedHashMap<String, Object>> receive = (List<LinkedHashMap<String, Object>>) map.get("receive");
                if (receive == null)
                    receive = new ArrayList<>();
                int i = 0;
                while (i < receive.size() || i < 1) {
                    int colum = 0;
                    row = sheet1.createRow(rownum);
                    cell = row.createCell(colum);
                    if (i >= receive.size()) {
                        receive.add(new LinkedHashMap<>());
                    }
                    cell.setCellValue(map.get("contract_name").toString());
                    cell.setCellStyle(styles.get("content"));
                    colum++;
                    cell = row.createCell(colum);
                    cell.setCellValue(map.get("contract_type").toString());
                    cell.setCellStyle(styles.get("content"));
                    colum++;
                    cell = row.createCell(colum);
                    cell.setCellValue(map.get("block").toString());
                    cell.setCellStyle(styles.get("content"));
                    colum++;
                    cell = row.createCell(colum);
                    cell.setCellValue(map.get("total_money").toString());
                    cell.setCellStyle(styles.get("content"));
                    if(null!=map.get("total_money")){
                        money=(Double)map.get("total_money");
                    }
                    colum++;
                    cell = row.createCell(colum);
                    cell.setCellValue(get(receive.get(i), "node_name").toString());
                    cell.setCellStyle(styles.get("content"));
                    colum++;
                    cell = row.createCell(colum);
                    cell.setCellValue(get(receive.get(i), "issue").toString());
                    cell.setCellStyle(styles.get("content"));
                    colum++;
                    cell = row.createCell(colum);
                    cell.setCellValue(get(receive.get(i), "should_money").toString());
                    cell.setCellStyle(styles.get("content"));
                    colum++;
                    cell = row.createCell(colum);
                    cell.setCellValue(get(receive.get(i), "actral_money").toString());
                    cell.setCellStyle(styles.get("content"));
                    colum++;
                    cell = row.createCell(colum);
                    cell.setCellValue(get(receive.get(i), "state").toString());
                    cell.setCellStyle(styles.get("content"));
                    colum++;
                    flag = colum;//标记后面需要合并的开始列；
                    if (sql.contains("3")) {
                        cell = row.createCell(colum);
                        cell.setCellValue(get(map, "should_total_money").toString());
                        cell.setCellStyle(styles.get("content"));
                        if(null!=map.get("should_total_money")){
                            shouldMoney=(Double)map.get("should_total_money");
                        }else{
                            shouldMoney=0.0;
                        }
                        shouldMoneyCol=colum;
                        colum++;
                        cell = row.createCell(colum);
                        cell.setCellValue(get(map, "actural_total_money").toString());
                        cell.setCellStyle(styles.get("content"));
                        if(null!=map.get("actural_total_money")){
                            actualMoney=(Double)map.get("actural_total_money");
                        }else{
                            actualMoney=0.0;
                        }
                        actualMoneyCol=colum;
                        colum++;
                    }
                    if (sql.contains("4")) {
                        cell = row.createCell(colum);
                        cell.setCellValue(get(map, "should_total_ticket").toString());
                        cell.setCellStyle(styles.get("content"));
                        colum++;
                        cell = row.createCell(colum);
                        cell.setCellValue(get(map, "actural_total_ticket").toString());
                        cell.setCellStyle(styles.get("content"));
                    }
                    rownum++;
                    i++;

                }
                totalMoney+=money;
                totalActualMoney+=actualMoney;
                totalShoudMoney+=shouldMoney;
            }
            if (!sql.contains("1") && sql.contains("2")) {
                List<LinkedHashMap<String, Object>> tickets = (List<LinkedHashMap<String, Object>>) map.get("tickets");
                if (tickets == null)
                    tickets = new ArrayList<>();
                int i = 0;
                while (i < tickets.size() || i < 1) {
                    int colum = 0;
                    row = sheet1.createRow(rownum);
                    cell = row.createCell(colum);
                    if (i >= tickets.size()) {
                        tickets.add(new LinkedHashMap<>());
                    }
                    cell.setCellValue(map.get("contract_name").toString());
                    cell.setCellStyle(styles.get("content"));
                    colum++;
                    cell = row.createCell(colum);
                    cell.setCellValue(map.get("contract_type").toString());
                    cell.setCellStyle(styles.get("content"));
                    colum++;
                    cell = row.createCell(colum);
                    cell.setCellValue(map.get("block").toString());
                    cell.setCellStyle(styles.get("content"));
                    colum++;
                    cell = row.createCell(colum);
                    cell.setCellValue(map.get("total_money").toString());
                    cell.setCellStyle(styles.get("content"));
                    if(null!=map.get("total_money")){
                       money=(Double)map.get("total_money");
                    }
                    colum++;
                    cell = row.createCell(colum);
                    cell.setCellValue(get(tickets.get(i), "issue").toString());
                    cell.setCellStyle(styles.get("content"));
                    colum++;
                    cell = row.createCell(colum);
                    cell.setCellValue(get(tickets.get(i), "should_money").toString());
                    cell.setCellStyle(styles.get("content"));
                    colum++;
                    cell = row.createCell(colum);
                    cell.setCellValue(get(tickets.get(i), "actral_money").toString());
                    cell.setCellStyle(styles.get("content"));
                    colum++;
                    cell = row.createCell(colum);
                    cell.setCellValue(get(tickets.get(i), "state").toString());
                    cell.setCellStyle(styles.get("content"));
                    colum++;
                    flag = colum;//标记后面需要合并的开始列；
                    if (sql.contains("3")) {
                        cell = row.createCell(colum);
                        cell.setCellValue(get(map, "should_total_money").toString());
                        cell.setCellStyle(styles.get("content"));
                        if(null!=map.get("should_total_money")){
                            shouldMoney=(Double)map.get("should_total_money");
                        }else{
                            shouldMoney=0.0;
                        }
                        shouldMoneyCol=colum;
                        colum++;
                        cell = row.createCell(colum);
                        cell.setCellValue(get(map, "actural_total_money").toString());
                        cell.setCellStyle(styles.get("content"));
                        if(null!=map.get("actural_total_money")){
                            actualMoney=(Double)map.get("actural_total_money");
                        }else{
                            actualMoney=0.0;
                        }
                        actualMoneyCol=colum;
                        colum++;
                    }
                    if (sql.contains("4")) {
                        cell = row.createCell(colum);
                        cell.setCellValue(get(map, "should_total_ticket").toString());
                        cell.setCellStyle(styles.get("content"));
                        colum++;
                        cell = row.createCell(colum);
                        cell.setCellValue(get(map, "actural_total_ticket").toString());
                        cell.setCellStyle(styles.get("content"));
                    }
                    rownum++;
                    i++;

                }

                totalMoney+=money;
                totalActualMoney+=actualMoney;
                totalShoudMoney+=shouldMoney;
            }
            if (!sql.contains("1") && !sql.contains("2")) {
                int colum = 0;
                row = sheet1.createRow(rownum);
                cell = row.createCell(colum);
                cell.setCellValue(map.get("contract_name").toString());
                cell.setCellStyle(styles.get("content"));
                colum++;
                cell = row.createCell(colum);
                cell.setCellValue(map.get("contract_type").toString());
                cell.setCellStyle(styles.get("content"));
                colum++;
                cell = row.createCell(colum);
                cell.setCellValue(map.get("block").toString());
                cell.setCellStyle(styles.get("content"));
                colum++;
                cell = row.createCell(colum);
                cell.setCellValue(map.get("total_money").toString());
                cell.setCellStyle(styles.get("content"));
                if(null!=map.get("total_money")){
                    totalMoney+=(Double)map.get("total_money");
                }
                colum++;
                flag = colum;//标记后面需要合并的开始列；
                if (sql.contains("3")) {
                    cell = row.createCell(colum);
                    cell.setCellValue(get(map, "should_total_money").toString());
                    cell.setCellStyle(styles.get("content"));
                    if(null!=map.get("should_total_money")){
                        shouldMoney=(Double)map.get("should_total_money");
                    }else{
                        shouldMoney=0.0;
                    }
                    shouldMoneyCol=colum;
                    colum++;
                    cell = row.createCell(colum);
                    cell.setCellValue(get(map, "actural_total_money").toString());
                    cell.setCellStyle(styles.get("content"));
                    if(null!=map.get("actural_total_money")){
                        actualMoney=(Double)map.get("actural_total_money");
                    }else{
                        actualMoney=0.0;
                    }
                    actualMoneyCol=colum;
                    colum++;
                }
                if (sql.contains("4")) {
                    cell = row.createCell(colum);
                    cell.setCellValue(get(map, "should_total_ticket").toString());
                    cell.setCellStyle(styles.get("content"));
                    colum++;
                    cell = row.createCell(colum);
                    cell.setCellValue(get(map, "actural_total_ticket").toString());
                    cell.setCellStyle(styles.get("content"));
                }
                rownum++;
                totalMoney+=money;
                totalActualMoney+=actualMoney;
                totalShoudMoney+=shouldMoney;
            }
            if (sql.contains("1") || sql.contains("2")) {
                for (int j = 0; j < 4; j++) {
                    if (rownum - 1 - firstRow >= 1) {
                        sheet1.addMergedRegion(new CellRangeAddress(firstRow, rownum - 1, j, j));
                        Cell mergeCell = sheet1.getRow(firstRow).getCell(j);
                        HSSFCellStyle hssfCellStyle = creatMergeStyle(wb);
                        mergeCell.setCellStyle(hssfCellStyle);
                    }

                }
            }


            if ((sql.contains("1") || sql.contains("2")) && (sql.contains("3") || sql.contains("4"))) {
                for (int j = flag; j < titleLength; j++) {
                    if (rownum - 1 - firstRow >= 1) {
                        sheet1.addMergedRegion(new CellRangeAddress(firstRow, rownum - 1, j, j));
                        Cell mergeCell = sheet1.getRow(firstRow).getCell(j);
                        HSSFCellStyle hssfCellStyle = creatMergeStyle(wb);
                        mergeCell.setCellStyle(hssfCellStyle);
                    }

                }
            }
            if (sql.contains("1")) {
                int mergeNum = 0;
                for (int a = firstRow + 1; a < rownum; a++) {
                    if (sheet1.getRow(a).getCell(4).getStringCellValue().equals(sheet1.getRow(a - 1).getCell(4).getStringCellValue())) {
                        mergeNum++;
                    } else {
                        if (mergeNum > 0) {
                            sheet1.addMergedRegion(new CellRangeAddress(a - mergeNum - 1, a - 1, 4, 4));
                            Cell mergeCell = sheet1.getRow(firstRow).getCell(4);
                            HSSFCellStyle hssfCellStyle = creatMergeStyle(wb);
                            mergeCell.setCellStyle(hssfCellStyle);
                        }

                        mergeNum = 0;
                    }
                }
            }
        }
        Row totalRow=sheet1.createRow(sheet1.getLastRowNum()+1);
        totalRow.createCell(3).setCellValue(totalMoney);
        totalRow.getCell(3).setCellStyle(styles.get("content"));
        totalRow.createCell(0).setCellValue("合计");
        totalRow.getCell(0).setCellStyle(styles.get("content"));
        if(sql.contains("3")){
            totalRow.createCell(shouldMoneyCol).setCellValue(totalShoudMoney);
            totalRow.getCell(shouldMoneyCol).setCellStyle(styles.get("content"));
            totalRow.createCell(actualMoneyCol).setCellValue(totalActualMoney);
            totalRow.getCell(actualMoneyCol).setCellStyle(styles.get("content"));
            }
        String filename = headName + ".xls";
        if (wb instanceof XSSFWorkbook) filename += "x";
        response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));
        OutputStream outputStream = response.getOutputStream();
        wb.write(outputStream);
        outputStream.flush();
        outputStream.close();


//        String file = "E:\\ylwexcel\\" + headName + ".xls";
//        if (wb instanceof XSSFWorkbook) file += "x";
//        FileOutputStream out = new FileOutputStream(file);
//        wb.write(out);
//        out.close();
//        wb.close();


    }


    private static Object get(Map<String, Object> map, String key) {
        return map.get(key) == null ? "--" : map.get(key);

    }

    /**
     * Creates a cell and aligns it a certain way.创建一个单元格
     *
     * @param wb     the workbook
     * @param row    the row to create the cell in
     * @param column the column number to create the cell in
     * @param halign the horizontal alignment for the cell.水平对齐方式
     * @param valign the vertical alignment for the cell.垂直对齐方式
     */

    /**
     * 创建border样式
     *
     * @param wb
     * @return
     */
    private static CellStyle createBorderedStyle(Workbook wb) {
        BorderStyle thin = BorderStyle.THIN;
        short black = IndexedColors.BLACK.getIndex();
        CellStyle style = wb.createCellStyle();
        style.setBorderRight(thin);
        style.setRightBorderColor(black);
        style.setBorderBottom(thin);
        style.setBottomBorderColor(black);
        style.setBorderLeft(thin);
        style.setLeftBorderColor(black);
        style.setBorderTop(thin);
        style.setTopBorderColor(black);
        style.setWrapText(true);//一个cell方不下内容时文本自动换行
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        return style;

    }

    private static HSSFCellStyle creatMergeStyle(Workbook wb) {
        HSSFCellStyle hssfCellStyle = (HSSFCellStyle) wb.createCellStyle();
        BorderStyle thin = BorderStyle.THIN;
        short black = IndexedColors.BLACK.getIndex();
        hssfCellStyle.setBorderRight(thin);
        hssfCellStyle.setRightBorderColor(black);
        hssfCellStyle.setBorderBottom(thin);
        hssfCellStyle.setBottomBorderColor(black);
        hssfCellStyle.setBorderLeft(thin);
        hssfCellStyle.setLeftBorderColor(black);
        hssfCellStyle.setBorderTop(thin);
        hssfCellStyle.setTopBorderColor(black);
        hssfCellStyle.setAlignment(HorizontalAlignment.CENTER);
        hssfCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        Font font2 = wb.createFont();
        font2.setFontName("微软雅黑");
        hssfCellStyle.setFont(font2);
        return hssfCellStyle;
    }

    /**
     * create a library of cell styles
     *
     * @param wb
     * @return
     */
    @SuppressWarnings("all")
    private static Map<String, CellStyle> createStyles(Workbook wb) {
        Map<String, CellStyle> styles = new HashMap<>();
        CellStyle style = wb.createCellStyle();
        //创建标题样式
        Font headerFont = wb.createFont();
        headerFont.setFontHeightInPoints((short) 18);
        headerFont.setBold(true);
        headerFont.setColor(HSSFColor.BLACK.index);
        style = createBorderedStyle(wb);
        style.setAlignment(HorizontalAlignment.CENTER);
        //style.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
        style.setFont(headerFont);
        styles.put("header", style);
        //创建表头样式
        Font font1 = wb.createFont();
        font1.setBold(true);
        //font1.setCharSet(10);
        //font1.setFontHeight((short)20);
        font1.setColor(HSSFColor.BLACK.index);
        style = createBorderedStyle(wb);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setFont(font1);
        styles.put("title", style);

        Font font2 = wb.createFont();
        font2.setFontName("微软雅黑");
        style = createBorderedStyle(wb);
        style.setFont(font2);
        style.setAlignment(HorizontalAlignment.CENTER);
        styles.put("content", style);
        return styles;
    }

    public static int maxNum(int a, int b, int c) {
        b = a >= b ? a : b;
        c = b >= c ? b : c;
        return c;
    }

}
