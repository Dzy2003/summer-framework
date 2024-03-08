package com.duan.summer.convert;

/**
 * @author 白日
 * @create 2024/3/8 11:54
 * @description
 */

public class IntegerConvent implements Convent<Integer>{
    @Override
    public Integer convent(Object value) {
        return Integer.valueOf(value.toString());
    }
}
