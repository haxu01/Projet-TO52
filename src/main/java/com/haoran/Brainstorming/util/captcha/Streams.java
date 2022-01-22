package com.haoran.Brainstorming.util.captcha;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Streams {
    /**
     *fermer le flux d'entrée
     * @param in
     */
    public static void close(InputStream in) {
        if (in != null) {
            try {
                in.close();
            } catch (IOException ioex) {
                // ignore
            }
        }
    }

    /**
     *fermer le flux de sortie
     * @param out
     */
    public static void close(OutputStream out) {
        if (out != null) {
            try {
                out.flush();
            } catch (IOException ioex) {
                // ignore
            }
            try {
                out.close();
            } catch (IOException ioex) {
                // ignore
            }
        }
    }
}
