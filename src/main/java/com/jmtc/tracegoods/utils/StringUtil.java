package com.jmtc.tracegoods.utils;

import java.util.Random;

/**
 * @author Chris
 * @date 2021/6/7 20:25
 * @Email:gang.wu@nexgaming.com
 */
public class StringUtil {
    public static String getRandomString(int length){
        String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random=new Random();
        StringBuffer sb=new StringBuffer();
        for(int i=0;i<length;i++){
            int number=random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }
}
