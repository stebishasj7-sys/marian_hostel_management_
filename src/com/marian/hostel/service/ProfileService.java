package com.marian.hostel.service;

import com.marian.hostel.model.*;
import com.marian.hostel.repository.DataStore;

import java.util.*;

public class ProfileService {
    private final DataStore dataStore = DataStore.getInstance();

    public Map<String, Object> getProfile(String userId) {
        Map<String, Object> res = new LinkedHashMap<>();
        User u = dataStore.getUserById(userId);
        if (u == null) {
            res.put("success", false);
            res.put("message", "User not found.");
            return res;
        }
        res.put("success", true);
        res.put("user", u.toMap());
        return res;
    }

    public Map<String, Object> updateProfile(String userId, Map<String, Object> updates) {
        Map<String, Object> res = new LinkedHashMap<>();
        User u = dataStore.getUserById(userId);
        if (u == null) {
            res.put("success", false);
            res.put("message", "User not found.");
            return res;
        }

        if (updates.containsKey("name")) u.setName((String) updates.get("name"));
        if (updates.containsKey("phone")) u.setPhone((String) updates.get("phone"));
        if (updates.containsKey("department")) u.setDepartment((String) updates.get("department"));
        if (updates.containsKey("roomNo")) u.setRoomNo((String) updates.get("roomNo"));
        if (updates.containsKey("bedNo")) u.setBedNo((String) updates.get("bedNo"));
        if (updates.containsKey("gender")) u.setGender((String) updates.get("gender"));
        if (updates.containsKey("dob")) u.setDob((String) updates.get("dob"));
        if (updates.containsKey("address")) u.setAddress((String) updates.get("address"));
        if (updates.containsKey("fatherName")) u.setFatherName((String) updates.get("fatherName"));
        if (updates.containsKey("motherName")) u.setMotherName((String) updates.get("motherName"));
        if (updates.containsKey("guardianPhone")) u.setGuardianPhone((String) updates.get("guardianPhone"));
        if (updates.containsKey("nationality")) u.setNationality((String) updates.get("nationality"));
        if (updates.containsKey("admissionNo")) u.setAdmissionNo((String) updates.get("admissionNo"));
        if (updates.containsKey("aadhaar")) u.setAadhaar((String) updates.get("aadhaar"));
        if (updates.containsKey("photoUrl")) u.setPhotoUrl((String) updates.get("photoUrl"));

        dataStore.saveToFile();

        res.put("success", true);
        res.put("message", "Profile details updated successfully!");
        res.put("user", u.toMap());
        return res;
    }

    public Map<String, Object> updatePhoto(String userId, String photoUrl) {
        Map<String, Object> res = new LinkedHashMap<>();
        User u = dataStore.getUserById(userId);
        if (u == null) {
            res.put("success", false);
            res.put("message", "User not found.");
            return res;
        }

        u.setPhotoUrl(photoUrl);
        dataStore.saveToFile();

        res.put("success", true);
        res.put("message", "Profile photo updated successfully!");
        res.put("photoUrl", photoUrl);
        return res;
    }
}
