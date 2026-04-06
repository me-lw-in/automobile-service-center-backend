package com.example.serviceassistantbackend.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class JobCardNumberUtil {
    private static final Random RANDOM = new Random();
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyyMMdd");

    public static String generateJobCardNumber() {

        String datePart = LocalDate.now().format(FORMATTER);

        int randomPart = 1000 + RANDOM.nextInt(9000); // 4-digit random

        return "JC-" + datePart + "-" + randomPart;
    }

}
