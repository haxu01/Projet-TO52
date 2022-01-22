package com.haoran.Brainstorming.controller.admin;

import com.haoran.Brainstorming.service.ICommentService;
import com.haoran.Brainstorming.service.ITagService;
import com.haoran.Brainstorming.service.ITopicService;
import com.haoran.Brainstorming.service.IUserService;
import com.sun.management.OperatingSystemMXBean;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.lang.management.ManagementFactory;

@Controller
public class IndexAdminController extends BaseAdminController {

    private final Logger log = LoggerFactory.getLogger(IndexAdminController.class);

    @Resource
    private ITopicService topicService;
    @Resource
    private ITagService tagService;
    @Resource
    private ICommentService commentService;
    @Resource
    private IUserService userService;

    @RequiresUser
    @GetMapping({"/admin/", "/admin/index"})
    public String index(Model model) {
        // Interroger le nouveau sujet du jour
        model.addAttribute("topic_count", topicService.countToday());
        // Interroger les tags ajoutées le jour
        model.addAttribute("tag_count", tagService.countToday());
        // Interroger les nouveaux commentaires du jour
        model.addAttribute("comment_count", commentService.countToday());
        // Interroger les nouveaux utilisateurs du jour
        model.addAttribute("user_count", userService.countToday());
        // Récupère le nom du système d'exploitation
        model.addAttribute("os_name", System.getProperty("os.name"));

        // RAM
        int kb = 1024;
        OperatingSystemMXBean osmxb = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        // mémoire physique totale
        long totalMemorySize = osmxb.getTotalPhysicalMemorySize() / kb;
        // mémoire physique utilisée
        long usedMemory = (osmxb.getTotalPhysicalMemorySize() - osmxb.getFreePhysicalMemorySize()) / kb;
        // Récupère la charge du processeur du système
        double systemCpuLoad = osmxb.getSystemCpuLoad();
        // Récupère la charge du thread jvm
        double processCpuLoad = osmxb.getProcessCpuLoad();

        model.addAttribute("totalMemorySize", totalMemorySize);
        model.addAttribute("usedMemory", usedMemory);
        model.addAttribute("systemCpuLoad", systemCpuLoad);
        model.addAttribute("processCpuLoad", processCpuLoad);

        return "admin/index";
    }

    // arrière-plan de connexion
    @GetMapping("/adminlogin")
    public String adminlogin() {
        return "admin/login";
    }

    // gère la logique de connexion en arrière-plan
    @PostMapping("/admin/login")
    public String adminlogin(String username, String password, String code, HttpSession session,
                             @RequestParam(defaultValue = "0") Boolean rememberMe,
                             HttpServletRequest request, RedirectAttributes redirectAttributes) {
        String url = WebUtils.getSavedRequest(request) == null ? "/admin/index" : WebUtils.getSavedRequest(request).getRequestUrl();
        String captcha = (String) session.getAttribute("_captcha");
        if (captcha == null || StringUtils.isEmpty(code) || !captcha.equalsIgnoreCase(code)) {
            redirectAttributes.addFlashAttribute("error", "error");
            redirectAttributes.addFlashAttribute("username", username);
            url = "/adminlogin";
        } else {
            try {
                // ajouter les informations d'authentification de l'utilisateur
                Subject subject = SecurityUtils.getSubject();
                UsernamePasswordToken token = new UsernamePasswordToken(username, password, rememberMe);
                //Vérifier, ici vous pouvez intercepter des exceptions, puis renvoyer les informations correspondantes
                subject.login(token);
            } catch (UnknownAccountException e) {
                log.error(e.getMessage());
                redirectAttributes.addFlashAttribute("error", "client n'exist pas");
                redirectAttributes.addFlashAttribute("username", username);
                url = "/adminlogin";
            } catch (IncorrectCredentialsException e) {
                log.error(e.getMessage());
                redirectAttributes.addFlashAttribute("error", "password error");
                redirectAttributes.addFlashAttribute("username", username);
                url = "/adminlogin";
            } catch (AuthenticationException e) {
                log.error(e.getMessage());
                redirectAttributes.addFlashAttribute("error", "error");
                redirectAttributes.addFlashAttribute("username", username);
                url = "/adminlogin";
            }
        }
        return redirect(url);
    }

    @GetMapping("/admin/logout")
    public String logout() {
        SecurityUtils.getSubject().logout();
        return redirect("/adminlogin");
    }

    @GetMapping("/admin/no_auth")
    public String noAuth() {
        return redirect("/adminlogin");
    }
}
