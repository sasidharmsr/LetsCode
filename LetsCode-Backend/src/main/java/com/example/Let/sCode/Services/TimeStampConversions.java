package com.example.Let.sCode.Services;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class TimeStampConversions {

    public String timstamptoString(long creationTimeSeconds) {
        Instant creationInstant = Instant.ofEpochSecond(creationTimeSeconds);
        LocalDateTime creationDateTime = LocalDateTime.ofInstant(creationInstant, ZoneOffset.UTC);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String creationFormatted = formatter.format(creationDateTime);
        return creationFormatted;
    }

    public long StringtotimeStamp(String Datetime) {
       // String inputDateTime = "2013-10-12(Sat) 17:30";
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd(EEE) HH:mm");
        LocalDateTime localDateTime = LocalDateTime.parse(Datetime, formatter);

        // Convert LocalDateTime to Unix timestamp (Instant)
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.systemDefault());
        long unixTimestamp = zonedDateTime.toInstant().getEpochSecond();
        return unixTimestamp;
    } 

    // ZoneId indiaZone = ZoneId.of("Asia/Kolkata"); // Set the time zone to India (Asia/Kolkata)
        
    // long currentUnixTimestampInSeconds = System.currentTimeMillis() / 1000;
    // System.out.println("Current Unix Timestamp (seconds): " + currentUnixTimestampInSeconds);
    
    // Instant creationInstant = Instant.ofEpochSecond(currentUnixTimestampInSeconds);
    // ZonedDateTime creationZonedDateTime = ZonedDateTime.ofInstant(creationInstant, ZoneOffset.UTC);
    // ZonedDateTime creationIndiaTime = creationZonedDateTime.withZoneSameInstant(indiaZone);
    
    // DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    // String creationFormatted = formatter.format(creationIndiaTime);
    // System.out.println("Creation time in India: " + creationFormatted);

}
