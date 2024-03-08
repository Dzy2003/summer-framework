package com.duan.summer.convert;

/**
 * @author 白日
 * @create 2024/3/8 11:52
 * @description
 */

public class LongConvent implements Convent<Long>{
    @Override
    public Long convent(Object value) {
        return Long.valueOf(value.toString());
    }
}
