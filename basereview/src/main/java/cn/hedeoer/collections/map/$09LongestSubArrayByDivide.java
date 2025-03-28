package cn.hedeoer.collections.map;

import java.util.*;

public class $09LongestSubArrayByDivide {
    /*
    *
    Longest Subarray With Sum Divisible By K
    *
    Given an arr[] containing n integers and a positive integer k,
    he problem is to find the longest subarray’s length with the sum of the elements divisible by k.

    Input: arr[] = [2, 7, 6, 1, 4, 5], k = 3
    Output: 4
    Explanation: The subarray [7, 6, 1, 4] has sum = 18, which is divisible by 3.


    Input: arr[] = [-2, 2, -5, 12, -11, -1, 7], k = 3
    Output: 5
    Explanation: The subarrays [2, -5, 12] and [12, -11, -1] has sum = -3, which is divisible by 3.


    Input: arr[] = [1, 2, -2], k = 5
    Output: 2
    Explanation: The subarray is [2, -2] with sum = 0, which is divisible by 2.
    *
    * */
    public static void main(String[] args) {

        int[] arr1 = {2, 7, 6, 1, 4, 5};
        int[] arr2 = {-2, 2, -5, 12, -11, -1, 7};
        int[] arr3 = {1, 2, -2};

        $09LongestSubArrayByDivide.getLongestSubArrayByDivide(arr1, 3);
        $09LongestSubArrayByDivide.getLongestSubArrayByDivide(arr2, 3);
        $09LongestSubArrayByDivide.getLongestSubArrayByDivide(arr3, 5);

    }

    /**
     * 对所有子数组进行迭代.
     * 为了避免在计算子数组总和时出现溢出，我们可以跟踪 (subarray sum % k)，
     * 如果 (subarray sum % k) 的结果为 0，我们就可以使用它的长度找到能被 k 整除的最长子数组
     * @param arr
     * @param k
     */
    private static void getLongestSubArrayByDivide(int[] arr, int k) {

        int res = 0;
        for (int i = 0; i < arr.length; i++) {
            int sum = 0;
            for (int j = i; j < arr.length; j++) {
                sum += arr[j];
                if(sum % k == 0){
                    res = Math.max(res, j - i + 1);
                }
            }
        }

        System.out.println(res);

    }
}
