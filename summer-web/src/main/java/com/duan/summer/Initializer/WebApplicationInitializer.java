package com.duan.summer.Initializer;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;

/**
 * @author 白日
 * @create 2024/2/25 13:14
 * @description
 */

public interface WebApplicationInitializer {
    void onStartup(ServletContext servletContext);
}
