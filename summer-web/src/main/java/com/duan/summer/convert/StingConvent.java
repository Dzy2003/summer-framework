package com.duan.summer.convert;

/**
 * @author 白日
 * @create 2024/3/8 11:53
 * @description
 */

public class StingConvent implements Convent<String>{
    @Override
    public String convent(Object value) {
        return value.toString();
    }
}
