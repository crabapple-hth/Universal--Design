package com.example.forum.Configuration;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//只需要添加Configuration用于注册配置类，不需要其他任何注解，已经自动配置好了
@Configuration
@MapperScan("com.example.forum.Mapper")
public class WebConfiguration implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new HandlerInterceptor() {
            @Override
            public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
                return HandlerInterceptor.super.preHandle(request, response, handler);
            }
        });
    }

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

}