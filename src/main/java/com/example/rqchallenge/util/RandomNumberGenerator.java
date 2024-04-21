package com.example.rqchallenge.util;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.Random;

public class RandomNumberGenerator {

    private static final Random random = new Random();

    private RandomNumberGenerator() {
    }

    public static int generateRandomNumber(int min, int max) {
        return random.nextInt(max - min + 1) + min;
    }
}
