package com.vks.hbase.serialize;
import java.io.UnsupportedEncodingException;
import java.util.Random;
import java.util.UUID;
/**
 * Utility class for users to generate their own keys. Any key can be used,
 * this is just a utility that provides a set of simple keys.
 *
 *
 */
public class SimpleRowKeyGenerator {

    public static byte[] getUUIDKey(String prefix)
            throws UnsupportedEncodingException{
        return (prefix + UUID.randomUUID().toString()).getBytes("UTF8");
    }

    public static byte[] getRandomKey(String prefix)
            throws UnsupportedEncodingException{
        return (prefix + String.valueOf(new Random().nextLong())).getBytes("UTF8");
    }
    public static byte[] getTimestampKey(String prefix)
            throws UnsupportedEncodingException {
        return (prefix + String.valueOf(
                System.currentTimeMillis())).getBytes("UTF8");
    }
    public static byte[] getNanoTimestampKey(String prefix)
            throws UnsupportedEncodingException{
        return (prefix + String.valueOf(
                System.nanoTime())).getBytes("UTF8");

    }
    //custom key by using first 2 fields
    public static byte[] getBDMPKey(byte[] payload, String delimit)
            throws UnsupportedEncodingException{
       String[] splitRec =  String.valueOf(payload).split(delimit);
        return (splitRec[0]+ "-"+ splitRec[1]).getBytes("UTF8");
    }


}