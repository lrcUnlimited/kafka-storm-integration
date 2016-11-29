package com.lrc.sports.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.lrc.sports.bean.Request;
import com.lrc.sports.bean.Student;
import com.sun.org.apache.regexp.internal.RE;

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
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
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
        //使用finally块来关闭输出流、输入流
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
        List<Request> list = new ArrayList<Request>(1);
        Request<String> req=new Request<String>();
        Student s = new Student();
        s.setAge((int) (Math.random() * 100));
        s.setName("test");
        int[]num={1,2,456};
        s.setNum(num);
        req.setBody(JSON.toJSONString(s));
        list.add(req);
        System.out.println(JSONArray.toJSONString(list));
        Student parseObject=JSON.parseObject(req.getBody(),Student.class);
        System.out.println(parseObject.getName());



        //sendPost("http://192.168.1.235:5140", JSONArray.toJSONString(list));
        //Thread.sleep(1000);

    }

}
