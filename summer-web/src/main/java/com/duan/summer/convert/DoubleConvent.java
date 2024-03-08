package com.duan.summer.convert;

/**
 * @author 白日
 * @create 2024/3/8 11:54
 * @description
 */

public class DoubleConvent implements Convent<Double>{
    @Override
    public Double convent(Object value) {
        return Double.valueOf(value.toString());
    }
}
