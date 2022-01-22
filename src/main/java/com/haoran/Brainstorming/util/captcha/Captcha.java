package com.haoran.Brainstorming.util.captcha;

import java.awt.*;
import java.io.OutputStream;

import static com.haoran.Brainstorming.util.captcha.Randoms.alpha;
import static com.haoran.Brainstorming.util.captcha.Randoms.num;

public abstract class Captcha {

    protected Font font = new Font("Verdana", Font.ITALIC | Font.BOLD, 28);
    protected int len = 4;
    protected int width = 120;
    protected int height = 32;
    private String chars = null;

    //Générer un tableau de caractères aléatoires
    protected char[] alphas() {
        char[] cs = new char[len];
        for (int i = 0; i < len; i++) {
            cs[i] = alpha();
        }
        chars = new String(cs);
        return cs;
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public int getLen() {
        return len;
    }

    public void setLen(int len) {
        this.len = len;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    //Obtenir une couleur aléatoire pour une plage donnée
    protected Color color(int fc, int bc) {
        if (fc > 255) fc = 255;
        if (bc > 255) bc = 255;
        int r = fc + num(bc - fc);
        int g = fc + num(bc - fc);
        int b = fc + num(bc - fc);
        return new Color(r, g, b);
    }

    //Sortir captcha, méthode abstraite, implémentée par des sous-classes
    public abstract void out(OutputStream os);

    //obtenir une chaîne aléatoire
    public String text() {
        return chars;
    }
}
