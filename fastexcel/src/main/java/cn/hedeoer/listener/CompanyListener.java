package cn.hedeoer.listener;

import cn.hedeoer.pojo.Company;
import cn.idev.excel.context.AnalysisContext;
import cn.idev.excel.context.xlsx.DefaultXlsxReadContext;
import cn.idev.excel.read.listener.ReadListener;
import cn.idev.excel.read.metadata.ReadSheet;
import cn.idev.excel.read.metadata.holder.ReadSheetHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据监听器 company
 */
public class CompanyListener implements ReadListener<Company> {

    /**
     * one row value.
     * 每行数据调用一次
     * @param company    one row value. It is same as {@link AnalysisContext#readRowHolder()}
     * @param analysisContext analysis context
     */
    @Override
    public void invoke(Company company, AnalysisContext analysisContext) {
        // 是否为xlsx文件
        if (analysisContext instanceof DefaultXlsxReadContext) {
            DefaultXlsxReadContext context = (DefaultXlsxReadContext) analysisContext;
            ReadSheetHolder readSheetHolder = context.readSheetHolder();
            // 获取sheet名称
            String sheetName = readSheetHolder.getSheetName();
            System.out.println(company);

        }
    }

    /**
     * all data read finish.
     * 所有数据解析完成后调用一次
     * @param analysisContext analysis context
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        if(analysisContext instanceof DefaultXlsxReadContext){
            DefaultXlsxReadContext context = (DefaultXlsxReadContext) analysisContext;
            // 获取总行数
            Integer totalCount = context.getTotalCount();
            System.out.println("总行数 = " + totalCount);

        }
    }


}
