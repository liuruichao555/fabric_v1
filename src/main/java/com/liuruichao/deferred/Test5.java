package com.liuruichao.deferred;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Test5
 *
 * @author liuruichao
 * Created on 2017/2/28 15:01
 */
public class Test5 {
    public static void main(String[] args) {
        // 利用set特性去重
        Set<String> set = new HashSet<>();
        List<String> list = new ArrayList<>();
        list.add("liuruichao");
        list.add("liuruichao");
        list.add("liuruichao");
        list.add("liuruichao");
        list.add("liuruichao");

        Iterator<String> iterator = list.iterator();
        while (iterator.hasNext()) {
            String value = iterator.next();
            if (list.contains(value)) {
                iterator.remove();
            }
        }

        for (String str : list) {
            set.add(str);
        }
    }
}
