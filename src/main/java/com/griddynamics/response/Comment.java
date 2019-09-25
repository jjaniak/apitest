package com.griddynamics.response;

import java.sql.Timestamp;

public class Comment {
    public String id;
    public Timestamp createdAt;
    public Timestamp updatedAt;
    public String body;
    public Author author;
}
