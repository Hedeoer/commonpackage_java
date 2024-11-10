public class TestInt {
    public static void main(String[] args) {
        System.out.println(processDouble(12.3));
        System.out.println(processDouble(12.0));
        System.out.println(processDouble(12.7));
    }

    public static long processDouble(double num) {
        // 检查是否有小数部分
        if (num % 1 != 0) {
            // 如果有小数部分，则向上取整
            return (long) Math.ceil(num);
        } else {
            // 如果没有小数部分，则取整数部分
            return (long) num;
        }
    }
}
