package com.lrc.sports.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.lrc.sports.bean.Student;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by li on 2016/11/28.
 * 通过http发送json数据到flume
 */
public class HttpUtil {

    /**
     * @param url
     * @param param
     * @return
     */
    public static String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            URLConnection conn = realUrl.openConnection();
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");  //设定 请求格式 json，也可以设定xml格式的
            conn.setDoOutput(true);
            conn.setDoInput(true);
            out = new PrintWriter(conn.getOutputStream());
            out.print(param);
            out.flush();
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！" + e);
            e.printStackTrace();
        }
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

    public static void main(String[] args) throws InterruptedException {
        while(true) {
            List<Student> list = new ArrayList<Student>(1);
            Student s = new Student();
            s.setAge((int) (Math.random() * 100));
            s.setName("test");
            list.add(s);


            sendPost("http://192.168.1.232:5140", JSONArray.toJSONString(list));
            //Thread.sleep(1000);
        }

    }

}
