package cn.hedeoer.collections.map;

import java.util.*;
import java.util.stream.Collectors;

public class $07MiniMumElementsRemove {
    /*
    Remove minimum elements such that no common elements exist in two arrays

    Given two arrays arr1[] and arr2[] consisting of n and m elements respectively.
    The task is to find the minimum number of elements to remove from each array such that
    intersection of both arrays becomes empty and both arrays become mutually exclusive.

    Input: arr[] = { 1, 2, 3, 4}, arr2[] = { 2, 3, 4, 5, 8 }
    Output: 3
    Explanation: We need to remove 2, 3 and 4 from any array.

    Input: arr[] = { 4, 2, 4, 4}, arr2[] = { 4, 3 }
    Output: 1
    Explanation: We need to remove 4 from arr2[]

    Input : arr[] = { 1, 2, 3, 4 }, arr2[] = { 5, 6, 7 }
    Output : 0
    Explanation: There is no common element in both.
    * */
    public static void main(String[] args) {

/*        int[] arr1 = {1, 2, 3, 4};
        int[] arr2 = {2, 3, 4, 5, 8};
        $07MiniMumElementsRemove.getMinimumElementsRemoveByBruteForce(arr1, arr2);

        arr1 = new int[]{4, 2, 4, 4};
        arr2 = new int[]{4, 3};
        $07MiniMumElementsRemove.getMinimumElementsRemoveByBruteForce(arr1, arr2);

        arr1 = new int[]{1, 2, 3, 4};
        arr2 = new int[]{5, 6, 7};
        $07MiniMumElementsRemove.getMinimumElementsRemoveByBruteForce(arr1, arr2);*/

/*        int[] arr1 = {1, 2, 3, 4};
        int[] arr2 = {2, 3, 4, 5, 8};
        $07MiniMumElementsRemove.getMinimumElementsRemoveByHashMap(arr1, arr2);

        arr1 = new int[]{4, 2, 4, 4};
        arr2 = new int[]{4, 3};
        $07MiniMumElementsRemove.getMinimumElementsRemoveByHashMap(arr1, arr2);

        arr1 = new int[]{1, 2, 3, 4};
        arr2 = new int[]{5, 6, 7};
        $07MiniMumElementsRemove.getMinimumElementsRemoveByHashMap(arr1, arr2);*/

        int[] arr1 = {1, 2, 3, 4};
        int[] arr2 = {2, 3, 4, 5, 8};
        $07MiniMumElementsRemove.getMinimumElementsRemoveByOneMap(arr1, arr2);

        arr1 = new int[]{4, 2, 4, 4};
        arr2 = new int[]{4, 3};
        $07MiniMumElementsRemove.getMinimumElementsRemoveByOneMap(arr1, arr2);

        arr1 = new int[]{1, 2, 3, 4};
        arr2 = new int[]{5, 6, 7};
        $07MiniMumElementsRemove.getMinimumElementsRemoveByOneMap(arr1, arr2);
    }

    /**
     * 暴力破解
     *输出双方元素需要删除的元素和 需要删除的交集元素个数
     * @param arr1
     * @param arr2
     */
    private static void getMinimumElementsRemoveByBruteForce(int[] arr1, int[] arr2) {
        // arr1中需要删除的交集元素索引
        // 使用set保证下面使用双层for循环时相同位置的索引不会重复添加，比如 {4, 2, 4, 4} 和 {4, 3}的情况
        HashSet<Integer> set1 = new HashSet<>();
        HashMap<Integer, Integer> map1 = new HashMap<>();

        // arr1中需要删除的交集元素索引
        HashSet<Integer> set2 = new HashSet<>();

        for (int i = 0; i < arr1.length; i++) {
            for (int j = 0; j < arr2.length; j++) {
                if (arr1[i] == arr2[j]) {
                    set1.add(i);
                    set2.add(j);
                    // 计算交集元素出现的次数
                    if (map1.containsKey(arr1[i])) {
                        map1.put(arr1[i], map1.get(arr1[i]) + 1);
                    } else {
                        map1.put(arr1[i], 1);
                    }

                }
            }
        }

        // 如果交集元素的在set1和set2中出现的次数不一致，只需要去除较少的一方即可保证无交集
        for (Map.Entry<Integer, Integer> entry : map1.entrySet()) {
            int key = entry.getKey();
            int value = entry.getValue();
            // 交集元素出现次数大于1
            if (value > 1) {
                // arr1中交集元素出现次数
                int count1 = Collections.frequency(Arrays.asList(arr1), key);
                // arr2中交集元素出现次数
                int count2 = Collections.frequency(Arrays.asList(arr2), key);
                // 交集元素出现次数不一致
                if (count1 > count2) {
                    // 移除set2中对应的索引
                    set2.removeIf(index -> arr2[index] == key);
                } else {
                    // 移除set1中对应的索引
                    set1.removeIf(index -> arr1[index] == key);
                }
            }
        }

        List<Integer> l1 = set1.stream()
                .map(index -> arr1[index])
                .collect(Collectors.toList());
        List<Integer> l2 = set2.stream()
                .map(index -> arr2[index])
                .collect(Collectors.toList());

        HashSet<Integer> re = new HashSet<>();
        re.addAll(set1);
        re.addAll(set2);

        System.out.println("We need to remove from arr1: " + l1);
        System.out.println("We need to remove from arr2: " + l2);
        System.out.println("We need to remove numbers of elements: " + re.size());


    }

    /**
     * 使用hashmap
     * 仅仅统计需要删除的交集元素个数
     * @param arr1
     * @param arr2
     */
    private static void getMinimumElementsRemoveByHashMap(int[] arr1, int[] arr2) {
        Integer res = 0;
        HashMap<Integer, Integer> map1 = new HashMap<>();
        HashMap<Integer, Integer> map2 = new HashMap<>();
        for(int ele : arr1){
            map1.put(ele, map1.getOrDefault(ele, 0) + 1);
        }

        for(int ele : arr2){
            map2.put(ele, map2.getOrDefault(ele, 0) + 1);
        }

        for(Map.Entry<Integer, Integer> entry : map1.entrySet()){
            if(map2.containsKey(entry.getKey())){
                res += Math.min(entry.getValue(), map2.get(entry.getKey()));
            }
        }

        System.out.println("We need to remove numbers of elements: " + res);
    }

    /**
     * 使用一个 hashmap
     * 仅仅统计需要删除的交集元素个数
     * @param arr1
     * @param arr2
     */
    private static void getMinimumElementsRemoveByOneMap(int[] arr1, int[] arr2) {

        Integer res = 0;
        HashMap<Integer, Integer> map = new HashMap<>();
        for(int ele : arr1){
            map.put(ele, map.getOrDefault(ele, 0) + 1);
        }

        for (int ele : arr2) {
            if(map.containsKey(ele) && map.get(ele) > 0){
                res++;
                /*
                * 为什么需要减1？
                * 假设：
                arr1 = [1, 2, 2, 3]
                arr2 = [2, 2, 3, 4]
                不减1的话会出现什么问题？

                arr1 中的元素计数：{1:1, 2:2, 3:1}
                遍历 arr2 时，第一个2匹配，第二个2也匹配（因为没减1）
                结果会将 arr1 中的两个2都算作匹配，但实际上 arr2 中也只有两个2
                正确处理（减1）：

                第一个2匹配后，计数变为 {1:1, 2:1, 3:1}
                第二个2匹配后，计数变为 {1:1, 2:0, 3:1}
                这样确保了每个元素只被匹配一次
                * */
                map.put(ele, map.get(ele) - 1);
            }
        }
        System.out.println("We need to remove numbers of elements: " + res);
    }
}
