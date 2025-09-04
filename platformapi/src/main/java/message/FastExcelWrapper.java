package message;

import cn.idev.excel.FastExcel;
import cn.idev.excel.util.ListUtils;
import entity.DemoData;

import java.util.Date;
import java.util.List;

public class FastExcelWrapper {
    public static void main(String[] args) {
        writeExcel(data());
    }

    private static List<DemoData> data() {
        List<DemoData> list = ListUtils.newArrayList();
        for (int i = 0; i < 10; i++) {
            DemoData data = new DemoData();
            data.setString("String" + i);
            data.setDate(new Date());
            data.setDoubleData(0.56);
            list.add(data);
        }
        return list;
    }

    private static void writeExcel(List<DemoData> list) {
        String fileName = "simpleWrite" + System.currentTimeMillis() + ".xlsx";

        FastExcel.write(fileName, DemoData.class)
                .sheet("Sheet1")
                .doWrite(data());
    }
}
