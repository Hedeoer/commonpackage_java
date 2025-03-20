package cn.hedeoer.collections.map;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class $03ElementMiss {
    public static void main(String[] args) {
    /*
    *
    * Find missing elements of a range
    *
    * Given an array, arr[0..n-1] of distinct elements and a range [low, high],
    * find all numbers that are in a range, but not the array.
    * The missing elements should be printed in sorted order.
    *
    *
    Input: arr[] = {10, 12, 11, 15},
           low = 10, high = 15
    Output: 13, 14

    Input: arr[] = {1, 14, 11, 51, 15},
           low = 50, high = 55
    Output: 50, 52, 53, 54 55
    *
    *
    * */
        int[] a = {10, 12, 11, 15};
        int[] b = {1, 14, 11, 51, 15};
//        List<Integer> missingElement = $03ElementMiss.findMissingElement(a, 10, 15);
//        System.out.println(missingElement);
//
//        List<Integer> missingElement2 = $03ElementMiss.findMissingElement(b, 100, 103);
//        System.out.println(missingElement2);

//        $03ElementMiss.findMissingElementByMark(a, 10, 15);
//        $03ElementMiss.findMissingElementByMark(b, 100, 103);

        $03ElementMiss.findMissingElementByHashing(a, 10, 15);
        $03ElementMiss.findMissingElementByHashing(b, 100, 103);
    }

    /**
     * 找到数组中缺失的元素
     * 思路：
     * 1. 对数组进行排序
     * 2. 找到数组中的最小值和最大值
     * 3. 比较最小值和最大值和low和high的大小，找出缺失的元素
     * @param originalArr
     * @param low
     * @param high
     * @return
     */
    private static List<Integer> findMissingElement(int[] originalArr, Integer low, Integer high){
        // 跳过非空判断

        ArrayList<Integer> result = new ArrayList<>();
        Arrays.sort(originalArr);
        int arrMin = originalArr[0];
        int arrMax = originalArr[originalArr.length - 1];
        // 如果数组的最大值小于low 或者 数组的最小值大于high 则直接[low, high]的列表
        if(low > arrMax || high < arrMin){
            for(int start = low; start <= high; start++){
                result.add(start);
            }
            return result;
        }

        // originalArr 中[arrMin,arrMax] 和 【low,high】存在区间交集
        for(int start = low; start <= high; start++){
            int binarySearch = Arrays.binarySearch(originalArr, start);
            // 如果不存在则加入结果集
            if(binarySearch < 0){
                result.add(start);
            }
        }
        return result;
    }

    /**
     * 利用标记法，如果将数组中的元素在【low，high】范围，标记存在，然后遍历【low，high】范围内的数组，找到未标记的的元素，即为缺失的元素
     * @param originalArr
     * @param low
     * @param high
     * @return
     */
    private static List<Integer> findMissingElementByMark(int[] originalArr, Integer low, Integer high){
        ArrayList<Integer> result = new ArrayList<>();
        // 存在标记为true，不存在标记为false
        boolean[] marks = new boolean[high - low + 1];
        for (int index = 0; index < originalArr.length; index++) {
            if(originalArr[index] >= low && originalArr[index] <= high){
                marks[index] = true;
            }
        }

        for (int index = 0; index < high - low + 1; index++) {
            // 如果不存在则加入结果集
            if(!marks[index]){
                result.add(index + low);
            }
        }
        System.out.println(result);
        return result;
    }

    /**
     * 利用hashset 找到数组中缺失的元素
     * @param originalArr
     * @param low
     * @param high
     * @return
     */
    private static List<Integer> findMissingElementByHashing(int[] originalArr, Integer low, Integer high){
        ArrayList<Integer> result = new ArrayList<>();
        HashSet<Integer> disSet = new HashSet<>();
        for (int i = 0; i < originalArr.length; i++) {
            disSet.add(originalArr[i]);
        }

        for (int i = low; i <= high; i++) {
            if(!disSet.contains(i)){
                result.add(i);
            }
        }
        System.out.println(result);
        return result;
    }
}
