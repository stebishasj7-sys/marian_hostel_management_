package com.marian.hostel.model;

import java.util.*;

public class User {
    private String id;
    private String name;
    private String email;
    private String password;
    private String role; // "STUDENT" or "WARDEN"
    private String department;
    private String roomNo;
    private String bedNo;
    private String phone;
    private String gender;
    private String dob;
    private String address;
    private String fatherName;
    private String motherName;
    private String guardianPhone;
    private String nationality;
    private String admissionNo;
    private String aadhaar;
    private String photoUrl; // base64 or avatar URL
    private boolean active;
    private boolean loggedIn;
    private long createdAt;

    public User() {
        this.id = UUID.randomUUID().toString();
        this.active = true;
        this.loggedIn = false;
        this.createdAt = System.currentTimeMillis();
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", id);
        map.put("name", name != null ? name : "");
        map.put("email", email != null ? email : "");
        map.put("role", role != null ? role : "STUDENT");
        map.put("department", department != null ? department : "");
        map.put("roomNo", roomNo != null ? roomNo : "");
        map.put("bedNo", bedNo != null ? bedNo : "");
        map.put("phone", phone != null ? phone : "");
        map.put("gender", gender != null ? gender : "");
        map.put("dob", dob != null ? dob : "");
        map.put("address", address != null ? address : "");
        map.put("fatherName", fatherName != null ? fatherName : "");
        map.put("motherName", motherName != null ? motherName : "");
        map.put("guardianPhone", guardianPhone != null ? guardianPhone : "");
        map.put("nationality", nationality != null ? nationality : "Indian");
        map.put("admissionNo", admissionNo != null ? admissionNo : "");
        map.put("aadhaar", aadhaar != null ? aadhaar : "");
        map.put("photoUrl", photoUrl != null ? photoUrl : "");
        map.put("active", active);
        map.put("loggedIn", loggedIn);
        map.put("createdAt", createdAt);
        return map;
    }

    public static User fromMap(Map<String, Object> map) {
        User u = new User();
        if (map.containsKey("id")) u.setId((String) map.get("id"));
        if (map.containsKey("name")) u.setName((String) map.get("name"));
        if (map.containsKey("email")) u.setEmail((String) map.get("email"));
        if (map.containsKey("password")) u.setPassword((String) map.get("password"));
        if (map.containsKey("role")) u.setRole((String) map.get("role"));
        if (map.containsKey("department")) u.setDepartment((String) map.get("department"));
        if (map.containsKey("roomNo")) u.setRoomNo((String) map.get("roomNo"));
        if (map.containsKey("bedNo")) u.setBedNo((String) map.get("bedNo"));
        if (map.containsKey("phone")) u.setPhone((String) map.get("phone"));
        if (map.containsKey("gender")) u.setGender((String) map.get("gender"));
        if (map.containsKey("dob")) u.setDob((String) map.get("dob"));
        if (map.containsKey("address")) u.setAddress((String) map.get("address"));
        if (map.containsKey("fatherName")) u.setFatherName((String) map.get("fatherName"));
        if (map.containsKey("motherName")) u.setMotherName((String) map.get("motherName"));
        if (map.containsKey("guardianPhone")) u.setGuardianPhone((String) map.get("guardianPhone"));
        if (map.containsKey("nationality")) u.setNationality((String) map.get("nationality"));
        if (map.containsKey("admissionNo")) u.setAdmissionNo((String) map.get("admissionNo"));
        if (map.containsKey("aadhaar")) u.setAadhaar((String) map.get("aadhaar"));
        if (map.containsKey("photoUrl")) u.setPhotoUrl((String) map.get("photoUrl"));
        if (map.containsKey("active")) u.setActive(Boolean.TRUE.equals(map.get("active")));
        if (map.containsKey("loggedIn")) u.setLoggedIn(Boolean.TRUE.equals(map.get("loggedIn")));
        if (map.containsKey("createdAt") && map.get("createdAt") instanceof Number) {
            u.setCreatedAt(((Number) map.get("createdAt")).longValue());
        }
        return u;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    public String getRoomNo() { return roomNo; }
    public void setRoomNo(String roomNo) { this.roomNo = roomNo; }
    public String getBedNo() { return bedNo; }
    public void setBedNo(String bedNo) { this.bedNo = bedNo; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public String getDob() { return dob; }
    public void setDob(String dob) { this.dob = dob; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getFatherName() { return fatherName; }
    public void setFatherName(String fatherName) { this.fatherName = fatherName; }
    public String getMotherName() { return motherName; }
    public void setMotherName(String motherName) { this.motherName = motherName; }
    public String getGuardianPhone() { return guardianPhone; }
    public void setGuardianPhone(String guardianPhone) { this.guardianPhone = guardianPhone; }
    public String getNationality() { return nationality; }
    public void setNationality(String nationality) { this.nationality = nationality; }
    public String getAdmissionNo() { return admissionNo; }
    public void setAdmissionNo(String admissionNo) { this.admissionNo = admissionNo; }
    public String getAadhaar() { return aadhaar; }
    public void setAadhaar(String aadhaar) { this.aadhaar = aadhaar; }
    public String getPhotoUrl() { return photoUrl; }
    public void setPhotoUrl(String photoUrl) { this.photoUrl = photoUrl; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    public boolean isLoggedIn() { return loggedIn; }
    public void setLoggedIn(boolean loggedIn) { this.loggedIn = loggedIn; }
    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }
}
