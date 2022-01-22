package com.haoran.Brainstorming.controller.front;

import com.haoran.Brainstorming.model.OAuthUser;
import com.haoran.Brainstorming.model.User;
import com.haoran.Brainstorming.service.ICollectService;
import com.haoran.Brainstorming.service.IOAuthUserService;
import com.haoran.Brainstorming.service.IUserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/user")
public class UserController extends BaseController {

    @Resource
    private IUserService userService;
    @Resource
    private ICollectService collectService;
    @Resource
    private IOAuthUserService oAuthUserService;

    @GetMapping("/{username}")
    public String profile(@PathVariable String username, Model model) {
        // Interroger les informations personnelles de l'utilisateur
        User user = userService.selectByUsername(username);
        // Interroger les informations de l'utilisateur pour la connexion oauth
        List<OAuthUser> oAuthUsers = oAuthUserService.selectByUserId(user.getId());
        Integer collectCount = collectService.countByUserId(user.getId());

        List<String> logins = oAuthUsers.stream().filter(oAuthUser -> oAuthUser.getType().equals("GITHUB")).map
                (OAuthUser::getLogin).collect(Collectors.toList());
        if (logins.size() > 0) {
            model.addAttribute("githubLogin", logins.get(0));
        }

        model.addAttribute("user", user);
        model.addAttribute("username", username);
        model.addAttribute("oAuthUsers", oAuthUsers);
        model.addAttribute("collectCount", collectCount);
        return render("user/profile");
    }

    @GetMapping("/{username}/topics")
    public String topics(@PathVariable String username, @RequestParam(defaultValue = "1") Integer pageNo, Model model) {
        model.addAttribute("username", username);
        model.addAttribute("pageNo", pageNo);
        return render("user/topics");
    }

    @GetMapping("/{username}/comments")
    public String comments(@PathVariable String username, @RequestParam(defaultValue = "1") Integer pageNo, Model model) {
        model.addAttribute("username", username);
        model.addAttribute("pageNo", pageNo);
        return render("user/comments");
    }

    @GetMapping("/{username}/collects")
    public String collects(@PathVariable String username, @RequestParam(defaultValue = "1") Integer pageNo, Model model) {
        model.addAttribute("username", username);
        model.addAttribute("pageNo", pageNo);
        return render("user/collects");
    }
}
