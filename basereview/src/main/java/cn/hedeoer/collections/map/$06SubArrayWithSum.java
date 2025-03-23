package cn.hedeoer.collections.map;

import java.util.HashMap;
import java.util.Map;

public class $06SubArrayWithSum {
    /*
    *
    Subarray with Given Sum – Handles Negative Numbers

    Given an unsorted array of integers, find a subarray that adds to a given number.
    If there is more than one subarray with the sum of the given number, print any of them.

    Input: arr[] = {1, 4, 20, 3, 10, 5}, sum = 33
    Output: Sum found between indexes 2 and 4
    Explanation: Sum of elements between indices 2 and 4 is 20 + 3 + 10 = 33


    Input: arr[] = {2, 12, -2, -20, 10}, sum = -10
    Output: Sum found between indexes 1 to 3
    Explanation: Sum of elements between indices 0 and 3 is 12 – 2 – 20 = -10


    Input: arr[] = {-10, 0, 2, -2, -20, 10}, sum = 20
    Output: No subarray with given sum exists
    Explanation: There is no subarray with the given sum
    *
    * */
    public static void main(String[] args) {

        int[] arr1 = {1, 4, 0, 0, 20, 3, 10, 5};
        //            0  1  2  3  4  5  6  7
        $06SubArrayWithSum.findSubArrayWithSum(arr1, 33);
        $06SubArrayWithSum.findSubArrayWithSumByFor(arr1, 33);
    }

    /**
     * 该算法基于一个重要观察：如果两个前缀和之差等于目标值，那么这两个位置之间的子数组之和就等于目标值。
     * 对于存在连续 0 的情况，目前取最短子数组 （0, 0, 20, 3, 10）和 （20, 3, 10）和都为33，但取 （20, 3, 10）
     * @param arr
     * @param target
     */
    private static void findSubArrayWithSum(int[] arr, int target) {
        System.out.println("\nSearching for subarray with sum: " + target);

        // 使用HashMap存储前缀和与其索引
        Map<Integer, Integer> prefixSumMap = new HashMap<>();
        int currentSum = 0;

        // 初始化：空子数组的和为0，索引位置为-1
        prefixSumMap.put(0, -1);

        for (int i = 0; i < arr.length; i++) {
            // 计算当前前缀和
            currentSum += arr[i];

            // 查找是否存在一个之前的前缀和，使得(当前和-之前和)等于目标值
            if (prefixSumMap.containsKey(currentSum - target)) {
                int startIndex = prefixSumMap.get(currentSum - target) + 1;
                int endIndex = i;

                System.out.println("Sum found between indexes " + startIndex + " and " + endIndex);

                // 打印子数组
                System.out.print("Subarray: [");
                for (int j = startIndex; j <= endIndex; j++) {
                    System.out.print(arr[j]);
                    if (j < endIndex) {
                        System.out.print(", ");
                    }
                }
                System.out.println("]");
                return;
            }

            // 存储当前前缀和及其位置
            prefixSumMap.put(currentSum, i);
        }

        System.out.println("No subarray with given sum exists");
    }


    /**
     * 使用 暴力遍历的方式匹配
     * @param arr
     * @param target
     */
    private static void findSubArrayWithSumByFor(int[] arr, int target) {
        int length = arr.length;
        // 外层循环推进子数组左边界
        for (int i = 0; i < arr.length; i++) {
            int tempSum = 0;
            // 内层循环推进子数组右边界
            for(int j = i; j < length; j++){
                tempSum += arr[j];
                // 右边界每推进一部，就判断当前子数组的和是否等于目标值
                if(tempSum == target){
                    System.out.println("Sum found between indexes " + i + " and " + j);
                    return;
                }
            }
        }
    }



}
