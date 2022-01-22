package com.haoran.Brainstorming.controller.api;

import com.haoran.Brainstorming.exception.ApiAssert;
import com.haoran.Brainstorming.model.Code;
import com.haoran.Brainstorming.model.User;
import com.haoran.Brainstorming.service.ICodeService;
import com.haoran.Brainstorming.service.ISystemConfigService;
import com.haoran.Brainstorming.service.IUserService;
import com.haoran.Brainstorming.util.Result;
import com.haoran.Brainstorming.util.StringUtil;
import com.haoran.Brainstorming.util.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Map;


@RestController
@RequestMapping("/api/settings")
public class SettingsApiController extends BaseApiController {

    @Resource
    private IUserService userService;
    @Resource
    private ICodeService codeService;
    @Resource
    private ISystemConfigService systemConfigService;
    // mettre à jour le profil utilisateur
    @PutMapping
    public Result update(@RequestBody Map<String, String> body, HttpSession session) {
        User user = getApiUser();
        String telegramName = body.get("telegramName");
        String website = body.get("website");
        String bio = body.get("bio");
        Boolean emailNotification = Boolean.parseBoolean(body.get("emailNotification"));
        // Requête des dernières informations de l'utilisateur courant
        User user1 = userService.selectById(user.getId());
        user1.setTelegramName(telegramName);
        user1.setWebsite(website);
        user1.setBio(bio);
        user1.setEmailNotification(emailNotification);
        userService.update(user1);

        User user2 = getUser();
        if (user2 != null) {
            user2.setBio(bio);
            session.setAttribute("_user", user2);
        }
        return success();
    }


    @PutMapping("/updateEmail")
    public Result updateEmail(@RequestBody Map<String, String> body, HttpSession session) {
        User user = getApiUser();
        String email = body.get("email");
        String code = body.get("code");
        ApiAssert.notEmpty(email, "Email ");
        ApiAssert.isTrue(StringUtil.check(email, StringUtil.EMAILREGEX), "error");
        Code code1 = codeService.validateCode(user.getId(), email, null, code);
        if (code1 == null) return error("error");
        code1.setUsed(true);
        codeService.update(code1);
        User user1 = userService.selectById(user.getId());
        user1.setEmail(email);
        if (!user1.getActive()) user1.setActive(true);
        userService.update(user1);
        User _user = getUser();
        _user.setEmail(email);
        session.setAttribute("_user", _user);
        return success();
    }

    @PutMapping("/updatePassword")
    public Result updatePassword(@RequestBody Map<String, String> body) {
        User user = getApiUser();
        String oldPassword = body.get("oldPassword");
        String newPassword = body.get("newPassword");
        ApiAssert.notTrue(oldPassword.equals(newPassword), "error");
        ApiAssert.isTrue(new BCryptPasswordEncoder().matches(oldPassword, user.getPassword()), "error");
        User user1 = userService.selectById(user.getId());
        user1.setPassword(new BCryptPasswordEncoder().encode(newPassword));
        userService.update(user1);
        return success();
    }

    @GetMapping("/refreshToken")
    public Result refreshToken(HttpSession session) {
        User user = getApiUser();
        String token = StringUtil.uuid();
        user.setToken(token);
        userService.update(user);
        User _user = getUser();
        _user.setToken(token);
        session.setAttribute("_user", _user);
        return success(token);
    }

}
