package com.liuruichao.deferred;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Test2
 *
 * @author liuruichao
 * Created on 2017/2/28 14:47
 */
public class Test2 {
    public static void main(String[] args) {
        /*ArrayList<String> list = new ArrayList<>();
        list.add("liuruichao");
        list.add("liuruichao2");
        list.add("liuruichao3");
        list.add("liuruichao4");
        list.add("liuruichao5");
        System.out.println(list.size());

        List<String> list2 = list.subList(1, 3);
        list2.remove(0);
        System.out.println(list.size());*/

        List<String> list = new ArrayList<>();
        list.add("liuruichao");
        list.add("liuruichao2");
        list.add("liuruichao3");
        list.add("liuruichao4");
        list.add("liuruichao5");
        String[] array = new String[list.size()];
        array = list.toArray(array);
        System.out.println(Arrays.toString(array));
    }
}
