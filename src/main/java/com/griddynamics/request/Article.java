package com.griddynamics.request;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Article {
    public String title;
    public String description;
    public String body;
    public List<String> tagList = new ArrayList<>();

    public Article(String title, String description, String body) {
        this.title = title;
        this.description = description;
        this.body = body;
    }

    public Article withTagList(String... tags) {
        this.tagList.addAll(Arrays.asList(tags));
        return this;
    }
}