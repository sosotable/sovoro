package com.sovoro.model;

import java.util.ArrayList;

public class RandOrder {
    static ArrayList<ArrayList<Integer>> randOrder=new ArrayList<>();
    public static void setRandOrder() {
        for(int i=0;i<10;i++) {
            randOrder.add(makeRandArrayList());
        }
    }
    public static ArrayList<Integer> makeRandArrayList() {
        ArrayList<Integer> arrayList=new ArrayList<>();
        for(int i=0;i<4;i++) {
            arrayList.add((int)(Math.random() * 4));
        }
        return arrayList;
    }
}
