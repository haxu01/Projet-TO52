package com.haoran.Brainstorming.util.identicon.generator;

import java.awt.*;


public interface IBaseGenerator {
    /**
     *
     * @param hash
     * @return
     */
    public boolean[][] getBooleanValueArray(String hash);


    /**
     *
     * @return
     */
    public Color getBackgroundColor();


    /**
     *
     * @return
     */
    public Color getForegroundColor();
}
