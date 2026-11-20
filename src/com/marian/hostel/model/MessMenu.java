package com.marian.hostel.model;

import java.util.*;

public class MessMenu {
    private String day; // "Monday", "Tuesday", etc.
    private String breakfast;
    private String lunch;
    private String snacks;
    private String dinner;
    private String special;

    public MessMenu() {}

    public MessMenu(String day, String breakfast, String lunch, String snacks, String dinner, String special) {
        this.day = day;
        this.breakfast = breakfast;
        this.lunch = lunch;
        this.snacks = snacks;
        this.dinner = dinner;
        this.special = special;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("day", day != null ? day : "");
        map.put("breakfast", breakfast != null ? breakfast : "");
        map.put("lunch", lunch != null ? lunch : "");
        map.put("snacks", snacks != null ? snacks : "");
        map.put("dinner", dinner != null ? dinner : "");
        map.put("special", special != null ? special : "");
        return map;
    }

    public static MessMenu fromMap(Map<String, Object> map) {
        MessMenu m = new MessMenu();
        if (map.containsKey("day")) m.setDay((String) map.get("day"));
        if (map.containsKey("breakfast")) m.setBreakfast((String) map.get("breakfast"));
        if (map.containsKey("lunch")) m.setLunch((String) map.get("lunch"));
        if (map.containsKey("snacks")) m.setSnacks((String) map.get("snacks"));
        if (map.containsKey("dinner")) m.setDinner((String) map.get("dinner"));
        if (map.containsKey("special")) m.setSpecial((String) map.get("special"));
        return m;
    }

    public String getDay() { return day; }
    public void setDay(String day) { this.day = day; }
    public String getBreakfast() { return breakfast; }
    public void setBreakfast(String breakfast) { this.breakfast = breakfast; }
    public String getLunch() { return lunch; }
    public void setLunch(String lunch) { this.lunch = lunch; }
    public String getSnacks() { return snacks; }
    public void setSnacks(String snacks) { this.snacks = snacks; }
    public String getDinner() { return dinner; }
    public void setDinner(String dinner) { this.dinner = dinner; }
    public String getSpecial() { return special; }
    public void setSpecial(String special) { this.special = special; }
}
