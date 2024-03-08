package com.duan.summer.convert;

import java.util.Date;

/**
 * @author 白日
 * @create 2024/3/8 11:57
 * @description
 */

public class DateConvent implements Convent<Date>{
    @Override
    public Date convent(Object value) {
        return new Date(value.toString());
    }
}
