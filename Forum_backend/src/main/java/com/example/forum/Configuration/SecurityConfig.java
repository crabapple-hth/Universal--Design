package com.example.forum.Configuration;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.forum.Entity.Dto.Account;
import com.example.forum.Entity.RestBean;
import com.example.forum.Entity.Vo.response.AuthorizeVo;
import com.example.forum.Mapper.UserMapper;
import com.example.forum.filter.JwtAuthenticationFilter;
import com.example.forum.utils.JwtUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;

@Configuration
public class SecurityConfig {

    @Resource
    JwtUtils jwtUtils;

    @Resource
    UserMapper mapper;


    @Resource
    JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(conf->conf
                        .requestMatchers("/api/auth/**").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(conf->conf
                        .loginProcessingUrl("/api/auth/login")
                        .successHandler(this::AuthenticationHandler)
                        .failureHandler(this::AuthenticationHandler)
                )
                .logout(conf->conf
                        .logoutUrl("/api/auth/logout")
                        .logoutSuccessHandler(this::logoutHandler)
                )
                .exceptionHandling(conf->
                        conf.accessDeniedHandler(this::AuthenticationHandler)
                                .authenticationEntryPoint(this::AuthenticationHandler)
                )
                .sessionManagement(conf->{
                    conf.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                })
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();

    }



    public void AuthenticationHandler(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Object exceptionOrAuthentication) throws IOException, ServletException {
        response.setContentType("application/json;charset=utf-8");
        PrintWriter writer=response.getWriter();
        if(exceptionOrAuthentication instanceof AccessDeniedException exception) {
            writer.write(RestBean.failure(403, exception.getMessage()).asJSONString());
        } else if(exceptionOrAuthentication instanceof AuthenticationException exception) {
            writer.write(RestBean.failure(401, exception.getMessage()).asJSONString());
        } else if(exceptionOrAuthentication instanceof Authentication authentication){
            User user=(User) authentication.getPrincipal();
            Account account=mapper.selectOne(new QueryWrapper<Account>().eq("user_name",user.getUsername()));
            String jwt= JwtUtils.CreatJWT(user,account.getUserid());
            AuthorizeVo vo= new AuthorizeVo();
            BeanUtils.copyProperties(account,vo);
            vo.setToken(jwt);
            Calendar calendar=Calendar.getInstance();
            calendar.add(Calendar.HOUR,72);
            vo.setExpired(calendar.getTime());
            //不过这里需要注意，在登录成功的时候需要返回我们生成的JWT令牌，这样客户端下次访问就可以携带这个令牌了，令牌过期之后就需要重新登录才可以
            writer.write(RestBean.success(vo).asJSONString());
        }
    }

    public void logoutHandler(HttpServletRequest request,
                              HttpServletResponse response,
                              Authentication authentication) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        PrintWriter writer=response.getWriter();
        String authorization = request.getHeader("Authorization");
        if(authorization != null && authorization.startsWith("Bearer ")) {
            String token = authorization.substring(7);
            //将Token加入黑名单
            if(JwtUtils.invalidate(token)) {
                //只有成功加入黑名单才会退出成功
                writer.write(RestBean.success("退出登录成功").asJSONString());
                return;
            }
        }
        writer.write(RestBean.failure(400, "退出登录失败").asJSONString());
    }
}

