package com.duan.summer.convert;

/**
 * @author 白日
 * @create 2024/3/8 11:53
 * @description
 */

public class FloatConvent implements Convent<Float>{
    @Override
    public Float convent(Object value) {
        return Float.valueOf(value.toString());
    }
}
