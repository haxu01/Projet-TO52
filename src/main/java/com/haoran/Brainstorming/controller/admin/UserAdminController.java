package com.haoran.Brainstorming.controller.admin;

import com.haoran.Brainstorming.model.User;
import com.haoran.Brainstorming.service.IUserService;
import com.haoran.Brainstorming.util.Result;
import com.haoran.Brainstorming.util.SecurityUtil;
import com.haoran.Brainstorming.util.StringUtil;
import com.haoran.Brainstorming.util.bcrypt.BCryptPasswordEncoder;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


@Controller
@RequestMapping("/admin/user")
public class UserAdminController extends BaseAdminController {

    @Resource
    private IUserService userService;

    // gestion des utilisateurs frontaux
    @RequiresPermissions("user:list")
    @GetMapping("/list")
    public String list(@RequestParam(defaultValue = "1") Integer pageNo, String username, Model model) {
        username= SecurityUtil.sanitizeInput(username);
        IPage<User> iPage = userService.selectAll(pageNo, username);
        model.addAttribute("page", iPage);
        model.addAttribute("username", username);
        return "admin/user/list";
    }

    // Modifier l'utilisateur
    @RequiresPermissions("user:edit")
    @GetMapping("/edit")
    public String edit(Integer id, Model model) {
        model.addAttribute("user", userService.selectByIdNoCatch(id));
        return "admin/user/edit";
    }

    // enregistre les informations utilisateur modifiées
    @RequiresPermissions("user:edit")
    @PostMapping("/edit")
    @ResponseBody
    public Result update(User user) {
        // Si le mot de passe n'est pas vide, chiffrez-le et enregistrez-le
        if (!StringUtils.isEmpty(user.getPassword())) {
            user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        } else {
            user.setPassword(null);
        }
        if (user.getEmailNotification() == null) user.setEmailNotification(false);
        userService.update(user);
        return success();
    }

    // supprimer les utilisateurs
    @RequiresPermissions("user:delete")
    @GetMapping("/delete")
    @ResponseBody
    public Result delete(Integer id) {
        userService.deleteUser(id);
        return success();
    }

    // rafraîchir le token
    @RequiresPermissions("user:refresh_token")
    @GetMapping("/refreshToken")
    @ResponseBody
    public Result refreshToken(Integer id) {
        User user = userService.selectByIdNoCatch(id);
        user.setToken(StringUtil.uuid());
        userService.update(user);
        return success(user.getToken());
    }
}
