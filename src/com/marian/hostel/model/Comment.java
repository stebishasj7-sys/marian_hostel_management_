package com.marian.hostel.model;

import java.util.*;

public class Comment {
    private String id;
    private String userId;
    private String userName;
    private String userRole;
    private String text;
    private long timestamp;
    private String dateFormatted;

    public Comment() {
        this.id = UUID.randomUUID().toString();
        this.timestamp = System.currentTimeMillis();
    }

    public Comment(String userId, String userName, String userRole, String text, String dateFormatted) {
        this.id = UUID.randomUUID().toString();
        this.userId = userId;
        this.userName = userName;
        this.userRole = userRole;
        this.text = text;
        this.timestamp = System.currentTimeMillis();
        this.dateFormatted = dateFormatted;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", id);
        map.put("userId", userId != null ? userId : "");
        map.put("userName", userName != null ? userName : "");
        map.put("userRole", userRole != null ? userRole : "");
        map.put("text", text != null ? text : "");
        map.put("timestamp", timestamp);
        map.put("dateFormatted", dateFormatted != null ? dateFormatted : "");
        return map;
    }

    public static Comment fromMap(Map<String, Object> map) {
        Comment c = new Comment();
        if (map.containsKey("id")) c.setId((String) map.get("id"));
        if (map.containsKey("userId")) c.setUserId((String) map.get("userId"));
        if (map.containsKey("userName")) c.setUserName((String) map.get("userName"));
        if (map.containsKey("userRole")) c.setUserRole((String) map.get("userRole"));
        if (map.containsKey("text")) c.setText((String) map.get("text"));
        if (map.containsKey("timestamp") && map.get("timestamp") instanceof Number) {
            c.setTimestamp(((Number) map.get("timestamp")).longValue());
        }
        if (map.containsKey("dateFormatted")) c.setDateFormatted((String) map.get("dateFormatted"));
        return c;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
    public String getUserRole() { return userRole; }
    public void setUserRole(String userRole) { this.userRole = userRole; }
    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
    public String getDateFormatted() { return dateFormatted; }
    public void setDateFormatted(String dateFormatted) { this.dateFormatted = dateFormatted; }
}
