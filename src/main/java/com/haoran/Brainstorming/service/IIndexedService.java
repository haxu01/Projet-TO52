package com.haoran.Brainstorming.service;


public interface IIndexedService {

    // indexer tous les sujets
    void indexAllTopic();

    // indexer les sujets
    void indexTopic(String id, String title, String content);

    // supprimer l'index du sujet
    void deleteTopicIndex(String id);

    // supprimer tous les index de sujet
    void batchDeleteIndex();

}
