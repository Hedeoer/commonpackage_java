package cn.hedeoer.collections.map;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class $04PairsCount {
    /*
    Sum – Count pairs with given sum

    Input: arr[] = {1, 5, 7, -1, 5}, target = 6
    Output:  3
    Explanation: Pairs with sum 6 are (1, 5), (7, -1) & (1, 5).


    Input: arr[] = {1, 1, 1, 1}, target = 2
    Output:  6
    Explanation: Pairs with sum 2 are (1, 1), (1, 1), (1, 1), (1, 1), (1, 1) and (1, 1).


    Input: arr[] = {10, 12, 10, 15, -1}, target = 125
    Output:  0
    * */
    public static void main(String[] args) {


        int[] arr1 = {1, 5, 7, -1, 5};
        int[] arr2 = {1, 1, 1, 1};
        int[] arr3 = {10, 12, 10, 15, -1};
//        System.out.println($04PairsCount.getParisCountByForEach(arr1, 6));
//        System.out.println($04PairsCount.getParisCountByForEach(arr2, 2));
//        System.out.println($04PairsCount.getParisCountByForEach(arr3, 125));

//        System.out.println($04PairsCount.getParisCountBySort(arr1, 6));
//        System.out.println($04PairsCount.getParisCountBySort(arr2, 2));
//        System.out.println($04PairsCount.getParisCountBySort(arr3, 125));

        System.out.println($04PairsCount.getParisCountByMap(arr1, 6));
        System.out.println($04PairsCount.getParisCountByMap(arr2, 2));
        System.out.println($04PairsCount.getParisCountByMap(arr3, 125));

    }

    /**
     * 找出所有组合数的和，暴力破解
     * @param arr 数组
     * @param target 目标和
     * @return 组合数
     */
    private static Integer getParisCountByForEach(int[] arr, Integer target) {
        if (arr.length < 2) {
            return null;
        }
        Integer count = 0;
        // map中每个《key，value》== 》 <(0,1),1> KEY为两个以逗号分隔形成的字符串，value为两个组合数的和
        HashMap<String, Integer> sequenceSumMap = new HashMap<>();

        for (int i = 0; i < arr.length - 1; i++) {
            for(int j = i + 1; j < arr.length; j++) {
                Integer sum = arr[i] + arr[j];
                String key = arr[i] + String.valueOf(arr[j]);
                sequenceSumMap.put(key, sum);
                if(sum.equals(target)) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * 排序后，使用双指针
     * @param arr
     * @param target
     * @return
     */
    private static Integer getParisCountBySort(int[] arr, Integer target) {
        if (arr.length < 2) {return null;}

        Arrays.sort(arr);
        Integer count = 0;
        int left = 0;
        int right = arr.length - 1;

        while(left < right){
            Integer sum = arr[left] + arr[right];
            if(sum.equals(target)) {
                int cnt1 = 0, cnt2 = 0;
                int ele1 = arr[left], ele2 = arr[right];

                // Count frequency of first element of the pair
                while (left <= right && arr[left] == ele1) {
                    left++;
                    cnt1++;
                }

                // Count frequency of second element of the pair
                while (left <= right && arr[right] == ele2) {
                    right--;
                    cnt2++;
                }

                // If both the elements are same, then count of
                // pairs = the number of ways to choose 2
                // elements among cnt1 elements
                // [3,3,3] 要取得6，有3种组合
                if (ele1 == ele2)
                    count += (cnt1 * (cnt1 - 1)) / 2;

                    // If the elements are different, then count of
                    // pairs = product of the count of both elements
                else
                    // [1,1,5,5,5] 要取得6，有2*3种组合
                    count += (cnt1 * cnt2);
            }else if(sum > target){
                right--;
            }else{
                left++;
            }

        }
        return count;
    }

    private static Integer getParisCountByMap(int[] arr, Integer target) {


        Map<Integer, Integer> freq = new HashMap<>();
        int cnt = 0;

        for (int i = 0; i < arr.length; i++) {

            // Check if the complement (target - arr[i])
            // exists in the map. If yes, increment count
            if (freq.containsKey(target - arr[i])) {
                cnt += freq.get(target - arr[i]);
            }

            // Increment the frequency of arr[i]
            freq.put(arr[i],
                    freq.getOrDefault(arr[i], 0) + 1);
        }
        return cnt;
    }
}
