package com.marian.hostel.model;

import java.util.*;

public class Bed {
    private String bedId;
    private String bedLabel; // e.g. "Lower Berth (L-1)", "Upper Berth (U-2)"
    private String studentId;
    private String studentName;
    private String studentDept;
    private String status; // "VACANT", "OCCUPIED", "MAINTENANCE"

    public Bed() {}

    public Bed(String bedId, String bedLabel, String status) {
        this.bedId = bedId;
        this.bedLabel = bedLabel;
        this.status = status;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("bedId", bedId);
        map.put("bedLabel", bedLabel != null ? bedLabel : "");
        map.put("studentId", studentId != null ? studentId : "");
        map.put("studentName", studentName != null ? studentName : "");
        map.put("studentDept", studentDept != null ? studentDept : "");
        map.put("status", status != null ? status : "VACANT");
        return map;
    }

    public static Bed fromMap(Map<String, Object> map) {
        Bed b = new Bed();
        if (map.containsKey("bedId")) b.setBedId((String) map.get("bedId"));
        if (map.containsKey("bedLabel")) b.setBedLabel((String) map.get("bedLabel"));
        if (map.containsKey("studentId")) b.setStudentId((String) map.get("studentId"));
        if (map.containsKey("studentName")) b.setStudentName((String) map.get("studentName"));
        if (map.containsKey("studentDept")) b.setStudentDept((String) map.get("studentDept"));
        if (map.containsKey("status")) b.setStatus((String) map.get("status"));
        return b;
    }

    public String getBedId() { return bedId; }
    public void setBedId(String bedId) { this.bedId = bedId; }
    public String getBedLabel() { return bedLabel; }
    public void setBedLabel(String bedLabel) { this.bedLabel = bedLabel; }
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }
    public String getStudentDept() { return studentDept; }
    public void setStudentDept(String studentDept) { this.studentDept = studentDept; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
