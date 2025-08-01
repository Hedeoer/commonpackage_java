import cn.hutool.core.convert.Convert;
import cn.hutool.core.convert.NumberChineseFormatter;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class NumberChineseFormatterTest {

    @Test
    public void formatThousandTest() {
        String f = NumberChineseFormatter.formatThousand(10, false);
        assertEquals("十", f);
        f = NumberChineseFormatter.formatThousand(11, false);
        assertEquals("十一", f);
        f = NumberChineseFormatter.formatThousand(19, false);
        assertEquals("十九", f);
    }

    // 测试千
    @Test
    public void formatThousandLongTest() {
        String f = NumberChineseFormatter.format(0, false);
        assertEquals("零", f);
        f = NumberChineseFormatter.format(1, false);
        assertEquals("一", f);
        f = NumberChineseFormatter.format(10, false);
        assertEquals("一十", f);
        f = NumberChineseFormatter.format(12, false);
        assertEquals("一十二", f);
        f = NumberChineseFormatter.format(100, false);
        assertEquals("一百", f);
        f = NumberChineseFormatter.format(101, false);
        assertEquals("一百零一", f);
        f = NumberChineseFormatter.format(110, false);
        assertEquals("一百一十", f);
        f = NumberChineseFormatter.format(112, false);
        assertEquals("一百一十二", f);
        f = NumberChineseFormatter.format(1000, false);
        assertEquals("一千", f);
        f = NumberChineseFormatter.format(1001, false);
        assertEquals("一千零一", f);
        f = NumberChineseFormatter.format(1010, false);
        assertEquals("一千零一十", f);
        f = NumberChineseFormatter.format(1100, false);
        assertEquals("一千一百", f);
        f = NumberChineseFormatter.format(1101, false);
        assertEquals("一千一百零一", f);
        f = NumberChineseFormatter.format(9999, false);
        assertEquals("九千九百九十九", f);
    }

    // 测试万
    @Test
    public void formatTenThousandLongTest() {
        String f = NumberChineseFormatter.format(1_0000, false);
        assertEquals("一万", f);
        f = NumberChineseFormatter.format(1_0001, false);
        assertEquals("一万零一", f);
        f = NumberChineseFormatter.format(1_0010, false);
        assertEquals("一万零一十", f);
        f = NumberChineseFormatter.format(1_0100, false);
        assertEquals("一万零一百", f);
        f = NumberChineseFormatter.format(1_1000, false);
        assertEquals("一万一千", f);
        f = NumberChineseFormatter.format(10_1000, false);
        assertEquals("一十万零一千", f);
        f = NumberChineseFormatter.format(10_0100, false);
        assertEquals("一十万零一百", f);
        f = NumberChineseFormatter.format(100_1000, false);
        assertEquals("一百万零一千", f);
        f = NumberChineseFormatter.format(100_0100, false);
        assertEquals("一百万零一百", f);
        f = NumberChineseFormatter.format(1000_1000, false);
        assertEquals("一千万零一千", f);
        f = NumberChineseFormatter.format(1000_0100, false);
        assertEquals("一千万零一百", f);
        f = NumberChineseFormatter.format(9999_0000, false);
        assertEquals("九千九百九十九万", f);
    }

    // 测试亿
    @Test
    public void formatHundredMillionLongTest() {
        String f = NumberChineseFormatter.format(1_0000_0000L, false);
        assertEquals("一亿", f);
        f = NumberChineseFormatter.format(1_0000_0001L, false);
        assertEquals("一亿零一", f);
        f = NumberChineseFormatter.format(1_0000_1000L, false);
        assertEquals("一亿零一千", f);
        f = NumberChineseFormatter.format(1_0001_0000L, false);
        assertEquals("一亿零一万", f);
        f = NumberChineseFormatter.format(1_0010_0000L, false);
        assertEquals("一亿零一十万", f);
        f = NumberChineseFormatter.format(1_0010_0000L, false);
        assertEquals("一亿零一十万", f);
        f = NumberChineseFormatter.format(1_0100_0000L, false);
        assertEquals("一亿零一百万", f);
        f = NumberChineseFormatter.format(1_1000_0000L, false);
        assertEquals("一亿一千万", f);
        f = NumberChineseFormatter.format(10_1000_0000L, false);
        assertEquals("一十亿零一千万", f);
        f = NumberChineseFormatter.format(100_1000_0000L, false);
        assertEquals("一百亿零一千万", f);
        f = NumberChineseFormatter.format(1000_1000_0000L, false);
        assertEquals("一千亿零一千万", f);
        f = NumberChineseFormatter.format(1100_1000_0000L, false);
        assertEquals("一千一百亿零一千万", f);
        f = NumberChineseFormatter.format(9999_0000_0000L, false);
        assertEquals("九千九百九十九亿", f);
    }

    // 测试万亿
    @Test
    public void formatTrillionsLongTest() {
        String f = NumberChineseFormatter.format(1_0000_0000_0000L, false);
        assertEquals("一万亿", f);
        f = NumberChineseFormatter.format(1_0000_1000_0000L, false);
        assertEquals("一万亿零一千万", f);
        f = NumberChineseFormatter.format(1_0010_0000_0000L, false);
        assertEquals("一万零一十亿", f);
    }

    @Test
    public void formatTest() {
        String f0 = NumberChineseFormatter.format(5000_8000, false);
        assertEquals("五千万零八千", f0);
        String f1 = NumberChineseFormatter.format(1_0889.72356, false);
        assertEquals("一万零八百八十九点七二", f1);
        f1 = NumberChineseFormatter.format(12653, false);
        assertEquals("一万二千六百五十三", f1);
        f1 = NumberChineseFormatter.format(215.6387, false);
        assertEquals("二百一十五点六四", f1);
        f1 = NumberChineseFormatter.format(1024, false);
        assertEquals("一千零二十四", f1);
        f1 = NumberChineseFormatter.format(100350089, false);
        assertEquals("一亿零三十五万零八十九", f1);
        f1 = NumberChineseFormatter.format(1200, false);
        assertEquals("一千二百", f1);
        f1 = NumberChineseFormatter.format(12, false);
        assertEquals("一十二", f1);
        f1 = NumberChineseFormatter.format(0.05, false);
        assertEquals("零点零五", f1);
    }

    @Test
    public void formatTest2() {
        String f1 = NumberChineseFormatter.format(-0.3, false, false);
        assertEquals("负零点三", f1);

        f1 = NumberChineseFormatter.format(10, false, false);
        assertEquals("一十", f1);
    }

    @Test
    public void formatTest3() {
//		String f1 = NumberChineseFormatter.format(5000_8000, false, false);
//		assertEquals("五千万零八千", f1);

        String f2 = NumberChineseFormatter.format(1_0035_0089, false, false);
        assertEquals("一亿零三十五万零八十九", f2);
    }

    @Test
    public void formatMaxTest() {
        String f3 = NumberChineseFormatter.format(99_9999_9999_9999L, false, false);
        assertEquals("九十九万九千九百九十九亿九千九百九十九万九千九百九十九", f3);
    }

    @Test
    public void formatTraditionalTest() {
        String f1 = NumberChineseFormatter.format(10889.72356, true);
        assertEquals("壹万零捌佰捌拾玖点柒贰", f1);
        f1 = NumberChineseFormatter.format(12653, true);
        assertEquals("壹万贰仟陆佰伍拾叁", f1);
        f1 = NumberChineseFormatter.format(215.6387, true);
        assertEquals("贰佰壹拾伍点陆肆", f1);
        f1 = NumberChineseFormatter.format(1024, true);
        assertEquals("壹仟零贰拾肆", f1);
        f1 = NumberChineseFormatter.format(100350089, true);
        assertEquals("壹亿零叁拾伍万零捌拾玖", f1);
        f1 = NumberChineseFormatter.format(1200, true);
        assertEquals("壹仟贰佰", f1);
        f1 = NumberChineseFormatter.format(12, true);
        assertEquals("壹拾贰", f1);
        f1 = NumberChineseFormatter.format(0.05, true);
        assertEquals("零点零伍", f1);
    }

    @Test
    public void formatSimpleTest() {
        String f1 = NumberChineseFormatter.formatSimple(1_2345);
        assertEquals("1.23万", f1);
        f1 = NumberChineseFormatter.formatSimple(-5_5555);
        assertEquals("-5.56万", f1);
        f1 = NumberChineseFormatter.formatSimple(1_2345_6789);
        assertEquals("1.23亿", f1);
        f1 = NumberChineseFormatter.formatSimple(-5_5555_5555);
        assertEquals("-5.56亿", f1);
        f1 = NumberChineseFormatter.formatSimple(1_2345_6789_1011L);
        assertEquals("1.23万亿", f1);
        f1 = NumberChineseFormatter.formatSimple(-5_5555_5555_5555L);
        assertEquals("-5.56万亿", f1);
        f1 = NumberChineseFormatter.formatSimple(123);
        assertEquals("123", f1);
        f1 = NumberChineseFormatter.formatSimple(-123);
        assertEquals("-123", f1);
    }

    @Test
    public void digitToChineseTest() {
        String digitToChinese = Convert.digitToChinese(12_4124_1241_2421.12);
        assertEquals("壹拾贰万肆仟壹佰贰拾肆亿壹仟贰佰肆拾壹万贰仟肆佰贰拾壹元壹角贰分", digitToChinese);

        digitToChinese = Convert.digitToChinese(12_0000_1241_2421L);
        assertEquals("壹拾贰万亿零壹仟贰佰肆拾壹万贰仟肆佰贰拾壹元整", digitToChinese);

        digitToChinese = Convert.digitToChinese(12_0000_0000_2421L);
        assertEquals("壹拾贰万亿零贰仟肆佰贰拾壹元整", digitToChinese);

        digitToChinese = Convert.digitToChinese(12_4124_1241_2421D);
        assertEquals("壹拾贰万肆仟壹佰贰拾肆亿壹仟贰佰肆拾壹万贰仟肆佰贰拾壹元整", digitToChinese);

        digitToChinese = Convert.digitToChinese(2421.02);
        assertEquals("贰仟肆佰贰拾壹元零贰分", digitToChinese);
    }

    @Test
    public void digitToChineseTest2() {
        double a = 67556.32;
        String digitUppercase = Convert.digitToChinese(a);
        assertEquals("陆万柒仟伍佰伍拾陆元叁角贰分", digitUppercase);

        a = 1024.00;
        digitUppercase = Convert.digitToChinese(a);
        assertEquals("壹仟零贰拾肆元整", digitUppercase);

        double b = 1024;
        digitUppercase = Convert.digitToChinese(b);
        assertEquals("壹仟零贰拾肆元整", digitUppercase);
    }

    @Test
    public void digitToChineseTest3() {
        String digitToChinese = Convert.digitToChinese(2_0000_0000.00);
        assertEquals("贰亿元整", digitToChinese);
        digitToChinese = Convert.digitToChinese(2_0000.00);
        assertEquals("贰万元整", digitToChinese);
        digitToChinese = Convert.digitToChinese(2_0000_0000_0000.00);
        assertEquals("贰万亿元整", digitToChinese);
    }

    @Test
    public void digitToChineseTest4() {
        String digitToChinese = Convert.digitToChinese(400_0000.00);
        assertEquals("肆佰万元整", digitToChinese);
    }

    @Test
    public void numberCharToChineseTest() {
        String s = NumberChineseFormatter.numberCharToChinese('1', false);
        assertEquals("一", s);
        s = NumberChineseFormatter.numberCharToChinese('2', false);
        assertEquals("二", s);
        s = NumberChineseFormatter.numberCharToChinese('0', false);
        assertEquals("零", s);

        // 非数字字符原样返回
        s = NumberChineseFormatter.numberCharToChinese('A', false);
        assertEquals("A", s);
    }

    @Test
    public void chineseToNumberTest() {
        assertEquals(0, NumberChineseFormatter.chineseToNumber("零"));
        assertEquals(102, NumberChineseFormatter.chineseToNumber("一百零二"));
        assertEquals(112, NumberChineseFormatter.chineseToNumber("一百一十二"));
        assertEquals(1012, NumberChineseFormatter.chineseToNumber("一千零一十二"));
        assertEquals(1000000, NumberChineseFormatter.chineseToNumber("一百万"));
        assertEquals(2000100112, NumberChineseFormatter.chineseToNumber("二十亿零一十万零一百一十二"));
    }

    @Test
    public void chineseToNumberTest2() {
        assertEquals(120, NumberChineseFormatter.chineseToNumber("一百二"));
        assertEquals(1200, NumberChineseFormatter.chineseToNumber("一千二"));
        assertEquals(22000, NumberChineseFormatter.chineseToNumber("两万二"));
        assertEquals(22003, NumberChineseFormatter.chineseToNumber("两万二零三"));
        assertEquals(22010, NumberChineseFormatter.chineseToNumber("两万二零一十"));
    }

    @Test
    public void chineseToNumberTest3() {
        // issue#1726，对于单位开头的数组，默认赋予1
        // 十二 -> 一十二
        // 百二 -> 一百二
        assertEquals(12, NumberChineseFormatter.chineseToNumber("十二"));
        assertEquals(120, NumberChineseFormatter.chineseToNumber("百二"));
        assertEquals(1300, NumberChineseFormatter.chineseToNumber("千三"));
    }

    @Test
    public void badNumberTest() {
        assertThrows(IllegalArgumentException.class, () -> {
            // 连续数字检查
            NumberChineseFormatter.chineseToNumber("一百一二三");
        });
    }

    @Test
    public void badNumberTest2() {
        assertThrows(IllegalArgumentException.class, () -> {
            // 非法字符
            NumberChineseFormatter.chineseToNumber("一百你三");
        });
    }

    @Test
    public void singleMoneyTest() {
        String format = NumberChineseFormatter.format(0.01, false, true);
        assertEquals("一分", format);
        format = NumberChineseFormatter.format(0.10, false, true);
        assertEquals("一角", format);
        format = NumberChineseFormatter.format(0.12, false, true);
        assertEquals("一角二分", format);

        format = NumberChineseFormatter.format(1.00, false, true);
        assertEquals("一元整", format);
        format = NumberChineseFormatter.format(1.10, false, true);
        assertEquals("一元一角", format);
        format = NumberChineseFormatter.format(1.02, false, true);
        assertEquals("一元零二分", format);
    }

    @Test
    public void singleNumberTest() {
        String format = NumberChineseFormatter.format(0.01, false, false);
        assertEquals("零点零一", format);
        format = NumberChineseFormatter.format(0.10, false, false);
        assertEquals("零点一", format);
        format = NumberChineseFormatter.format(0.12, false, false);
        assertEquals("零点一二", format);

        format = NumberChineseFormatter.format(1.00, false, false);
        assertEquals("一", format);
        format = NumberChineseFormatter.format(1.10, false, false);
        assertEquals("一点一", format);
        format = NumberChineseFormatter.format(1.02, false, false);
        assertEquals("一点零二", format);
    }

    @Test
    public void dotTest() {
        final String format = NumberChineseFormatter.format(new BigDecimal("3.1415926"), false, false);
        assertEquals("三点一四一五九二六", format);
    }

    @Test
    public void dotTest2() {
//        String number = "166375.00万元";
//        String number = "伍仟零捌万";
//        int res = NumberChineseFormatter.chineseToNumber(number);
//        assertEquals(50080000, res);


//        String number = "壹佰元整";
//        BigDecimal res = NumberChineseFormatter.chineseMoneyToNumber(number);
//        System.out.println(res);

//        String number = "叁拾万零伍佰元";
//        BigDecimal res = NumberChineseFormatter.chineseMoneyToNumber(number);
//        System.out.println(res);

        // 陆万柒仟伍佰伍拾柒元
        // 贰拾叁亿贰仟捌佰叁拾捌万贰仟叁佰肆拾贰元
        String number = "陆万柒仟伍佰伍拾柒元";
        BigDecimal res = NumberChineseFormatter.chineseMoneyToNumber(number);
        // --1966584954.00
        System.out.println(res);
        System.out.println(Convert.chineseMoneyToNumber(number));

//        String number = "贰万叁万贰佰肆拾伍元整";
//        BigDecimal res = NumberChineseFormatter.chineseMoneyToNumber(number);
//        // 50245
//        System.out.println(res.intValue());
    }

    @Test
    public void badChineseMoneyTest() {
        // 整形溢出
        String number = "贰拾叁亿贰仟捌佰叁拾捌万贰仟叁佰肆拾贰元";
        /*
        * chineseMoneyToNumber方法底层使用的chineseToNumber方法，最终结果使用int存储（21 4748 3647）
        * 如果数字超过这个范围，则会錯誤
        * 解决办法是重构chineseToNumber方法，使用BigDecimal来处理大数字
        * // 推荐使用 python库cn2an
        * */
        BigDecimal res = NumberChineseFormatter.chineseMoneyToNumber(number);
        // --1966584954.00
        System.out.println(res);


    }
}