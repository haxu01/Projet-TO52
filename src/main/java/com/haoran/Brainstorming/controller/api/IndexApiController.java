package com.haoran.Brainstorming.controller.api;

import com.haoran.Brainstorming.exception.ApiAssert;
import com.haoran.Brainstorming.model.Code;
import com.haoran.Brainstorming.model.Tag;
import com.haoran.Brainstorming.model.User;
import com.haoran.Brainstorming.service.*;
import com.haoran.Brainstorming.util.bcrypt.BCryptPasswordEncoder;
import com.haoran.Brainstorming.util.*;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.*;


@RestController
@RequestMapping("/api")
public class IndexApiController extends BaseApiController {

    @Resource
    private IUserService userService;
    @Resource
    private ISystemConfigService systemConfigService;
    @Resource
    private CookieUtil cookieUtil;
    @Resource
    private ITopicService topicService;
    @Resource
    private ITagService tagService;
    @Resource
    private FileUtil fileUtil;
    @Resource
    private ICodeService codeService;

    //interface de la page d'accueil
    @GetMapping({"/", "/index"})
    public Result index(@RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "all") String
            tab) {
        MyPage<Map<String, Object>> page = topicService.selectAll(pageNo, tab);
        for (Map<String, Object> map : page.getRecords()) {
            Object content = map.get("content");
            map.put("content", StringUtils.isEmpty(content) ? null : SensitiveWordUtil.replaceSensitiveWord(content
                    .toString(), "*", SensitiveWordUtil.MinMatchType));
        }
        return success(page);
    }

    // Retourne le sujet sous la tag selon le nom de la tag
    @GetMapping("/tag/{name}")
    public Result topicsByTagName(@RequestParam(defaultValue = "1") Integer pageNo, @PathVariable String name) {
        Tag tag = tagService.selectByName(name);
        if (tag == null) {
            return error("error");
        } else {
            MyPage<Map<String, Object>> iPage = tagService.selectTopicByTagId(tag.getId(), pageNo);
            Map<String, Object> result = new HashMap<>();
            result.put("tag", tag);
            result.put("page", iPage);
            return success(result);
        }
    }


    // gérer l'interface de connexion
    @PostMapping("/login")
    public Result login(@RequestBody Map<String, String> body, HttpSession session) {
        String username = body.get("username");
        String password = body.get("password");
        String captcha = body.get("captcha");
        String _captcha = (String) session.getAttribute("_captcha");
        ApiAssert.notTrue(_captcha == null || StringUtils.isEmpty(captcha), "Captcha?");
        ApiAssert.notTrue(!_captcha.equalsIgnoreCase(captcha), "Incorrect");
        ApiAssert.notEmpty(username, "Username?");
        ApiAssert.notEmpty(password, "Password?");
        User user = userService.selectByUsername(username);
        ApiAssert.notNull(user, "Incorrect");
        ApiAssert.isTrue(new BCryptPasswordEncoder().matches(password, user.getPassword()), "Incorrect");
        return this.doUserStorage(session, user);
    }


    // gérer l'interface enregistrée
    @PostMapping("/register")
    public Result register(@RequestBody Map<String, String> body, HttpSession session) {
        String username = body.get("username");
        String password = body.get("password");
        String email = body.get("email");
        String captcha = body.get("captcha");
        String _captcha = (String) session.getAttribute("_captcha");
        ApiAssert.notTrue(_captcha == null || StringUtils.isEmpty(captcha), "Captcha?");
        ApiAssert.notTrue(!_captcha.equalsIgnoreCase(captcha), "error");
        ApiAssert.notEmpty(username, "Username?");
        ApiAssert.notEmpty(password, "Password");
        ApiAssert.notEmpty(email, "Email");
        ApiAssert.isTrue(StringUtil.check(username, StringUtil.USERNAMEREGEX), "Username error");
        ApiAssert.isTrue(StringUtil.check(email, StringUtil.EMAILREGEX), "Email error");
        User user = userService.selectByUsername(username);
        ApiAssert.isNull(user, "Username déjà exist");
        User emailUser = userService.selectByEmail(email);
        ApiAssert.isNull(emailUser, "Email déjà exist");
        user = userService.addUser(username, password, null, email, null, null, true);
        return this.doUserStorage(session, user);
    }

    // Une fois la connexion réussie, la logique de traitement est la même, ici est extraite pour encapsuler une méthode de traitement
    private Result doUserStorage(HttpSession session, User user) {
        // Écrit les informations de l'utilisateur dans la session
        if (session != null) {
            session.setAttribute("_user", user);
            session.removeAttribute("_captcha");
        }
        // écrit le token utilisateur dans le cookie
        cookieUtil.setCookie(systemConfigService.selectAllConfig().get("cookie_name").toString(), user.getToken());
        Map<String, Object> map = new HashMap<>();
        map.put("user", user);
        map.put("token", user.getToken());
        return success(map);
    }

    //interface du tag
    @GetMapping("/tags")
    public Result tags(@RequestParam(defaultValue = "1") Integer pageNo) {
        return success(tagService.selectAll(pageNo, null, null));
    }

    //télécharger un image
    @PostMapping("/upload")
    @ResponseBody
    public Result upload(@RequestParam("file") MultipartFile[] files, String type, HttpSession session) {
        User user = getApiUser();
        ApiAssert.notEmpty(type, "File type error");
        Map<String, Object> resultMap = new HashMap<>();
        List<String> urls = new ArrayList<>();
        List<String> errors = new ArrayList<>();
        for (int i = 0; i < files.length; i++) {
            String url;
            MultipartFile file = files[i];
            String suffix = "." + Objects.requireNonNull(file.getContentType()).split("/")[1];
            if (!Arrays.asList(".jpg", ".png", ".gif", ".jpeg", ".mp4").contains(suffix.toLowerCase())) {
                errors.add("File type error");
                continue;
            }
            long size = file.getSize();
            // Vérifier la taille du fichier en fonction des différents types de téléchargement
            if (type.equalsIgnoreCase("video")) {
                long uploadVideoSizeLimit = Long.parseLong(systemConfigService.selectAllConfig().get("upload_video_size_limit").toString());
                if (size > uploadVideoSizeLimit * 1024 * 1024) {
                    errors.add("La taille du fichier doit être inférieure à " + uploadVideoSizeLimit + "MB");
                    continue;
                }
            } else {
                long uploadImageSizeLimit = Long.parseLong(systemConfigService.selectAllConfig().get("upload_image_size_limit").toString());
                if (size > uploadImageSizeLimit * 1024 * 1024) {
                    errors.add("La taille du fichier doit être inférieure à" + uploadImageSizeLimit + "MB 以内");
                    continue;
                }
            }
            if (type.equalsIgnoreCase("avatar")) {
                //Obtenir l'url accessible après le téléchargement
                url = fileUtil.upload(file, "avatar", "avatar/" + user.getUsername());
                if (url != null) {
                    // Requête des dernières informations de l'utilisateur courant
                    User user1 = userService.selectById(user.getId());
                    user1.setAvatar(url);
                    // enregistre le nouvel avatar de l'utilisateur
                    userService.update(user1);
                    // Mettre à jour les dernières informations utilisateur dans la session
                    if (session != null) session.setAttribute("_user", user1);
                }
            } else if (type.equalsIgnoreCase("topic")) {
                url = fileUtil.upload(file, null, "topic/" + user.getUsername());
            } else if (type.equalsIgnoreCase("video")) {
                url = fileUtil.upload(file, null, "video/" + user.getUsername());
            } else {
                errors.add("le type du fichier incorrect");
                continue;
            }
            if (url == null) {
                errors.add("error");
                continue;
            }
            urls.add(url);
        }
        resultMap.put("urls", urls);
        resultMap.put("errors", errors);
        return success(resultMap);
    }

}
