package com.example.rqchallenge.employees.util;

import com.example.rqchallenge.util.RandomNumberGenerator;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class RandomNumberGeneratorTest {

    @Test
    void testGenerateRandomNumberInRange() {
        int min = 10;
        int max = 20;

        // Generate 100 random numbers and check if they are within the specified range
        for (int i = 0; i < 100; i++) {
            int randomNumber = RandomNumberGenerator.generateRandomNumber(min, max);
            assertTrue(randomNumber >= min && randomNumber <= max);
        }
    }
}
