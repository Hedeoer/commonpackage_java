package cn.hedeoer.collections.map;

import java.util.HashMap;
import java.util.HashSet;

public class $05DistanceMax {
/*
Max Distance Between Two Occurrences

Given an array arr[], the task is to find the maximum distance between two occurrences of any element.
If no element occurs twice, return 0.

Input: arr = [1, 1, 2, 2, 2, 1]
Output: 5
Explanation: distance for 1 is: 5-0 = 5, distance for 2 is: 4-2 = 2, So max distance is 5.


Input : arr[] = [3, 2, 1, 2, 1, 4, 5, 8, 6, 7, 4, 2]
Output: 10
Explanation : Max distance for 2 is 11-1 = 10, max distance for 1 is 4-2 = 2 and max distance for 4 is 10-5 = 5


Input: arr[] = [1, 2, 3, 6, 5, 4]
Output: 0
Explanation: No element has two occurrence, so maximum distance = 0.
* */
    public static void main(String[] args) {

        int[] arr1 = {1, 1, 2, 2, 2, 1};
        int[] arr2 = {3, 2, 1, 2, 1, 4, 5, 8, 6, 7, 4, 2};
        int[] arr3 = {1, 2, 3, 6, 5, 4};
        System.out.println($05DistanceMax.getDistanceMax(arr1));
        System.out.println($05DistanceMax.getDistanceMax(arr2));
        System.out.println($05DistanceMax.getDistanceMax(arr3));

        System.out.println($05DistanceMax.getDistanceMaxByMap(arr1));
        System.out.println($05DistanceMax.getDistanceMaxByMap(arr2));
        System.out.println($05DistanceMax.getDistanceMaxByMap(arr3));
    }

    private static Integer getDistanceMax(int[] arr ) {

        int max = 0;
        // 数组转字符串
        StringBuilder sb = new StringBuilder();
        // 去重数组元素
        HashSet<String> disSet = new HashSet<>();
        for (int ele : arr) {
            sb.append(ele);
            disSet.add(ele + "" );
        }

        String str = sb.toString();
        for (String ele : disSet) {
            // 获取元素第一次出现的下标和最后一次出现的下标
            int firstOccurIndex = str.indexOf(ele);
            int lastOccurIndex = str.lastIndexOf(ele);
            int distance = Math.abs(firstOccurIndex - lastOccurIndex);
            // 比较最大距离
            if(distance > max) {
                max = distance;
            }
        }

        return max;
    }

    /**
     * 使用map
     * @param arr
     * @return
     */
    private static Integer getDistanceMaxByMap(int[] arr) {
        int max = 0;
        // <数组元素，元素在数组中的索引编号>
        HashMap<Integer, Integer> map = new HashMap<>();

        for (int index = 0 ; index < arr.length ; index++) {
            if(! map.containsKey(arr[index])){
                map.put(arr[index], index);
            }else{
                max = Math.max(max, index - map.get(arr[index]));
            }
        }

        return max;
    }
}
