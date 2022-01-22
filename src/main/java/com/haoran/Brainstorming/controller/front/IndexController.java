package com.haoran.Brainstorming.controller.front;

import com.haoran.Brainstorming.model.User;
import com.haoran.Brainstorming.service.ICodeService;
import com.haoran.Brainstorming.service.ISystemConfigService;
import com.haoran.Brainstorming.service.IUserService;
import com.haoran.Brainstorming.util.CookieUtil;
import com.haoran.Brainstorming.util.SecurityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Locale;


@Controller
public class IndexController extends BaseController {

    private Logger log = LoggerFactory.getLogger(IndexController.class);

    @Resource
    private CookieUtil cookieUtil;
    @Resource
    private ISystemConfigService systemConfigService;
    @Resource
    private IUserService userService;

    //la page d'accueil
    @GetMapping({"/", "/index", "/index.html"})
    public String index(@RequestParam(defaultValue = "all") String tab, @RequestParam(defaultValue = "1") Integer
            pageNo, Boolean active, Model model) {
        model.addAttribute("tab", tab);
        model.addAttribute("active", active);
        model.addAttribute("pageNo", pageNo);

        return render("index");
    }

    @GetMapping("/top100")
    public String top100() {
        return render("top100");
    }

    @GetMapping("/settings")
    public String settings(HttpSession session, Model model) {
        User user = userService.selectById(getUser().getId());
        model.addAttribute("user", user);
        return render("user/settings");
    }

    @GetMapping("/tags")
    public String tags(@RequestParam(defaultValue = "1") Integer pageNo, Model model) {
        model.addAttribute("pageNo", pageNo);
        return render("tag/tags");
    }

    //connexion
    @GetMapping("/login")
    public String login() {
        User user = getUser();
        if (user != null) return redirect("/");
        return render("login");
    }
    //inscription
    @GetMapping("/register")
    public String register() {
        User user = getUser();
        if (user != null) return redirect("/");
        return render("register");
    }

    //notification
    @GetMapping("/notifications")
    public String notifications() {
        return render("notifications");
    }


    @GetMapping("/logout")
    public String logout(HttpSession session) {
        User user = getUser();
        if (user != null) {
            userService.delRedisUser(user);
            session.removeAttribute("_user");
            cookieUtil.clearCookie(systemConfigService.selectAllConfig().get("cookie_name").toString());
        }
        return redirect("/");
    }

    @GetMapping("/search")
    public String search(@RequestParam(defaultValue = "1") Integer pageNo, @RequestParam String keyword, Model model) {
        model.addAttribute("pageNo", pageNo);
        model.addAttribute("keyword", SecurityUtil.sanitizeInput(keyword));
        return render("search");
    }

    @GetMapping("changeLanguage")
    public String changeLanguage(String lang, HttpSession session, HttpServletRequest request) {
        String referer = request.getHeader("referer");
        if ("zh".equals(lang)) {
            session.setAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME, Locale.SIMPLIFIED_CHINESE);
        } else if ("en".equals(lang)) {
            session.setAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME, Locale.US);
        }
        return StringUtils.isEmpty(referer) ? redirect("/") : redirect(referer);
    }

}
