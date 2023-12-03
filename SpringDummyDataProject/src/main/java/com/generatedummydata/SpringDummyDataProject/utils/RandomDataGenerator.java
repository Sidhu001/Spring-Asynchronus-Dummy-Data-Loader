package com.generatedummydata.SpringDummyDataProject.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.generatedummydata.SpringDummyDataProject.constants.TableConstants.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import static java.lang.Math.abs;

public class RandomDataGenerator {
    int START = 1000000000;
    long END = 9999999999L;
    public static final Logger logger = LogManager.getLogger(testRandomFunctions.class);

    private static String[] ssId = {"USD", "CAD", "GEN", "F22", "F25"};

    private static String[] bcc = {"016", "072", "060", "027", "037"};
    private static String[] acctTpCd = {"002", "004", "006", "008", "010"};
    public static String generateRandomNumber(int noOfDigits) {
        Random random = new Random();
        return String.valueOf(abs(random.nextLong())).substring(0,noOfDigits);
    }

    public static String generateRandomThreeCharacterStringsFromGivenArray(String fieldName){
        String value = "";
        Random random = new Random();
        int index;
        switch (fieldName){
            case SRCE_SYS_ID:
                index = random.nextInt(ssId.length);
                value = ssId[index];
                break;
            case BUS_CTR_CD:
                index = random.nextInt(bcc.length);
                value = bcc[index];
                break;
            case ACCT_TYPE_CD:
                index = random.nextInt(acctTpCd.length);
                value = acctTpCd[index];
                break;
            default:
                throw new IllegalArgumentException("Unexpected field name provided " + fieldName);
        }
        return value;
    }

    public static String generateRandomStringOfRequiredLength(int length){
        return UUID.randomUUID().toString().replaceAll("-","").substring(0, length);
    }

    public static String generateRandomDate(){
        LocalDate startDate = LocalDate.of(1990, 01, 01);
        LocalDate endDate = LocalDate.of(2026, 12, 31);
        long daysBetween = ChronoUnit.DAYS.between(startDate, endDate);
        long randomNumberOfDays = ThreadLocalRandom.current().nextLong(daysBetween + 1);
        return String.valueOf(startDate.plusDays(randomNumberOfDays));
    }

    public static Long generateRandomLongValue(){
        Random random = new Random();
        return (long) (100000000000000L + random.nextFloat() * 900000000000000L);
    }

    public static int generateRandomInt(int min, int max){
        return ThreadLocalRandom.current().nextInt(min, max);
    }

    public static String generateRandomDateTimeStamp(){
        long offset = Timestamp.valueOf("2012-01-01 00:00:00").getTime();
        long end = Timestamp.valueOf("2013-01-01 00:00:00").getTime();
        long diff = end - offset + 1;
        String timestamp = new Timestamp(offset + (long)(Math.random() * diff)).toString().concat("Z").replaceFirst(" ", "T");
        timestamp = OffsetDateTime.ofInstant(
                Instant.parse(timestamp), ZoneOffset.UTC)
                .format(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT));
        return timestamp;
    }

}
