package com.mvc.user.util;

import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Utils {

    protected HttpServletRequest request;

    /**
     * 将当前系统时间解析为yyyy-MM-dd HH:mm:ss:SSS格式
     */
    public static String dateTimeString2Date() {
        long time = System.currentTimeMillis();
        Date d = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
        String t = sdf.format(d);
        return t;
    }

    /**
     * 将当前系统时间解析为yyyy年MM月dd日HH时mm分ss秒格式
     */
    public static String dateTimeString3Date() {
        long time = System.currentTimeMillis();
        Date d = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒");
        String t = sdf.format(d);
        return t;
    }

    /**
     * 将当前系统时间解析为yyyy-MM-dd HH:mm:ss:SSS格式
     */
    public static Date toDate() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
        return sdf.parse(dateTimeString2Date());
    }

    /**
     * 将当前系统时间解析为yyyy-MM-dd HH:mm:ss:SSS格式
     */
    public static Date toDate(String str) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
        return sdf.parse(str);
    }

    /**
     * 返回六位数的随机数
     */
    public static String returnSjs() {
        return String.valueOf(((int) ((Math.random() * 9 + 1) * 100000)));
    }

    /**
     * 获取系统的UUID
     */
    public static String getGUID() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    /***
     * 将字符串转化为MD5
     */
    public static String toMD5(String inStr) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        char[] charArray = inStr.toCharArray();
        byte[] byteArray = new byte[charArray.length];

        for (int i = 0; i < charArray.length; i++)
            byteArray[i] = (byte) charArray[i];
        byte[] md5Bytes = md5.digest(byteArray);
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16)
                hexValue.append("0");
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();

    }

    //将数据进行SHA-512加密
    public static String toSHA(String inStr) {
        MessageDigest sha = null;
        try {
            sha = MessageDigest.getInstance("SHA-512");
        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
            return "";
        }
        char[] charArray = inStr.toCharArray();
        byte[] byteArray = new byte[charArray.length];

        for (int i = 0; i < charArray.length; i++)
            byteArray[i] = (byte) charArray[i];
        byte[] md5Bytes = sha.digest(byteArray);
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16)
                hexValue.append("0");
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }

    /**
     * 将MD5解析为正常字符串（需要解析两次才可以）
     */
    public static String convertMD5(String inStr) {
        char[] a = inStr.toCharArray();
        for (int i = 0; i < a.length; i++) {
            a[i] = (char) (a[i] ^ 't');
        }
        String s = new String(a);
        return s;

    }

    //resultSet转换为List
    public static List toList(ResultSet rs) throws SQLException {
        List list = new ArrayList();
        ResultSetMetaData md = rs.getMetaData();
        int columnCount = md.getColumnCount(); //Map rowData;
        while (rs.next()) { //rowData = new HashMap(columnCount);
            Map rowData = new HashMap();
            for (int i = 1; i <= columnCount; i++) {
                rowData.put(md.getColumnName(i), rs.getObject(i));
            }
            list.add(rowData);
        }
        return list;
    }

    //对数据采用指纹加密的方式，保障数据的唯一性，用以生成用户的平台钱包地址，理论上唯一
    public static String toEncryption() throws NoSuchAlgorithmException {
        String str = "";
        for (int i = 0; i < 100; i++) {
            String s = UUID.randomUUID().toString();
            // 先进行MD5加密
            MessageDigest md = MessageDigest.getInstance("md5");
            // 对数据进行加密
            byte[] bs = md.digest(s.getBytes());

	        /*
             * BASE64Encoder底层实现原理：三字节变四字节
	         */
            // 采用数据指纹进一步加密，拿到数据指纹
            BASE64Encoder base = new BASE64Encoder();
            // 进一步加密
            str = base.encode(bs);
        }
        return str;
    }

    //采用UUID的方式添加用户的钱包，
    public static String toWallet() {
        Utils util = new Utils();
        String str1 = util.getGUID();
        String str2 = util.getGUID();
        String strp1 = str1.replace("-", "") + str2.replace("-", "");
        String strp2 = strp1.toUpperCase();
        return strp2;
    }

    public static String TransactSQLInjection(String str) {
        //return str.replaceAll(".*([';]+|(--)+).*", " ");
        return str.replaceAll("([';])+|(--)+", "");
    }


}
