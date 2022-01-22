package com.haoran.Brainstorming.model.vo;

import com.haoran.Brainstorming.model.Comment;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;

public class CommentsByTopic extends Comment implements Serializable {
    private static final long serialVersionUID = 8082073760910701836L;
    // La structure de données d'un seul objet de la liste de commentaires sous le sujet

    private String username;
    private String avatar;
    // Le niveau du commentaire, si le sujet est commenté directement, le calque est 0. Si le commentaire est répondu, le calque de la réponse courante est le calque+1 de l'objet commentaire
    private Integer layer;

    private LinkedHashMap<Integer, List<CommentsByTopic>> children;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Integer getLayer() {
        return layer;
    }

    public void setLayer(Integer layer) {
        this.layer = layer;
    }

    public LinkedHashMap<Integer, List<CommentsByTopic>> getChildren() {
        return children;
    }

    public void setChildren(LinkedHashMap<Integer, List<CommentsByTopic>> children) {
        this.children = children;
    }
}
