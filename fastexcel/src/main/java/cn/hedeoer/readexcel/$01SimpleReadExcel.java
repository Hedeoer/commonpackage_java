package cn.hedeoer.readexcel;

import cn.hedeoer.listener.CompanyListener;
import cn.hedeoer.pojo.Company;
import cn.idev.excel.ExcelReader;
import cn.idev.excel.FastExcel;
import cn.idev.excel.context.AnalysisContext;
import cn.idev.excel.context.xlsx.DefaultXlsxReadContext;
import cn.idev.excel.read.builder.ExcelReaderSheetBuilder;
import cn.idev.excel.read.listener.PageReadListener;
import cn.idev.excel.read.listener.ReadListener;
import cn.idev.excel.read.metadata.ReadSheet;
import cn.idev.excel.read.metadata.holder.ReadSheetHolder;
import cn.idev.excel.read.metadata.holder.xlsx.XlsxReadWorkbookHolder;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


public class $01SimpleReadExcel {
    public static void main(String[] args) {


        String fileName = "C:\\Users\\H\\Desktop\\fast\\阿克苏市场主体.xlsx";



        // 使用方式1：Lambda表达式直接处理数据
/*
        AtomicInteger num = new AtomicInteger(0);
        FastExcel.read(fileName, Company.class, new PageReadListener<>(dataList -> {
            for (Object o : dataList) {
                if (o instanceof Company) {
                    num.getAndIncrement();
                    Company company = (Company) o;
                    System.out.println(company);
                }
            }
        }))
                // 读取第一个 Sheet
                .sheet()
                // This Sheet has two row head ,since the third row is the data
                .headRowNumber(2)
                // 读取前100行
                .numRows(100)
                .doRead();
        System.out.println(num);
*/

        // 使用方式2：匿名内部类
/*        FastExcel.read(fileName, Company.class, new ReadListener<Company>() {
            @Override
            public void invoke(Company data, AnalysisContext context) {
                if (context instanceof DefaultXlsxReadContext) {
                    DefaultXlsxReadContext con = (DefaultXlsxReadContext) context;
                    ReadSheetHolder readSheetHolder = con.readSheetHolder();
                    String sheetName = readSheetHolder.getSheetName();
                    System.out.println(data);

                }
            }
            @Override
            public void doAfterAllAnalysed(AnalysisContext context) {
                if(context instanceof DefaultXlsxReadContext){
                    DefaultXlsxReadContext con = (DefaultXlsxReadContext) context;
                    Integer totalCount = con.getTotalCount();
                    System.out.println("totalCount = " + totalCount);
                }
            }
        })
                .sheet()
                // This Sheet has two row head ,since the third row is the data
                .headRowNumber(2)
                // 读取前100行
                .numRows(100)
                .doRead();*/

        // 使用方式3：自定义监听器
/*        FastExcel.read(fileName, Company.class, new CompanyListener())
                // 读取第一个 Sheet
                .sheet()
                // This Sheet has two row head ,since the third row is the data
                .headRowNumber(2)
                // 读取前100行
                .numRows(100)
                .doRead();*/



        // 使用方式4：多 Sheet 读取
        // 读取全部 Sheet
        FastExcel.read(fileName, Company.class, new CompanyListener())
                // This Sheet has two row head ,since the third row is the data
                .headRowNumber(2)
                // 读取前100行
                .numRows(100)
                .doReadAll();

//        FastExcel.read(fileName, MultipleSheetsData.class, new MultipleSheetsListener()).doReadAll();

        // 读取指定 Sheet
       /* try (ExcelReader excelReader = FastExcel.read(fileName).build()) {
            ReadSheet sheet1 = FastExcel.readSheet(0).head(Company.class).registerReadListener(new CompanyListener()).build();
            ReadSheet sheet2 = FastExcel.readSheet(1).head(Company.class).registerReadListener(new CompanyListener()).build();
            excelReader.read(sheet1, sheet2);
        }*/
    }
}
