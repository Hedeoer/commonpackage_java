package cn.hedeoer.collections.map;

import java.util.*;

public class $01WindowK {

    public static void main(String[] args) {
        /*
        Count Distinct Elements In Every Window of Size K
        Input: arr[] = [1, 2, 1, 3, 4, 2, 3], k = 4
        Output: [3, 4, 4, 3]
        Explanation: First window is [1, 2, 1, 3], count of distinct numbers is 3.
                     Second window is [2, 1, 3, 4] count of distinct numbers is 4.
                     Third window is [1, 3, 4, 2] count of distinct numbers is 4.
                     Fourth window is [3, 4, 2, 3] count of distinct numbers is 3.


        Input: arr[] = [4, 1, 1], k = 2
        Output: [2, 1]
        Explanation: First window is [4, 1], count of distinct numbers is 2.
                     Second window is [1, 1], count of distinct numbers is 1.

        如何模拟滑动窗口 for循环
        如何对窗口内的元素去重统计 hashmap

        * */
        int[] result = windowK(new int[]{1, 2, 1, 3, 4, 2, 3}, 4);
        System.out.println(Arrays.toString(result));
        List<Integer> resultList = countDistinct(new int[]{1, 2, 1, 3, 4, 2, 3}, 4);
        System.out.println(resultList);

    }

    /**
     * 方式1：不断取得窗口内的元素，放入hashset中去重，统计hashset的size
     * @param arr
     * @param k
     * @return
     */
    private static int[] windowK(int[] arr, int k) {
        if(arr == null)
            throw new RuntimeException("arr is null");
        if(arr.length == 0)
            throw new RuntimeException("arr is empty");
        if(k <= 0)
            throw new RuntimeException("k值为至少大于0");

        int resultSize = arr.length - k + 1;
        int[] resultArr = new int[resultSize];

        for (int time = 0; time < resultSize; time++) {
            // 存储k长度的数组元素
            int[] tempDest = new int[k];
            System.arraycopy(arr,time,tempDest,0, tempDest.length);

           /* // 将tempDest的元素放入hashset中去重
            HashSet<Integer> disSet = new HashSet<>();
            for (int element : tempDest) {
                disSet.add(element);
            }
            // 统计hashmap的size
            resultArr[time] = disSet.size();*/
            Integer countTime = getDistinctValueByHashMap(tempDest);
            resultArr[time] = countTime;
        }


        return resultArr;
    }



    // Function to count distinct elements in every window of size k

    /**
     * 方式二
     * 通过不断的添加和删除hashmap元素，来实现滑动窗口元素的滑动效果
     * @param arr
     * @param k
     * @return
     */
    static List<Integer> countDistinct(int[] arr, int k) {
        int n = arr.length;
        ArrayList<Integer> res = new ArrayList<>();
        Map<Integer, Integer> freq = new HashMap<>();

        // Store the frequency of elements of the first window
        for (int i = 0; i < k; i++) {
            freq.put(arr[i], freq.getOrDefault(arr[i], 0) + 1);
        }

        // Store the count of distinct elements of the first window
        res.add(freq.size());

        for (int i = k; i < n; i++) {
            freq.put(arr[i], freq.getOrDefault(arr[i], 0) + 1);
            freq.put(arr[i - k], freq.get(arr[i - k]) - 1);

            // If the frequency of arr[i - k] becomes 0,
            // remove it from the hash map
            if (freq.get(arr[i - k]) == 0) {
                freq.remove(arr[i - k]);
            }

            res.add(freq.size());
        }

        return res;
    }

    private static Integer getDistinctValueByHashMap(int[] arr) {
        HashMap<Integer, Integer> map = new HashMap<>();
        for (int element : arr) {
            map.put(element,null);
        }
        return map.size();
    }

}
