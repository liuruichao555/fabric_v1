package com.liuruichao.deferred;

import org.jdeferred.*;
import org.jdeferred.impl.DeferredObject;

import java.util.Objects;

/**
 * Test1
 *
 * @author liuruichao
 * Created on 2017/2/28 13:35
 */
public class Test1 {
    public static void main(String[] args) {
        Deferred deferred = new DeferredObject();
        Promise promise = deferred.promise();

        promise.done(o -> System.out.println("onDone"))
                .fail(o -> System.out.println("onFail"))
                .progress(o -> System.out.println("progress"))
                .always((state, o, o2) -> System.out.println("always")); // 总是会执行

        promise.then(o -> {
            System.out.println("onDone2");
        });

        // success
        //deferred.resolve("heheda");

        // fail
        deferred.reject("buzhidao");
    }
}
