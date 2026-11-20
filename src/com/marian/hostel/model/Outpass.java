package com.marian.hostel.model;

import java.util.*;

public class Outpass {
    private String id;
    private String tokenNo; // e.g. "MH-PASS-2026-4819"
    private String studentId;
    private String studentName;
    private String roomNo;
    private String department;
    private String phone;
    private String parentPhone;
    private String purpose; // "Medical Appointment", "Home Visit", "Market / Shopping", "Academic / Library", "Emergency", "Other"
    private String destination;
    private String departureDate;
    private String departureTime;
    private String returnDate;
    private String returnTime;
    private boolean isPrebooked;
    private String status; // "PENDING", "APPROVED", "REJECTED", "USED", "EXPIRED"
    private boolean parentConsentVerified;
    private String wardenRemarks;
    private String verifiedByWardenId;
    private String verifiedByWardenName;
    private String digitalVerificationHash; // Digital verification signature/seal
    private long appliedAt;
    private long verifiedAt;

    public Outpass() {
        this.id = UUID.randomUUID().toString();
        this.tokenNo = "MH-PASS-" + (1000 + new Random().nextInt(9000));
        this.status = "PENDING";
        this.appliedAt = System.currentTimeMillis();
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", id);
        map.put("tokenNo", tokenNo != null ? tokenNo : "");
        map.put("studentId", studentId != null ? studentId : "");
        map.put("studentName", studentName != null ? studentName : "");
        map.put("roomNo", roomNo != null ? roomNo : "");
        map.put("department", department != null ? department : "");
        map.put("phone", phone != null ? phone : "");
        map.put("parentPhone", parentPhone != null ? parentPhone : "");
        map.put("purpose", purpose != null ? purpose : "");
        map.put("destination", destination != null ? destination : "");
        map.put("departureDate", departureDate != null ? departureDate : "");
        map.put("departureTime", departureTime != null ? departureTime : "");
        map.put("returnDate", returnDate != null ? returnDate : "");
        map.put("returnTime", returnTime != null ? returnTime : "");
        map.put("isPrebooked", isPrebooked);
        map.put("status", status != null ? status : "PENDING");
        map.put("parentConsentVerified", parentConsentVerified);
        map.put("wardenRemarks", wardenRemarks != null ? wardenRemarks : "");
        map.put("verifiedByWardenId", verifiedByWardenId != null ? verifiedByWardenId : "");
        map.put("verifiedByWardenName", verifiedByWardenName != null ? verifiedByWardenName : "");
        map.put("digitalVerificationHash", digitalVerificationHash != null ? digitalVerificationHash : "");
        map.put("appliedAt", appliedAt);
        map.put("verifiedAt", verifiedAt);
        return map;
    }

    public static Outpass fromMap(Map<String, Object> map) {
        Outpass o = new Outpass();
        if (map.containsKey("id")) o.setId((String) map.get("id"));
        if (map.containsKey("tokenNo")) o.setTokenNo((String) map.get("tokenNo"));
        if (map.containsKey("studentId")) o.setStudentId((String) map.get("studentId"));
        if (map.containsKey("studentName")) o.setStudentName((String) map.get("studentName"));
        if (map.containsKey("roomNo")) o.setRoomNo((String) map.get("roomNo"));
        if (map.containsKey("department")) o.setDepartment((String) map.get("department"));
        if (map.containsKey("phone")) o.setPhone((String) map.get("phone"));
        if (map.containsKey("parentPhone")) o.setParentPhone((String) map.get("parentPhone"));
        if (map.containsKey("purpose")) o.setPurpose((String) map.get("purpose"));
        if (map.containsKey("destination")) o.setDestination((String) map.get("destination"));
        if (map.containsKey("departureDate")) o.setDepartureDate((String) map.get("departureDate"));
        if (map.containsKey("departureTime")) o.setDepartureTime((String) map.get("departureTime"));
        if (map.containsKey("returnDate")) o.setReturnDate((String) map.get("returnDate"));
        if (map.containsKey("returnTime")) o.setReturnTime((String) map.get("returnTime"));
        if (map.containsKey("isPrebooked")) o.setPrebooked(Boolean.TRUE.equals(map.get("isPrebooked")));
        if (map.containsKey("status")) o.setStatus((String) map.get("status"));
        if (map.containsKey("parentConsentVerified")) o.setParentConsentVerified(Boolean.TRUE.equals(map.get("parentConsentVerified")));
        if (map.containsKey("wardenRemarks")) o.setWardenRemarks((String) map.get("wardenRemarks"));
        if (map.containsKey("verifiedByWardenId")) o.setVerifiedByWardenId((String) map.get("verifiedByWardenId"));
        if (map.containsKey("verifiedByWardenName")) o.setVerifiedByWardenName((String) map.get("verifiedByWardenName"));
        if (map.containsKey("digitalVerificationHash")) o.setDigitalVerificationHash((String) map.get("digitalVerificationHash"));
        if (map.containsKey("appliedAt") && map.get("appliedAt") instanceof Number) {
            o.setAppliedAt(((Number) map.get("appliedAt")).longValue());
        }
        if (map.containsKey("verifiedAt") && map.get("verifiedAt") instanceof Number) {
            o.setVerifiedAt(((Number) map.get("verifiedAt")).longValue());
        }
        return o;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTokenNo() { return tokenNo; }
    public void setTokenNo(String tokenNo) { this.tokenNo = tokenNo; }
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }
    public String getRoomNo() { return roomNo; }
    public void setRoomNo(String roomNo) { this.roomNo = roomNo; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getParentPhone() { return parentPhone; }
    public void setParentPhone(String parentPhone) { this.parentPhone = parentPhone; }
    public String getPurpose() { return purpose; }
    public void setPurpose(String purpose) { this.purpose = purpose; }
    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }
    public String getDepartureDate() { return departureDate; }
    public void setDepartureDate(String departureDate) { this.departureDate = departureDate; }
    public String getDepartureTime() { return departureTime; }
    public void setDepartureTime(String departureTime) { this.departureTime = departureTime; }
    public String getReturnDate() { return returnDate; }
    public void setReturnDate(String returnDate) { this.returnDate = returnDate; }
    public String getReturnTime() { return returnTime; }
    public void setReturnTime(String returnTime) { this.returnTime = returnTime; }
    public boolean isPrebooked() { return isPrebooked; }
    public void setPrebooked(boolean prebooked) { isPrebooked = prebooked; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public boolean isParentConsentVerified() { return parentConsentVerified; }
    public void setParentConsentVerified(boolean parentConsentVerified) { this.parentConsentVerified = parentConsentVerified; }
    public String getWardenRemarks() { return wardenRemarks; }
    public void setWardenRemarks(String wardenRemarks) { this.wardenRemarks = wardenRemarks; }
    public String getVerifiedByWardenId() { return verifiedByWardenId; }
    public void setVerifiedByWardenId(String verifiedByWardenId) { this.verifiedByWardenId = verifiedByWardenId; }
    public String getVerifiedByWardenName() { return verifiedByWardenName; }
    public void setVerifiedByWardenName(String verifiedByWardenName) { this.verifiedByWardenName = verifiedByWardenName; }
    public String getDigitalVerificationHash() { return digitalVerificationHash; }
    public void setDigitalVerificationHash(String digitalVerificationHash) { this.digitalVerificationHash = digitalVerificationHash; }
    public long getAppliedAt() { return appliedAt; }
    public void setAppliedAt(long appliedAt) { this.appliedAt = appliedAt; }
    public long getVerifiedAt() { return verifiedAt; }
    public void setVerifiedAt(long verifiedAt) { this.verifiedAt = verifiedAt; }
}
