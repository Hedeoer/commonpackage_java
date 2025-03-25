package cn.hedeoer.collections.map;

import java.util.Arrays;
import java.util.HashSet;

public class $08TripletSumArray {
/*

Sum – Triplet Sum in Array

Given an array arr[] of size n and an integer sum,
the task is to check if there is a triplet in the array which sums up to the given target sum.
给定一个大小为 n 的数组 arr[] 和一个整数总和，任务是检查数组中是否存在总和等于给定目标总和的三元组
Input: arr[] = [1, 4, 45, 6, 10, 8], target = 13
Output: true
Explanation: The triplet [1, 4, 8] sums up to 13


Input: arr[] = [1, 2, 4, 3, 6, 7], target = 10
Output: true
Explanation: The triplets [1, 3, 6] and [1, 2, 7] both sum to 10.


Input: arr[] = [40, 20, 10, 3, 6, 7], sum = 24
Output: false
Explanation: No triplet in the array sums to 24.


* */
    public static void main(String[] args) {

        int arr1 [] = new int[]{1, 4, 45, 6, 10, 8};
        $08TripletSumArray.findTripletsByFor(arr1, 13);
        $08TripletSumArray.findTripletsByTwoPointer(arr1, 13);
        $08TripletSumArray.findTripletsByHashSet(arr1, 13);

    }

    public static void findTripletsByFor(int[] arr, int target) {
        boolean found = false;

        // 第一个数 范围（0,length-2）
        for (int i = 0; i < arr.length - 2 ; i++) {
            // 第二个数 范围（i+1,length-1）
            for(int j = i + 1; j < arr.length - 1; j++){
                // 第三个数 范围（j+1,length）
                for(int k = j + 1; k < arr.length; k++){
                    if(arr[i] + arr[j] + arr[k] == target){
                        found = true;
                        break;
                    }
                }
            }
        }
        System.out.println(found);
    }

    /**
     * 利用双指针
     * @param arr
     * @param target
     */
    public static void findTripletsByTwoPointer(int[] arr, int target) {
        boolean found = false;
        Arrays.sort(arr);

        // 第一个数 （ 0， length-2 ）
        for (int i = 0; i < arr.length - 2; i++) {
            // Initialize left and right pointers with
            // start and end of remaining subarray
            int l = i + 1;
            int r = arr.length - 1;
            int requireSum = target -  arr[i];
            while(l < r){
                if(arr[l] + arr[r] == requireSum){
                    found = true;
                    break;
                }else if(arr[l] + arr[r] < requireSum){
                    l++;
                }else{
                    r--;
                }
            }

        }
        System.out.println(found);


    }

    private static void findTripletsByHashSet(int[] arr, int target) {

        boolean found = false;
        // Hash set to store potential second elements
        HashSet<Integer> secondNumSet = new HashSet<>();
        for (int i = 0; i < arr.length - 2; i++) {

            // Fix the third element as arr[j]
            for(int j = i + 1; j < arr.length ; j++){
                // Search for second element in hash set
                if(secondNumSet.contains(target - arr[i] - arr[j])){
                    found = true;
                    break;
                }
                // Add arr[j] as a potential second element
                secondNumSet.add(arr[j]);
            }
        }
        System.out.println(found);
    }
}
