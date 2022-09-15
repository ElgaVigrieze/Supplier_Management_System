package com.company.springmvcweb.data;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        var repo3 = new DeliveryPerformance();
        System.out.println(repo3.getSupplierPerformancePerMonth(3));
        List<Double> dpm = new ArrayList<>();
        for (int i = 1; i < 9; i++) {
            if(repo3.getSupplierPerformancePerMonth(i) == null){
                dpm.add(null);
            }
            dpm.add(repo3.getSupplierPerformancePerMonth(i));
            System.out.println(i);
        }
        System.out.println(dpm);
    }

}
