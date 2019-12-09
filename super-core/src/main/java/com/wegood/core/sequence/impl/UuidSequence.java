package com.wegood.core.sequence.impl;


import com.wegood.core.sequence.Sequence;

import java.util.UUID;

/**
 * @author rain
 */
public class UuidSequence implements Sequence<String> {

    @Override
    public String next() {
        return get();
    }

    public static String get() {
        return UUID.randomUUID().toString();
    }

    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            System.out.println(UuidSequence.get());
        }
    }

    @Override
    public boolean support(Class clazz) {
        return clazz == String.class;
    }

}
