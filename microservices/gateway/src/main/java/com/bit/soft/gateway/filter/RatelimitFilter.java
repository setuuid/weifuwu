package com.bit.soft.gateway.filter;

import com.bit.soft.gateway.feign.TokenFeign;
import com.bit.soft.gateway.model.ErrorModel;
import com.bit.soft.gateway.model.JsonResult;
import com.google.common.util.concurrent.RateLimiter;
import net.sf.json.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by lyj on 2018/9/4.
 */
//@WebFilter(urlPatterns = { "/*" }, filterName = "ratelimitFilter")
public class RatelimitFilter implements Filter {

    private static Log logger = LogFactory.getLog(RatelimitFilter.class);

    private static String [] noFilterUrl={"serviceb"};


    @Autowired
    private TokenFeign tokenFeign;

  /*  @Autowired
    private RestTemplate restTemplate;*/

    private final String tokenName = "access_token";


    public void init(FilterConfig filterConfig) throws ServletException {

        System.out.println("----------------过滤器初始化------------------------");
    }

    @Value("${rateLimit}")
    private double rateLimit;

    /*每秒控制5个许可*/
    RateLimiter rateLimiter = RateLimiter.create(rateLimit);

    /**
     * 获取令牌
     *
     * @return
     */
    public boolean tryAcquire() {
        return rateLimiter.tryAcquire();
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        response.setContentType("application/json;charset=UTF-8");

        if(tryAcquire()){
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        responseOutWithJson(response,new ErrorModel(HttpStatus.UNAUTHORIZED.value(),"未获取到许可",null));
    }

    /**
     * 以JSON格式输出
     * @param response
     */
    protected void responseOutWithJson(HttpServletResponse response,
                                       Object responseObject) {
        //将实体对象转换为JSON Object转换
        JSONObject responseJSONObject = JSONObject.fromObject(responseObject);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter out = null;
        try {
            out = response.getWriter();
            out.append(responseJSONObject.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    public void destroy() {

        System.out.println("--------------过滤器销毁--------------");
    }

    public static void main(String[] args) {
        String start = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        RateLimiter limiter = RateLimiter.create(1.0); // 这里的1表示每秒允许处理的量为1个
        for (int i = 1; i <= 10; i++) {
            limiter.acquire();// 请求RateLimiter, 超过permits会被阻塞
            System.out.println("call execute.." + i);
        }
        String end = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        System.out.println("start time:" + start);
        System.out.println("end time:" + end);
    }
}