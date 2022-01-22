package com.haoran.Brainstorming.util.identicon.generator.impl;

import com.haoran.Brainstorming.util.identicon.generator.IBaseGenerator;
import com.google.common.base.Preconditions;
import com.google.common.math.DoubleMath;
import org.springframework.util.StringUtils;

import java.awt.*;
import java.math.RoundingMode;
import java.util.Random;

public class DefaultGenerator implements IBaseGenerator {
    private String hash;
    private boolean[][] booleanValueArray;
    private Random random = new Random();

    @Override
    public boolean[][] getBooleanValueArray(String hash) {
        Preconditions.checkArgument(!StringUtils.isEmpty(hash) && hash.length() >= 16, "illegal argument hash:not null "
                + "and size >= 16");

        this.hash = hash;

        boolean[][] array = new boolean[6][5];


        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 5; j++) {
                array[i][j] = false;
            }
        }

        for (int i = 0; i < hash.length(); i += 2) {
            int s = i / 2;

            boolean v = DoubleMath.roundToInt(Integer.parseInt(hash.charAt(i) + "", 16) / 10.0, RoundingMode.HALF_UP) > 0 ?
                    true : false;
            if (s % 3 == 0) {
                array[s / 3][0] = v;
                array[s / 3][4] = v;
            } else if (s % 3 == 1) {
                array[s / 3][1] = v;
                array[s / 3][3] = v;
            } else {
                array[s / 3][2] = v;
            }
        }

        this.booleanValueArray = array;

        return this.booleanValueArray;
    }

    @Override
    public Color getBackgroundColor() {
        return new Color(236, 236, 236);
    }

    @Override
    public Color getForegroundColor() {
        int r = random.nextInt(256);
        int g = random.nextInt(256);
        int b = random.nextInt(256);
        return new Color(r, g, b);
    }
}
