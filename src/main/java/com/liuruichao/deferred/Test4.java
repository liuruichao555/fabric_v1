package com.liuruichao.deferred;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;

/**
 * Test4
 *
 * @author liuruichao
 * Created on 2017/2/28 14:57
 */
public class Test4 {
    public static void main(String[] args) {
        Map<String, String> map = new HashMap<>();
        map.put("lrc", "liuruichao");
        map.put("bzd", "buzhidao");
        map.put("hhd", "heheda");

        Set<Map.Entry<String, String>> entrySet = map.entrySet();
        for (Map.Entry<String, String> entry : entrySet) {
            System.out.println(String.format("key: %s, value: %s.", entry.getKey(), entry.getValue()));
        }

        // java8 or later
        map.forEach((s, s2) -> System.out.println(String.format("s: %s, s2: %s.", s, s2)));
    }
}
