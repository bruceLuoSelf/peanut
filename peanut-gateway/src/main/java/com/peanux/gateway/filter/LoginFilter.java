package com.peanux.gateway.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.peanut.common.pojo.UserInfo;
import com.peanut.common.utils.CookieUtils;
import com.peanut.common.utils.JwtUtils;
import com.peanux.gateway.config.FilterProperties;
import com.peanux.gateway.config.JwtProperties;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author ljn
 * @date 2019/8/30.
 * 过滤器
 * 登录使用
 */
@Component
@EnableConfigurationProperties(FilterProperties.class)
public class LoginFilter extends ZuulFilter {

    @Autowired
    private FilterProperties filterProperties;

    @Autowired
    JwtProperties properties;

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 10;
    }

    @Override
    public boolean shouldFilter() {
        List<String> allowPaths = filterProperties.getAllowPaths();
        RequestContext context = RequestContext.getCurrentContext();
        HttpServletRequest request = context.getRequest();
        String url = request.getRequestURL().toString();
        if (CollectionUtils.isEmpty(allowPaths)) {
            return true;
        }
        for (String allowPath : allowPaths) {
            if (url.contains(allowPath)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        // 初始化Zuul网关运行上下文
        RequestContext context = RequestContext.getCurrentContext();
        HttpServletRequest request = context.getRequest();
        // 获取token
        String token = CookieUtils.getCookieValue(request, properties.getCookieName());
        if (StringUtils.isBlank(token)) {
            context.setSendZuulResponse(false);
            context.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
        }
        try {
            UserInfo userInfo = JwtUtils.getInfoFromToken(token, properties.getPublicKey());
        } catch (Exception e) {
            e.printStackTrace();
            context.setSendZuulResponse(false);
            context.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
        }
        return null;
    }
}
