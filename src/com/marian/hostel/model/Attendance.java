package com.marian.hostel.model;

import java.util.*;

public class Attendance {
    private String id;
    private String date; // YYYY-MM-DD
    private String session; // "NIGHT_CURFEW", "MORNING"
    private String studentId;
    private String studentName;
    private String roomNo;
    private String status; // "PRESENT", "ABSENT", "ON_LEAVE"
    private String markedBy;
    private long timestamp;

    public Attendance() {
        this.id = UUID.randomUUID().toString();
        this.timestamp = System.currentTimeMillis();
    }

    public Attendance(String date, String session, String studentId, String studentName, String roomNo, String status, String markedBy) {
        this.id = UUID.randomUUID().toString();
        this.date = date;
        this.session = session;
        this.studentId = studentId;
        this.studentName = studentName;
        this.roomNo = roomNo;
        this.status = status;
        this.markedBy = markedBy;
        this.timestamp = System.currentTimeMillis();
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", id);
        map.put("date", date != null ? date : "");
        map.put("session", session != null ? session : "NIGHT_CURFEW");
        map.put("studentId", studentId != null ? studentId : "");
        map.put("studentName", studentName != null ? studentName : "");
        map.put("roomNo", roomNo != null ? roomNo : "");
        map.put("status", status != null ? status : "PRESENT");
        map.put("markedBy", markedBy != null ? markedBy : "");
        map.put("timestamp", timestamp);
        return map;
    }

    public static Attendance fromMap(Map<String, Object> map) {
        Attendance a = new Attendance();
        if (map.containsKey("id")) a.setId((String) map.get("id"));
        if (map.containsKey("date")) a.setDate((String) map.get("date"));
        if (map.containsKey("session")) a.setSession((String) map.get("session"));
        if (map.containsKey("studentId")) a.setStudentId((String) map.get("studentId"));
        if (map.containsKey("studentName")) a.setStudentName((String) map.get("studentName"));
        if (map.containsKey("roomNo")) a.setRoomNo((String) map.get("roomNo"));
        if (map.containsKey("status")) a.setStatus((String) map.get("status"));
        if (map.containsKey("markedBy")) a.setMarkedBy((String) map.get("markedBy"));
        if (map.containsKey("timestamp") && map.get("timestamp") instanceof Number) {
            a.setTimestamp(((Number) map.get("timestamp")).longValue());
        }
        return a;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public String getSession() { return session; }
    public void setSession(String session) { this.session = session; }
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }
    public String getRoomNo() { return roomNo; }
    public void setRoomNo(String roomNo) { this.roomNo = roomNo; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getMarkedBy() { return markedBy; }
    public void setMarkedBy(String markedBy) { this.markedBy = markedBy; }
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}
