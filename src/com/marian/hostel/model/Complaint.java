package com.marian.hostel.model;

import java.util.*;

public class Complaint {
    private String id;
    private String studentId;
    private String studentName;
    private String studentRoom;
    private String studentDept;
    private String category; // "Electrical", "Plumbing", "Mess/Food", "Wi-Fi/Network", "Cleanliness", "Noise/Discipline", "Other"
    private String title;
    private String description;
    private long timestamp;
    private String dateFormatted;
    private String status; // "PENDING", "RESOLVED"
    private boolean wardenTick; // green checkmark indicator
    private String resolvedBy;
    private String resolvedAt;
    private String wardenRemarks;
    private Set<String> likes = new LinkedHashSet<>();
    private List<Comment> comments = new ArrayList<>();

    public Complaint() {
        this.id = UUID.randomUUID().toString();
        this.timestamp = System.currentTimeMillis();
        this.status = "PENDING";
        this.wardenTick = false;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", id);
        map.put("studentId", studentId != null ? studentId : "");
        map.put("studentName", studentName != null ? studentName : "");
        map.put("studentRoom", studentRoom != null ? studentRoom : "");
        map.put("studentDept", studentDept != null ? studentDept : "");
        map.put("category", category != null ? category : "General");
        map.put("title", title != null ? title : "");
        map.put("description", description != null ? description : "");
        map.put("timestamp", timestamp);
        map.put("dateFormatted", dateFormatted != null ? dateFormatted : "");
        map.put("status", status != null ? status : "PENDING");
        map.put("wardenTick", wardenTick);
        map.put("resolvedBy", resolvedBy != null ? resolvedBy : "");
        map.put("resolvedAt", resolvedAt != null ? resolvedAt : "");
        map.put("wardenRemarks", wardenRemarks != null ? wardenRemarks : "");
        map.put("likesCount", likes.size());
        map.put("likes", new ArrayList<>(likes));

        List<Map<String, Object>> commentList = new ArrayList<>();
        for (Comment c : comments) {
            commentList.add(c.toMap());
        }
        map.put("comments", commentList);
        return map;
    }

    @SuppressWarnings("unchecked")
    public static Complaint fromMap(Map<String, Object> map) {
        Complaint c = new Complaint();
        if (map.containsKey("id")) c.setId((String) map.get("id"));
        if (map.containsKey("studentId")) c.setStudentId((String) map.get("studentId"));
        if (map.containsKey("studentName")) c.setStudentName((String) map.get("studentName"));
        if (map.containsKey("studentRoom")) c.setStudentRoom((String) map.get("studentRoom"));
        if (map.containsKey("studentDept")) c.setStudentDept((String) map.get("studentDept"));
        if (map.containsKey("category")) c.setCategory((String) map.get("category"));
        if (map.containsKey("title")) c.setTitle((String) map.get("title"));
        if (map.containsKey("description")) c.setDescription((String) map.get("description"));
        if (map.containsKey("timestamp") && map.get("timestamp") instanceof Number) {
            c.setTimestamp(((Number) map.get("timestamp")).longValue());
        }
        if (map.containsKey("dateFormatted")) c.setDateFormatted((String) map.get("dateFormatted"));
        if (map.containsKey("status")) c.setStatus((String) map.get("status"));
        if (map.containsKey("wardenTick")) c.setWardenTick(Boolean.TRUE.equals(map.get("wardenTick")));
        if (map.containsKey("resolvedBy")) c.setResolvedBy((String) map.get("resolvedBy"));
        if (map.containsKey("resolvedAt")) c.setResolvedAt((String) map.get("resolvedAt"));
        if (map.containsKey("wardenRemarks")) c.setWardenRemarks((String) map.get("wardenRemarks"));

        if (map.containsKey("likes") && map.get("likes") instanceof List) {
            List<Object> rawLikes = (List<Object>) map.get("likes");
            for (Object o : rawLikes) {
                if (o != null) c.getLikes().add(o.toString());
            }
        }
        if (map.containsKey("comments") && map.get("comments") instanceof List) {
            List<Object> rawComments = (List<Object>) map.get("comments");
            for (Object o : rawComments) {
                if (o instanceof Map) {
                    c.getComments().add(Comment.fromMap((Map<String, Object>) o));
                }
            }
        }
        return c;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }
    public String getStudentRoom() { return studentRoom; }
    public void setStudentRoom(String studentRoom) { this.studentRoom = studentRoom; }
    public String getStudentDept() { return studentDept; }
    public void setStudentDept(String studentDept) { this.studentDept = studentDept; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
    public String getDateFormatted() { return dateFormatted; }
    public void setDateFormatted(String dateFormatted) { this.dateFormatted = dateFormatted; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public boolean isWardenTick() { return wardenTick; }
    public void setWardenTick(boolean wardenTick) { this.wardenTick = wardenTick; }
    public String getResolvedBy() { return resolvedBy; }
    public void setResolvedBy(String resolvedBy) { this.resolvedBy = resolvedBy; }
    public String getResolvedAt() { return resolvedAt; }
    public void setResolvedAt(String resolvedAt) { this.resolvedAt = resolvedAt; }
    public String getWardenRemarks() { return wardenRemarks; }
    public void setWardenRemarks(String wardenRemarks) { this.wardenRemarks = wardenRemarks; }
    public Set<String> getLikes() { return likes; }
    public void setLikes(Set<String> likes) { this.likes = likes; }
    public List<Comment> getComments() { return comments; }
    public void setComments(List<Comment> comments) { this.comments = comments; }
}
