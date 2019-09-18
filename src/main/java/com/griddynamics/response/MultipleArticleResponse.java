package com.griddynamics.response;

import java.util.List;

public class MultipleArticleResponse {
    private List <Article> articles;
    private int articlesCount;

    public MultipleArticleResponse(List<Article> articles, int articlesCount) {
        this.articles = articles;
        this.articlesCount = articlesCount;
    }
}
