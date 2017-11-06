package com.depression.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MD5Util {
    
    /**
     * 123456加密后是：123456:E10ADC3949BA59ABBE56E057F20F883E
     */

	/** * 正则表达式，用于匹配   */
    private final static Pattern pattern = Pattern.compile("\\d+");  
    
    /** * 字符集 */
    private final static String charset="utf-8"; 
    
    /** * 自定义key   */
    private final static String key = "aestsv";
    
    /** * 16进制字符集 */
    private static final char HEX_DIGITS[] = { '0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

    /** * 指定算法为MD5的MessageDigest */
    private static MessageDigest messageDigest = null;

    /** * 初始化messageDigest的加密算法为MD5 */
    static {
        try {
            messageDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    /**
     * * 获取文件的MD5值
     * 
     * @param file
     *            目标文件
     * 
     * @return MD5字符串
     */
    public static String getFileMD5String(File file) {
        String ret = "";
        FileInputStream in = null;
        FileChannel ch = null;
        try {
            in = new FileInputStream(file);
            ch = in.getChannel();
            ByteBuffer byteBuffer = ch.map(FileChannel.MapMode.READ_ONLY, 0,
                    file.length());
            messageDigest.update(byteBuffer);
            ret = bytesToHex(messageDigest.digest());
        } catch (IOException e) {
            e.printStackTrace();

        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (ch != null) {
                try {
                    ch.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return ret;
    }

    /**
     * * 获取文件的MD5值
     * 
     * @param fileName
     *            目标文件的完整名称
     * 
     * @return MD5字符串
     */
    public static String getFileMD5String(String fileName) {
        return getFileMD5String(new File(fileName));
    }

    /**
     * * MD5加密字符串
     * 
     * @param str
     *            目标字符串
     * 
     * @return MD5加密后的字符串
     */

    public static String getMD5String(String str) {

        return getMD5String(str.getBytes());
    }

    /**
     * * MD5加密以byte数组表示的字符串
     * 
     * @param bytes
     *            目标byte数组
     * 
     * @return MD5加密后的字符串
     */

    public static String getMD5String(byte[] bytes) {
        messageDigest.update(bytes);
        return bytesToHex(messageDigest.digest());
    }

    /**
     * * 校验密码与其MD5是否一致
     * 
     * @param pwd
     *            密码字符串
     * 
     * @param md5
     *            基准MD5值
     * 
     * @return 检验结果
     */
    public static boolean checkPassword(String pwd, String md5) {
        return getMD5String(pwd).equalsIgnoreCase(md5);
    }

    /**
     * * 校验密码与其MD5是否一致
     * 
     * @param pwd
     *            以字符数组表示的密码
     * 
     * @param md5
     *            基准MD5值
     * 
     * @return 检验结果
     */
    public static boolean checkPassword(char[] pwd, String md5) {
        return checkPassword(new String(pwd), md5);

    }

    /**
     * * 检验文件的MD5值
     * 
     * @param file
     *            目标文件
     * 
     * @param md5
     *            基准MD5值
     * 
     * @return 检验结果
     */
    public static boolean checkFileMD5(File file, String md5) {
        return getFileMD5String(file).equalsIgnoreCase(md5);

    }

    /**
     * * 检验文件的MD5值
     * 
     * @param fileName
     *            目标文件的完整名称
     * 
     * @param md5
     *            基准MD5值
     * 
     * @return 检验结果
     */
    public static boolean checkFileMD5(String fileName, String md5) {
        return checkFileMD5(new File(fileName), md5);

    }

    /**
     * * 将字节数组转换成16进制字符串
     * 
     * @param bytes
     *            目标字节数组
     * 
     * @return 转换结果
     */
    public static String bytesToHex(byte bytes[]) {
        return bytesToHex(bytes, 0, bytes.length);

    }

    /**
     * * 将字节数组中指定区间的子数组转换成16进制字符串
     * 
     * @param bytes
     *            目标字节数组
     * 
     * @param start
     *            起始位置（包括该位置）
     * 
     * @param end
     *            结束位置（不包括该位置）
     * 
     * @return 转换结果
     */
    public static String bytesToHex(byte bytes[], int start, int end) {
        StringBuilder sb = new StringBuilder();
        for (int i = start; i < start + end; i++) {
            sb.append(byteToHex(bytes[i]));
        }
        return sb.toString();

    }

    /**
     * * 将单个字节码转换成16进制字符串
     * 
     * @param bt
     *            目标字节
     * 
     * @return 转换结果
     */
    public static String byteToHex(byte bt) {
        return HEX_DIGITS[(bt & 0xf0) >> 4] + "" + HEX_DIGITS[bt & 0xf];

    }

    // //
    public static void main(String[] args) throws IOException {
        long begin = System.currentTimeMillis();
        String md5 = getFileMD5String(new File("c://aa.cer"));
        long end = System.currentTimeMillis();
        System.out.println("MD5:\t" + md5 + "\nTime:\t" + (end - begin) + "ms");

    }
    
    //加密处理  
    public static String encode(String src) {   
        try {  
            //得到一个指定的编码格式的字节数组，Linux和windows默认的编码格式不同，所以要指定特定的编码  
            byte[] data = src.getBytes(charset);  
            byte[] keys = key.getBytes();  
            StringBuilder sb = new StringBuilder();  
            for (int i = 0; i < data.length; i++) {  
                //结合key和相应的数据进行加密操作,ofxx的作用是补码，byte是8bits，而int是32bits  
                int n = (0xff & data[i]) + (0xff & keys[i % keys.length]);  
                sb.append("A"+n);  
            }  
            return sb.toString();  
        }catch (UnsupportedEncodingException e){  
            e.printStackTrace();  
        }  
        return src;  
    }  
  
    //解密处理  
    public static String decode(String src) {  
        if(src == null || src.length() == 0){  
            return src;  
        }  
        //正则表达式字符串匹配  
        Matcher m = pattern.matcher(src);  
          
        List<Integer> list = new ArrayList<Integer>();  
        //find方法(部分匹配):尝试去发现输入串中是否匹配相应的子串  
        while (m.find()) {  
            try {  
                //返回匹配到的子字符串  
                String group = m.group();  
                list.add(Integer.valueOf(group));  
            } catch (Exception e) {  
                e.printStackTrace();  
                return src;  
            }  
        }  
          
        //如果有匹配的字符串  
        if (list.size() > 0) {  
            try {  
                byte[] data = new byte[list.size()];  
                byte[] keys = key.getBytes();  
                //相对于加密过程的解密过程  
                for (int i = 0; i < data.length; i++) {  
                    data[i] = (byte) (list.get(i) - (0xff & keys[i % keys.length]));  
                }  
                return new String(data, charset);  
            } catch (UnsupportedEncodingException e){  
                e.printStackTrace();  
            }  
            return src;  
        } else {  
            return src;  
        }  
    }  
}