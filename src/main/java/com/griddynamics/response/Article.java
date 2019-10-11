package com.griddynamics.response;

import java.sql.Timestamp;
import java.util.List;

public class Article {
    public String slug;
    public String title;
    public String description;
    public String body;
    public List <String> tagList;
    public Timestamp createdAt;
    public Timestamp updatedAt;
    public boolean favorited;
    public int favoritesCount;
    public Author author;
}
