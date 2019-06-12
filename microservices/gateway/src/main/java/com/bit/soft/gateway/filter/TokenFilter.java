package com.bit.soft.gateway.filter;

import com.bit.soft.gateway.feign.TokenFeign;
import com.bit.soft.gateway.model.ErrorModel;
import com.bit.utils.CacheUtil;
import com.netflix.zuul.context.RequestContext;
import net.sf.json.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static com.bit.soft.gateway.common.Const.TOKEN_EXPIRE_SECONDS;


/**
 * Created by lyj on 2018/9/4.
 */
@WebFilter(urlPatterns = {"/*"}, filterName = "tokenAuthorFilter")
public class TokenFilter implements Filter {

	private static Log logger = LogFactory.getLog(TokenFilter.class);

	private static String[] noFilterUrl = {"user/login"};

	@Autowired
	private CacheUtil cacheUtil;

  /*  @Autowired
    private RestTemplate restTemplate;*/

	private final String tokenName = "at";

	private final String terminalName = "tid";

	public void init(FilterConfig filterConfig) throws ServletException {

		System.out.println("----------------过滤器初始化------------------------");
	}


	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		response.setContentType("application/json;charset=UTF-8");
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods","POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers","Content-Type, x-requested-with, X-Custom-Header,at,tid");
        //如果是OPTIONS请求就return 往后执行会到业务代码中 他不带参数会产生异常
		if (request.getMethod().equals("OPTIONS")) {
			return;
		}
		if (isInclude(((HttpServletRequest) servletRequest).getRequestURI())) {//白名单不拦截
			try {
				filterChain.doFilter(servletRequest, servletResponse);
				return;
			} catch (Exception e) {
				e.printStackTrace();
			}

		} else {
			String token = "";
			String terminalId = request.getHeader(terminalName);;
			if (terminalId == null || "".equals(terminalId)) {//判断是否为空
				responseOutWithJson(response, new ErrorModel(HttpStatus.UNAUTHORIZED.value(), "接入端为空", null));
				return;
			}
			if (request.getHeader(tokenName) != null && !"".equals((request.getHeader(tokenName)))) {//支持从header头中取值
				token = request.getHeader(tokenName);
			} else {
				token = request.getParameter(tokenName);//支持从url中获取
			}
			if (token == null || "".equals(token)) {//判断是否为空
				responseOutWithJson(response, new ErrorModel(HttpStatus.UNAUTHORIZED.value(), "无token", null));
				return;
			} else {
				String user = (String) cacheUtil.get("token:"+terminalId+":"+token);
				//JsonResult rs = tokenFeign.verify(token);
				if (user != null) {
					long l = cacheUtil.getExpire(token);
					if (l<300){
						// 更新过期时间
						cacheUtil.expire(token, TOKEN_EXPIRE_SECONDS);
					}
					RequestContext ct = RequestContext.getCurrentContext();
					ct.addZuulRequestHeader("userToken", user);
					filterChain.doFilter(servletRequest, servletResponse);
					return;
				} else {
					responseOutWithJson(response, new ErrorModel(HttpStatus.UNAUTHORIZED.value(), "无效token", null));
					return;
				}
			}
		}
	}


	public void destroy() {

		System.out.println("--------------过滤器销毁--------------");
	}

	/**
	 * 是否需要过滤
	 *
	 * @param url
	 * @return
	 */
	private boolean isInclude(String url) {
		for (String pattern : noFilterUrl) {
			if (url.indexOf(pattern) != -1) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 以JSON格式输出
	 *
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
}