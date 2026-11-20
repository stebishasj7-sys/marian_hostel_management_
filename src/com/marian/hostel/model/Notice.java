package com.marian.hostel.model;

import java.util.*;

public class Notice {
    private String id;
    private String title;
    private String content;
    private String category; // "General", "Emergency", "Mess", "Fee", "Maintenance"
    private boolean isEmergency;
    private String postedBy;
    private String postedByName;
    private long timestamp;
    private String dateFormatted;

    public Notice() {
        this.id = UUID.randomUUID().toString();
        this.timestamp = System.currentTimeMillis();
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", id);
        map.put("title", title != null ? title : "");
        map.put("content", content != null ? content : "");
        map.put("category", category != null ? category : "General");
        map.put("isEmergency", isEmergency);
        map.put("postedBy", postedBy != null ? postedBy : "");
        map.put("postedByName", postedByName != null ? postedByName : "Warden");
        map.put("timestamp", timestamp);
        map.put("dateFormatted", dateFormatted != null ? dateFormatted : "");
        return map;
    }

    public static Notice fromMap(Map<String, Object> map) {
        Notice n = new Notice();
        if (map.containsKey("id")) n.setId((String) map.get("id"));
        if (map.containsKey("title")) n.setTitle((String) map.get("title"));
        if (map.containsKey("content")) n.setContent((String) map.get("content"));
        if (map.containsKey("category")) n.setCategory((String) map.get("category"));
        if (map.containsKey("isEmergency")) n.setEmergency(Boolean.TRUE.equals(map.get("isEmergency")));
        if (map.containsKey("postedBy")) n.setPostedBy((String) map.get("postedBy"));
        if (map.containsKey("postedByName")) n.setPostedByName((String) map.get("postedByName"));
        if (map.containsKey("timestamp") && map.get("timestamp") instanceof Number) {
            n.setTimestamp(((Number) map.get("timestamp")).longValue());
        }
        if (map.containsKey("dateFormatted")) n.setDateFormatted((String) map.get("dateFormatted"));
        return n;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public boolean isEmergency() { return isEmergency; }
    public void setEmergency(boolean emergency) { isEmergency = emergency; }
    public String getPostedBy() { return postedBy; }
    public void setPostedBy(String postedBy) { this.postedBy = postedBy; }
    public String getPostedByName() { return postedByName; }
    public void setPostedByName(String postedByName) { this.postedByName = postedByName; }
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
    public String getDateFormatted() { return dateFormatted; }
    public void setDateFormatted(String dateFormatted) { this.dateFormatted = dateFormatted; }
}
