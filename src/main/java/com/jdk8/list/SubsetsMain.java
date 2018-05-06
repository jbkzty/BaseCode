package com.jdk8.list;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author spuerKun
 * @date 17/12/7.
 */
public class SubsetsMain {

    public static List<List<Integer>> subsets(List<Integer> list) {
        if (list.isEmpty()) {
            // 如果输入为空，则边只包含一个字集，便是空列表本身
            List<List<Integer>> ans = new ArrayList<>();
            ans.add(Collections.emptyList());
            return ans;
        }
        Integer first = list.get(0);
        List<Integer> rest = list.subList(1, list.size());

        List<List<Integer>> subans = subsets(rest);
        return null;
    }

}
