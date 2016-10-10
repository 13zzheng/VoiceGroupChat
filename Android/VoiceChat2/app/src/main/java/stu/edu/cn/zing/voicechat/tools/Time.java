package stu.edu.cn.zing.voicechat.tools;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2016/9/7.
 */
public class Time {
    public static String getTime() {
        DateFormat format=new SimpleDateFormat("yyyyMMddHHmmss");
        return format.format(new Date());
    }
}
