package com.marian.hostel.model;

import java.util.*;

public class OutpassSetting {
    private String morningStart; // e.g. "08:00"
    private String morningEnd;   // e.g. "09:00"
    private String eveningStart; // e.g. "16:00"
    private String eveningEnd;   // e.g. "17:00"
    private String curfewTime;   // e.g. "20:30"
    private boolean enforceTimeLimits;
    private boolean allowPrebooking;
    private int maxPassesPerDay;

    public OutpassSetting() {
        this.morningStart = "08:00";
        this.morningEnd = "09:00";
        this.eveningStart = "16:00";
        this.eveningEnd = "17:00";
        this.curfewTime = "20:30";
        this.enforceTimeLimits = true;
        this.allowPrebooking = true;
        this.maxPassesPerDay = 50;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("morningStart", morningStart);
        map.put("morningEnd", morningEnd);
        map.put("eveningStart", eveningStart);
        map.put("eveningEnd", eveningEnd);
        map.put("curfewTime", curfewTime);
        map.put("enforceTimeLimits", enforceTimeLimits);
        map.put("allowPrebooking", allowPrebooking);
        map.put("maxPassesPerDay", maxPassesPerDay);
        return map;
    }

    public static OutpassSetting fromMap(Map<String, Object> map) {
        OutpassSetting s = new OutpassSetting();
        if (map.containsKey("morningStart")) s.setMorningStart((String) map.get("morningStart"));
        if (map.containsKey("morningEnd")) s.setMorningEnd((String) map.get("morningEnd"));
        if (map.containsKey("eveningStart")) s.setEveningStart((String) map.get("eveningStart"));
        if (map.containsKey("eveningEnd")) s.setEveningEnd((String) map.get("eveningEnd"));
        if (map.containsKey("curfewTime")) s.setCurfewTime((String) map.get("curfewTime"));
        if (map.containsKey("enforceTimeLimits")) s.setEnforceTimeLimits(Boolean.TRUE.equals(map.get("enforceTimeLimits")));
        if (map.containsKey("allowPrebooking")) s.setAllowPrebooking(Boolean.TRUE.equals(map.get("allowPrebooking")));
        if (map.containsKey("maxPassesPerDay") && map.get("maxPassesPerDay") instanceof Number) {
            s.setMaxPassesPerDay(((Number) map.get("maxPassesPerDay")).intValue());
        }
        return s;
    }

    public String getMorningStart() { return morningStart; }
    public void setMorningStart(String morningStart) { this.morningStart = morningStart; }
    public String getMorningEnd() { return morningEnd; }
    public void setMorningEnd(String morningEnd) { this.morningEnd = morningEnd; }
    public String getEveningStart() { return eveningStart; }
    public void setEveningStart(String eveningStart) { this.eveningStart = eveningStart; }
    public String getEveningEnd() { return eveningEnd; }
    public void setEveningEnd(String eveningEnd) { this.eveningEnd = eveningEnd; }
    public String getCurfewTime() { return curfewTime; }
    public void setCurfewTime(String curfewTime) { this.curfewTime = curfewTime; }
    public boolean isEnforceTimeLimits() { return enforceTimeLimits; }
    public void setEnforceTimeLimits(boolean enforceTimeLimits) { this.enforceTimeLimits = enforceTimeLimits; }
    public boolean isAllowPrebooking() { return allowPrebooking; }
    public void setAllowPrebooking(boolean allowPrebooking) { this.allowPrebooking = allowPrebooking; }
    public int getMaxPassesPerDay() { return maxPassesPerDay; }
    public void setMaxPassesPerDay(int maxPassesPerDay) { this.maxPassesPerDay = maxPassesPerDay; }
}
