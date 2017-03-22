package com.liuruichao.deferred;

import java.util.ArrayList;
import java.util.List;

/**
 * Test3
 *
 * @author liuruichao
 *         Created on 2017/2/28 14:53
 */
public class Test3 {
    public static void main(String[] args) {
        List<? extends Animal> list = listAll();
        // fail
        // list.add(new Dog());
    }

    private static List<Animal> listAll() {
        List<Animal> list = new ArrayList<>();

        list.add(new Dog());
        list.add(new Cat());

        return list;
    }

    private static class Animal {

    }

    private static class Dog extends Animal {

    }

    private static class Cat extends Animal {

    }
}
