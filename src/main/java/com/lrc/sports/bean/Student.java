package com.lrc.sports.bean;

/**
 * Created by li on 2016/11/28.
 */
public class Student {
    private int age;
    private String name;
    private int []num;

    public int getAge() {
        return age;
    }

    public int[] getNum() {
        return num;
    }

    public void setNum(int[] num) {
        this.num = num;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
