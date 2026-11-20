package com.marian.hostel.model;

import java.util.*;

public class EmergencyContact {
    private String id;
    private String title;      // e.g. "Hostel Chief Warden", "Campus Ambulance", "Hostel Security Gate"
    private String name;       // Contact person or service
    private String phone;      // Direct number
    private String altPhone;   // Alternative number
    private String location;   // Room or campus block
    private String availability; // "24x7 Emergency", "08:00 AM - 10:00 PM"
    private int priority;      // Order

    public EmergencyContact() {
        this.id = UUID.randomUUID().toString();
    }

    public EmergencyContact(String title, String name, String phone, String altPhone, String location, String availability, int priority) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.name = name;
        this.phone = phone;
        this.altPhone = altPhone;
        this.location = location;
        this.availability = availability;
        this.priority = priority;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", id);
        map.put("title", title != null ? title : "");
        map.put("name", name != null ? name : "");
        map.put("phone", phone != null ? phone : "");
        map.put("altPhone", altPhone != null ? altPhone : "");
        map.put("location", location != null ? location : "");
        map.put("availability", availability != null ? availability : "24x7");
        map.put("priority", priority);
        return map;
    }

    public static EmergencyContact fromMap(Map<String, Object> map) {
        EmergencyContact c = new EmergencyContact();
        if (map.containsKey("id")) c.setId((String) map.get("id"));
        if (map.containsKey("title")) c.setTitle((String) map.get("title"));
        if (map.containsKey("name")) c.setName((String) map.get("name"));
        if (map.containsKey("phone")) c.setPhone((String) map.get("phone"));
        if (map.containsKey("altPhone")) c.setAltPhone((String) map.get("altPhone"));
        if (map.containsKey("location")) c.setLocation((String) map.get("location"));
        if (map.containsKey("availability")) c.setAvailability((String) map.get("availability"));
        if (map.containsKey("priority") && map.get("priority") instanceof Number) {
            c.setPriority(((Number) map.get("priority")).intValue());
        }
        return c;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getAltPhone() { return altPhone; }
    public void setAltPhone(String altPhone) { this.altPhone = altPhone; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public String getAvailability() { return availability; }
    public void setAvailability(String availability) { this.availability = availability; }
    public int getPriority() { return priority; }
    public void setPriority(int priority) { this.priority = priority; }
}
