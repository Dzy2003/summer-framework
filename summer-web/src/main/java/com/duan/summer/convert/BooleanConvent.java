package com.duan.summer.convert;
/**
 * @author 白日
 * @create 2024/3/8 11:59
 * @description
 */

public class BooleanConvent implements Convent<Boolean>{
    @Override
    public Boolean convent(Object value) {
        return Boolean.parseBoolean(value.toString());
    }
}
