package cn.hedeoer.collections.map;

import java.util.*;

public class $02ArraysEqual {
    public static void main(String[] args) {
/*
Check if two arrays are equal or not

给定两个长度相等的数组 a 和 b。任务是判断给定的数组是否相等。如果符合以下条件，则认为两个数组相等：
- 两个数组包含相同的元素集。
- 元素的排列（或排列组合）可能不同。
- 如果有重复元素，则两个数组中每个元素的计数必须相同。

Input: a[] = [1, 2, 5, 4, 0], b[] = [2, 4, 5, 0, 1]
Output: true

Input: a[] = [1, 2, 5, 4, 0, 2, 1], b[] = [2, 4, 5, 0, 1, 1, 2]
Output: true

 Input: a[] = [1, 7, 1], b[] = [7, 7, 1]
Output: false
* */

        int[] a = {1, 7, 1};
        int[] b = {1, 7, 1};
        System.out.println($02ArraysEqual.isEqual(a, b));

        int[] c = {1, 2, 5, 4, 0};
        int[] d = {2, 4, 5, 0, 1};
        System.out.println($02ArraysEqual.isEqual(c, d));

        int[] e = {1, 2, 5, 4, 0, 2, 1};
        int[] f = {2, 4, 5, 0, 1, 2,1};
        System.out.println($02ArraysEqual.isEqual(e, f));

        int[] g = {1, 2, 5};
        int[] h = {6,7};
        System.out.println($02ArraysEqual.isEqual(g, h));


    }


    /**
     * 利用hashmap 比较两个数组是否相等
     * @param a
     * @param b
     * @return
     */
    private static boolean isEqualByHashMap(int[] a, int[] b) {
        // 忽略空值情况处理
        if(a.length != b.length)  return false;


        HashMap<Integer, Integer> map1 = new HashMap<>();
        HashMap<Integer, Integer> map2 = new HashMap<>();

        for (int ele : a) {
            map1.put(ele, map1.getOrDefault(ele, 0) + 1);
        }
        for (int ele : b) {
            map2.put(ele, map2.getOrDefault(ele, 0) + 1);

        }
        // hashmap 比较 1. map大小 2. map中每个entrySet是否相同
        return map1.equals(map2);
    }

    /**
     * 利用排序比较两个数组是否相等
     * @param a
     * @param b
     * @return
     */
    private static boolean isEqual(int[] a, int[] b) {
        boolean result = (a.length == b.length);
        // 忽略空值情况处理

        Arrays.sort(a);
        Arrays.sort(b);

        for (int index = 0; index < a.length; index++) {
            if (a[index] != b[index]) {
                result = false;
                break;
            }
        }
        return result;
    }

}
