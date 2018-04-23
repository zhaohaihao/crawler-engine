package com.frame.process.utils;

import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
/**
 * 时间工具类
 * Created by zhh on 2018/04/21.
 */
public final class DateTimeUtils {

    /**
     * 年(yyyy)
     */
    public static final String YEAR = "yyyy";

    /**
     * 年-月(yyyy-MM)
     */
    public static final String YEAR_MONTH = "yyyy-MM";

    /**
     * 年-月-日(yyyy-MM-dd)
     */
    public static final String YEAR_MONTH_DAY = "yyyy-MM-dd";

    /**
     * 年月日(yyyyMMdd)
     */
    public static final String YEAR_MONTH_DAY_SIMPLE = "yyyyMMdd";

    /**
     * 时分秒(HHmmss)
     */
    public static final String HOUR_MINUTE_SECOND_SIMPLE = "HHmmss";

    /**
     * 年-月-日 小时(yyyy-MM-dd HH)
     */
    public static final String YEAR_MONTH_DAY_HOUR = "yyyy-MM-dd HH";

    /**
     * 年-月-日 小时(yyyy-MM-dd HH)中文输出
     */
    public static final String YEAR_MONTH_DAY_HOUR_CN = "yyyy年MM月dd日HH时";

    /**
     * 年-月-日 小时:分钟(yyyy-MM-dd HH:mm)
     */
    public static final String YEAR_MONTH_DAY_HOUR_MINUTE = "yyyy-MM-dd HH:mm";

    /**
     * 年-月-日 小时:分钟:秒钟(yyyy-MM-dd HH:mm:ss)
     */
    public static final String YEAR_MONTH_DAY_HOUR_MINUTE_SECOND = "yyyy-MM-dd HH:mm:ss";

    /**
     * 年/月/日 小时:分钟(yyyy/MM/dd HH:mm)
     */
    public static final String YEAR_MONTH_DAY_HOUR_MINUTE_SLASH = "yyyy/MM/dd HH:mm";

    /**
     * 年/月/日 小时:分钟:秒钟(yyyy/MM/dd HH:mm:ss)
     */
    public static final String YEAR_MONTH_DAY_HOUR_MINUTE_SECOND_SLASH = "yyyy/MM/dd HH:mm:ss";

    /**
     * 年月日小时分钟秒钟(yyyyMMddHHmmss)
     */
    public static final String YEAR_MONTH_DAY_HOUR_MINUTE_SECOND_SIMPLE = "yyyyMMddHHmmss";


    /**
     * (UK地区时间)年月日小时分钟秒钟(EEE MMM dd HH:mm:ss Z yyyy)
     */
    public static final String YEAR_MONTH_DAY_HOUR_MINUTE_SECOND_LOCALE_UK = "EEE MMM dd HH:mm:ss Z yyyy";
    
    /**
     * 小时:分钟:秒钟(HH:mm:ss)
     */
    public static final String HOUR_MINUTE_SECOND = "HH:mm:ss";

    /**
     * 小时:分钟(HH:mm)
     */
    public static final String HOUR_MINUTE = "HH:mm";

    /**
     * 月.日(M.d)
     */
    public static final String MONTH_DAY = "M.d";

    /**
     * 格式化日期时间
     *
     * @param date    Date对象
     * @param pattern 模式
     * @return 格式化后的日期时间字符串
     */
    public static String format(Date date, String pattern) {
        if (date == null)
            return "";
        return new DateTime(date).toString(pattern);
    }
    
    /**
     * 格式化日期时间字符串
     *
     * @param dateString 日期时间字符串
     * @param pattern    模式
     * @return Date对象
     */
    public static Date formatDateString(String dateString, String pattern) {
        try {
            DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(pattern);
            return dateTimeFormatter.parseDateTime(dateString).toDate();
        } catch (Exception e) {
            return null;
        }
    }
}
