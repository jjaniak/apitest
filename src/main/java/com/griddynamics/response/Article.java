package com.griddynamics.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.sql.Timestamp;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)

public class Article {
    public String slug;
    public String title;
    public String description;
    public String body;
    public List <String> tagList;
    public Timestamp createdAt;
    public Timestamp updatetAt;
    public boolean favorited;
    public int favoritesCount;
    public Author author;
}
