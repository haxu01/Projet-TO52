package com.haoran.Brainstorming.controller.front;

import com.haoran.Brainstorming.controller.api.BaseApiController;
import com.haoran.Brainstorming.util.FileUtil;
import com.haoran.Brainstorming.util.captcha.Captcha;
import com.haoran.Brainstorming.util.captcha.GifCaptcha;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


@Controller
@RequestMapping("/common")
public class CommonController extends BaseApiController {

    @Resource
    private FileUtil fileUtil;

    //le code de véréfication en GIF
    @GetMapping("/captcha")
    public void captcha(HttpServletResponse response, HttpSession session) throws IOException {
        Captcha captcha = new GifCaptcha();
        captcha.out(response.getOutputStream());
        String text = captcha.text();
        session.setAttribute("_captcha", text);
    }

    @GetMapping("/show_img")
    public String showOssImg(String name) {
        return "redirect:" + fileUtil.generatorOssUrl(name);
    }

}
