package com.ccq.framework.cypto;


import com.ccq.framework.exception.AppException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {

    /**
     * 对字符串进行md5
     * @param content
     * @return
     */
    public static String md5(String content) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("md5");
            return new String(md.digest(content.getBytes()));
        } catch (NoSuchAlgorithmException e) {
            throw new AppException(e.getMessage());
        }
    }
}
