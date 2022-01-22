package com.haoran.Brainstorming.interceptor;

import com.haoran.Brainstorming.model.User;
import com.haoran.Brainstorming.service.ISystemConfigService;
import com.haoran.Brainstorming.service.IUserService;
import com.haoran.Brainstorming.util.CookieUtil;
import com.haoran.Brainstorming.util.HttpUtil;
import com.haoran.Brainstorming.util.IpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;


@Component
public class CommonInterceptor implements HandlerInterceptor {

    private final Logger log = LoggerFactory.getLogger(CommonInterceptor.class);
    @Resource
    private IUserService userService;
    @Resource
    private CookieUtil cookieUtil;
    @Resource
    private ISystemConfigService systemConfigService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        long start = System.currentTimeMillis();
        request.setAttribute("_start", start);

        // Juger qu'il y a des informations utilisateur dans la session, s'il y en a, les transmettre directement
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("_user");
        if (user == null) {
            // Récupérer le token dans le cookie, interroger les informations de l'utilisateur et les mettre dans la session
            String token = cookieUtil.getCookie(systemConfigService.selectAllConfig().get("cookie_name").toString());
            if (!StringUtils.isEmpty(token)) {
                // Vérifie si l'utilisateur existe en fonction du token
                user = userService.selectByToken(token);
                if (user != null) {
                    // L'utilisateur existe pour écrire une session, le cookie est alors donné passage
                    session.setAttribute("_user", user);
                    cookieUtil.setCookie(systemConfigService.selectAllConfig().get("cookie_name").toString(), user.getToken());
                }
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        if (!HttpUtil.isApiRequest(request) && modelAndView != null) {
            modelAndView.addObject("site", systemConfigService.selectAllConfig());
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        long start = (long) request.getAttribute("_start");
        String actionName = request.getRequestURI();
        String clientIp = IpUtil.getIpAddr(request);
        StringBuilder logString = new StringBuilder();
        logString.append(clientIp).append("|").append(actionName).append("|");
        Map<String, String[]> params = request.getParameterMap();
        params.forEach((key, value) -> {
            logString.append(key);
            logString.append("=");
            for (String paramString : value) {
                logString.append(paramString);
            }
            logString.append("|");
        });
        long executionTime = System.currentTimeMillis() - start;
        logString.append("excitation=").append(executionTime).append("ms");
        log.info(logString.toString());
    }
}
