package com.haoran.Brainstorming.util.identicon;

import com.haoran.Brainstorming.service.ISystemConfigService;
import com.haoran.Brainstorming.util.HashUtil;
import com.haoran.Brainstorming.util.MD5Util;
import com.haoran.Brainstorming.util.StringUtil;
import com.haoran.Brainstorming.util.identicon.generator.IBaseGenerator;
import com.haoran.Brainstorming.util.identicon.generator.impl.MyGenerator;
import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Base64;


@Component
public class Identicon {

    private final Logger log = LoggerFactory.getLogger(Identicon.class);

    private final IBaseGenerator generator;
    @Resource
    private ISystemConfigService systemConfigService;

    public Identicon() {
        this.generator = new MyGenerator();
    }

    public BufferedImage create(String hash, int size) {
        Preconditions.checkArgument(size > 0 && !StringUtils.isEmpty(hash));

        boolean[][] array = generator.getBooleanValueArray(hash);

        int ratio = size / 6;

        BufferedImage identicon = new BufferedImage(ratio * 6, ratio * 6, BufferedImage.TYPE_INT_ARGB);
        Graphics graphics = identicon.getGraphics();

        graphics.setColor(generator.getBackgroundColor());
        graphics.fillRect(0, 0, identicon.getWidth(), identicon.getHeight());

        graphics.setColor(generator.getForegroundColor());
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (array[i][j]) {
                    graphics.fillRect(j * ratio + 35, i * ratio + 35, ratio, ratio);
                }
            }
        }

        return identicon;
    }

    public static String imgToBase64String(RenderedImage img, String formatName) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            ImageIO.write(img, formatName, os);
            return Base64.getEncoder().encodeToString(os.toByteArray());
        } catch (final IOException ioe) {
            throw new UncheckedIOException(ioe);
        }
    }

    public String generator() {
        Identicon identicon = new Identicon();
        String md5 = MD5Util.getMD5String(StringUtil.randomString(6));
        BufferedImage image = identicon.create(md5, 300);
        return "data:image/png;base64," + imgToBase64String(image, "png");
    }

    public String generator(String username) {
        Identicon identicon = new Identicon();
        String md5 = HashUtil.md5(StringUtil.randomString(6));
        BufferedImage image = identicon.create(md5, 420);
        return saveFile(username, image);
    }

    public String saveFile(String username, BufferedImage image) {
        String fileName = "avatar.png";
        String userAvatarPath = "avatar/" + username + "/";
        try {
            File file = new File(systemConfigService.selectAllConfig().get("upload_path").toString() + userAvatarPath);
            if (!file.exists()) file.mkdirs();
            File file1 = new File(systemConfigService.selectAllConfig().get("upload_path").toString() + userAvatarPath + fileName);
            if (!file1.exists()) file1.createNewFile();
            ImageIO.write(image, "PNG", file1);
            return systemConfigService.selectAllConfig().get("static_url").toString() + userAvatarPath + fileName;
        } catch (IOException e) {
            log.error(e.getLocalizedMessage());
        }
        return null;
    }

}
