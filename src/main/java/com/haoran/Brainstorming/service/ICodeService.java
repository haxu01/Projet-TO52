package com.haoran.Brainstorming.service;

import com.haoran.Brainstorming.model.Code;

public interface ICodeService {
    Code selectByCode(String _code);

    // Interroger le code inutilisé
    Code selectNotUsedCode(Integer userId, String email, String mobile);

    // Créer un enregistrement de code de vérification
    Code createCode(Integer userId, String email, String mobile);

    // Vérifier le code de vérification de l'e-mail
    Code validateCode(Integer userId, String email, String mobile, String _code);

    void update(Code code);

    // supprimer les enregistrements de commentaires en fonction de l'identifiant de l'utilisateur
    void deleteByUserId(Integer userId);
}
