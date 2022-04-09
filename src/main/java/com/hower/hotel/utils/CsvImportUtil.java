package com.hower.hotel.utils;

import cn.hutool.core.text.csv.CsvReader;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;

public class CsvImportUtil {

    private static final String FIX="\uFEFF";

    /**
     * 获取csv文件内容
     * @return 对象list
     */
    public static List<Map<String,Object>> getResource(byte[] bate) throws IOException {
        List<Map<String,Object>> allString = new ArrayList();
        Map<String,Object> callLogInfo ;
        List<String> list = new ArrayList();
        // 获取文件内容
        list = getSource(bate);
        // 获取文件表头
        List<String> title = Arrays.asList(list.get(0).split(","));
        String customerName = title.get(0).trim();
        String customerNo = title.get(1).trim();
        // 头部会带有"\uFEFF"值
        if(customerName.startsWith(FIX)){
            customerName = customerName.replace(FIX, "");
        }
        callLogInfo = new HashMap();
        callLogInfo.put("content",customerName);
        callLogInfo.put("profits",customerNo);
        allString.add(callLogInfo);

        list.remove(0);
        // 循环内容
        for(int i = 0; i<list.size();i++){
            List<String> content = Arrays.asList(list.get(i).split(","));
            // 当没有添加额外参数时
            if(content!=null){
                callLogInfo = new HashMap();
                callLogInfo.put("content",content.get(0));
                callLogInfo.put("profits",content.get(1));
                allString.add(callLogInfo);
            }
        }
        return allString;
    }


    /**
     * 读文件数据
     */
    public static List<String> getSource(byte[] bate) throws IOException {
        BufferedReader br = null;
        ByteArrayInputStream fis=null;
        InputStreamReader isr = null;
        try {
            fis = new ByteArrayInputStream(bate);
            //指定以UTF-8编码读入
            isr = new InputStreamReader(fis,"GBK");
            br = new BufferedReader(isr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String line;
        String everyLine ;
        List<String> allString = new ArrayList<>();
        try {
            //读取到的内容给line变量
            while ((line = br.readLine()) != null){
                everyLine = line;
                allString.add(everyLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(fis != null){
                fis.close();
            }
            if(isr != null){
                isr.close();
            }
        }
        return allString;
    }
}
