package com.haoran.Brainstorming.util.captcha;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.OutputStream;

import static com.haoran.Brainstorming.util.captcha.Randoms.num;


public class GifCaptcha extends Captcha {
    public GifCaptcha() {
    }

    public GifCaptcha(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public GifCaptcha(int width, int height, int len) {
        this(width, height);
        this.len = len;
    }

    public GifCaptcha(int width, int height, int len, Font font) {
        this(width, height, len);
        this.font = font;
    }

    @Override
    public void out(OutputStream os) {
        try {
            GifEncoder gifEncoder = new GifEncoder();
            // génère des caractères
            gifEncoder.start(os);
            gifEncoder.setQuality(180);
            gifEncoder.setDelay(100);
            gifEncoder.setRepeat(0);
            BufferedImage frame;
            char[] rands = alphas();
            Color fontcolor[] = new Color[len];
            for (int i = 0; i < len; i++) {
                fontcolor[i] = new Color(20 + num(110), 20 + num(110), 20 + num(110));
            }
            for (int i = 0; i < len; i++) {
                frame = graphicsImage(fontcolor, rands, i);
                gifEncoder.addFrame(frame);
                frame.flush();
            }
            gifEncoder.finish();
        } finally {
            Streams.close(os);
        }

    }

    /**
     *Dessiner une carte de code aléatoire
     *
     * @param fontcolor
     * @param strs
     * @param flag
     * @return BufferedImage
     */
    private BufferedImage graphicsImage(Color[] fontcolor, char[] strs, int flag) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = (Graphics2D) image.getGraphics();
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, width, height);
        AlphaComposite ac3;
        int h = height - ((height - font.getSize()) >> 1);
        int w = width / len;
        g2d.setFont(font);
        for (int i = 0; i < len; i++) {
            ac3 = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, getAlpha(flag, i));
            g2d.setComposite(ac3);
            g2d.setColor(fontcolor[i]);
            g2d.drawOval(num(width), num(height), 5 + num(10), 5 + num(10));
            g2d.drawString(strs[i] + "", (width - (len - i) * w) + (w - font.getSize()) + 1, h - 4);
        }
        g2d.dispose();
        return image;
    }

    /**
     * Obtener la transparence, de 0 à 1, calculer automatiquement la taille du pas
     * @return float
     */
    private float getAlpha(int i, int j) {
        int num = i + j;
        float r = (float) 1 / len, s = (len + 1) * r;
        return num > len ? (num * r - s) : num * r;
    }

}
