package com.haoran.Brainstorming.service.impl;

import com.haoran.Brainstorming.service.IIndexedService;
import org.springframework.stereotype.Service;


@Service
public class IndexedService implements IIndexedService {

    @Override
    public void indexAllTopic() {
    }

    @Override
    public void indexTopic(String id, String title, String content) {
    }

    @Override
    public void deleteTopicIndex(String id) {
    }

    @Override
    public void batchDeleteIndex() {
    }
}
