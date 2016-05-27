package com.example;

import java.util.ArrayList;
import java.util.List;

/**
 * user:zhengqingju
 * Date:2016/5/17
 * Time:9:26
 * Description:Page Function.
 */

public class TestSIN {


    public static void main(String[] args) {
        float y;
        for (int i = 0; i<= 360 ;i++){
            float x =i;

            y = (float) Math.sin(Math.toRadians(x));
            System.out.println(y);
        }
//        List<double[]> x = new ArrayList<double[]>();
//        List<double[]> values = new ArrayList<double[]>();
//        int step = 4;
//        int count = 360 / step + 1;
//        x.add(new double[count]);
//        double[] sinValues = new double[count];
//        values.add(sinValues);
//        for (int i = 0; i < count; i++) {
//            int angle = i * step;
//            x.get(0)[i] = angle;
//
//            double rAngle = Math.toRadians(angle);
//            sinValues[i] = Math.sin(rAngle);
//            System.out.println(sinValues[i]);
//        }




    }

}
