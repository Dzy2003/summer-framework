package com.duan.summer.support;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * @author 白日
 * @create 2024/3/6 15:59
 * @description
 */

public record WebServletRequest(HttpServletRequest request, HttpServletResponse response) {
}
