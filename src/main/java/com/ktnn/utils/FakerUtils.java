package com.ktnn.utils;

import com.github.javafaker.Faker;
import lombok.Getter;

@Getter
public class FakerUtils {
    private static final Faker faker = new Faker();

    public Faker getFaker() {
        return faker;
    }

    public static int getRandomPrice() {
        return faker.random().nextInt(1000000, 1000000000);
    }

    public static long getRandomQuantity() {
        return faker.random().nextInt(200, 1000);
    }

    public static long getUniqueNumber() {
        return System.nanoTime();
    }
}
