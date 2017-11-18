package com.ccq.framework.lang;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by xiaoliu on 2017/9/13.
 */
public class DateUtil {

    /* 根据时间戳获取日期对象 */
    public static Date toDate(Long mills) {
        return new Date(mills);
    }


    /* 返回当前时间崔 */
    public static long currentMilliSeconds() {
        return new Date().getTime();
    }

}
