package com.orderapi.interceptor;

import com.itextpdf.styledxmlparser.jsoup.internal.StringUtil;
import com.orderapi.common.JwtUtil;
import com.orderapi.context.UserContext;
import com.orderapi.entity.User;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.jdbc.Null;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author: wuze
 * @description:
 * @date: 2023/02/17
 */
@Slf4j
public class AuthenticationInterceptor implements HandlerInterceptor {


    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object) throws Exception {
        // 从 http 请求头中取出 token
//        String token = httpServletRequest.getHeader("token");
//        if(!StringUtil.isBlank(token)){
//            Claims claims = null;
//            try{
//                claims = JwtUtil.parseJWT(token);
//            }catch (Exception e){
//                log.warn("JWT格式错误");
//            }
//            if(claims == null){
//                UserContext.setUser(null);
//                return true;
//            }
//            User user = new User();
//            user.setId(claims.get("id",Long.class));
//            user.setUserName(claims.get("userName",String.class));
//            user.setPassword(claims.get("password",String.class));
//            user.setRole(claims.get("role",Integer.class));
//            user.setGender(claims.get("gender", Integer.class));
//            user.setAge(claims.get("age", Integer.class));
//            user.setPhone(claims.get("phone", String.class));
//            user.setSalt(claims.get("salt",String.class));
//            user.setCreateTime(claims.get("createTime", Date.class));
//            user.setUpdateTime(claims.get("updateTime", Date.class));
//
//            UserContext.setUser(user);
//        }
        return true;
    }
    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
    }
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
    }
}
