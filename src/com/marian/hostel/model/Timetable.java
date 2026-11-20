package com.marian.hostel.model;

import java.util.*;

public class Timetable {
    private String id;
    private String timeSlot; // e.g. "06:00 AM - 07:00 AM"
    private String title;    // e.g. "Morning Fitness / Prayer"
    private String description;
    private String days;     // e.g. "All Days" or "Mon - Fri"

    public Timetable() {
        this.id = UUID.randomUUID().toString();
    }

    public Timetable(String timeSlot, String title, String description, String days) {
        this.id = UUID.randomUUID().toString();
        this.timeSlot = timeSlot;
        this.title = title;
        this.description = description;
        this.days = days;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", id);
        map.put("timeSlot", timeSlot != null ? timeSlot : "");
        map.put("title", title != null ? title : "");
        map.put("description", description != null ? description : "");
        map.put("days", days != null ? days : "All Days");
        return map;
    }

    public static Timetable fromMap(Map<String, Object> map) {
        Timetable t = new Timetable();
        if (map.containsKey("id")) t.setId((String) map.get("id"));
        if (map.containsKey("timeSlot")) t.setTimeSlot((String) map.get("timeSlot"));
        if (map.containsKey("title")) t.setTitle((String) map.get("title"));
        if (map.containsKey("description")) t.setDescription((String) map.get("description"));
        if (map.containsKey("days")) t.setDays((String) map.get("days"));
        return t;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTimeSlot() { return timeSlot; }
    public void setTimeSlot(String timeSlot) { this.timeSlot = timeSlot; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getDays() { return days; }
    public void setDays(String days) { this.days = days; }
}
