package com.movie.controller.api.admin;

import java.util.ArrayList;
import java.util.List;

public class testlist {
    public static void main(String[] args) {

        String str = "12345678";
        List<Integer> list = new ArrayList<>();

        for (int i = 0; i < 8; i++) {
            list.add(Integer.parseInt(str.substring(i,i+1)));
        }
        for (Integer a : list) {
           System.out.println(a);
        }

    }
}
