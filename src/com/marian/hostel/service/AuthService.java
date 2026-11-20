package com.marian.hostel.service;

import com.marian.hostel.model.*;
import com.marian.hostel.repository.DataStore;

import java.util.*;

public class AuthService {
    private final DataStore dataStore = DataStore.getInstance();

    public Map<String, Object> getWardenStatus() {
        Map<String, Object> res = new LinkedHashMap<>();
        int active = dataStore.getActiveWardenCount();
        int total = dataStore.getTotalWardenCount();
        boolean available = dataStore.isWardenLoginAvailable();

        res.put("activeWardenCount", active);
        res.put("totalWardenCount", total);
        res.put("maxWardens", 2);
        res.put("isWardenLoginAvailable", available);
        return res;
    }

    public Map<String, Object> register(Map<String, Object> data) {
        Map<String, Object> res = new LinkedHashMap<>();
        String email = (String) data.get("email");
        String password = (String) data.get("password");
        String name = (String) data.get("name");
        String role = (String) data.getOrDefault("role", "STUDENT");

        if (email == null || email.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            res.put("success", false);
            res.put("message", "Email and Password are required.");
            return res;
        }

        if (dataStore.getUserByEmail(email) != null) {
            res.put("success", false);
            res.put("message", "An account with this email address already exists.");
            return res;
        }

        // If registering as warden, ensure max 2 wardens rule
        if ("WARDEN".equalsIgnoreCase(role)) {
            if (dataStore.getTotalWardenCount() >= 2) {
                res.put("success", false);
                res.put("message", "Registration as Warden is closed. Maximum 2 warden accounts already exist for Marian Hostel.");
                return res;
            }
        }

        User u = User.fromMap(data);
        u.setId(UUID.randomUUID().toString());
        u.setRole(role.toUpperCase());
        u.setActive(true);
        u.setLoggedIn(false);

        // Assign room bed if provided
        String roomNo = u.getRoomNo();
        if (roomNo != null && !roomNo.isEmpty() && "STUDENT".equalsIgnoreCase(u.getRole())) {
            Room r = dataStore.getRoom(roomNo);
            if (r != null) {
                for (Bed b : r.getBeds()) {
                    if ("VACANT".equalsIgnoreCase(b.getStatus())) {
                        b.setStatus("OCCUPIED");
                        b.setStudentId(u.getId());
                        b.setStudentName(u.getName());
                        b.setStudentDept(u.getDepartment());
                        u.setBedNo(b.getBedLabel());
                        break;
                    }
                }
            }
        }

        dataStore.addUser(u);
        dataStore.saveToFile();

        res.put("success", true);
        res.put("message", "Registration successful! Welcome to Marian Hostel.");
        res.put("user", u.toMap());
        res.put("token", u.getId());
        return res;
    }

    public Map<String, Object> login(String email, String password, String loginMode) {
        Map<String, Object> res = new LinkedHashMap<>();
        User u = dataStore.getUserByEmail(email);

        if (u == null || !u.getPassword().equals(password)) {
            res.put("success", false);
            res.put("message", "Invalid email or password.");
            return res;
        }

        // Check if user is attempting warden login
        if ("WARDEN".equalsIgnoreCase(u.getRole())) {
            // Check 2-warden limit
            if (!u.isLoggedIn() && dataStore.getActiveWardenCount() >= 2) {
                res.put("success", false);
                res.put("message", "Warden access limit reached! Maximum 2 wardens are currently logged in.");
                res.put("wardenLimitReached", true);
                return res;
            }
            u.setLoggedIn(true);
        } else {
            // Student login
            if ("WARDEN".equalsIgnoreCase(loginMode)) {
                res.put("success", false);
                res.put("message", "Access denied. This account does not possess Warden administrative credentials.");
                return res;
            }
            u.setLoggedIn(true);
        }

        dataStore.saveToFile();

        res.put("success", true);
        res.put("message", "Login successful! Welcome back, " + u.getName());
        res.put("user", u.toMap());
        res.put("token", u.getId());
        res.put("wardenStatus", getWardenStatus());
        return res;
    }

    public Map<String, Object> logout(String userId) {
        Map<String, Object> res = new LinkedHashMap<>();
        if (userId != null) {
            User u = dataStore.getUserById(userId);
            if (u != null) {
                u.setLoggedIn(false);
                dataStore.saveToFile();
            }
        }
        res.put("success", true);
        res.put("message", "Logged out successfully.");
        res.put("wardenStatus", getWardenStatus());
        return res;
    }

    public Map<String, Object> resetWardenLogins() {
        dataStore.resetAllWardenLogins();
        Map<String, Object> res = new LinkedHashMap<>();
        res.put("success", true);
        res.put("message", "All active warden sessions have been reset. Warden login is now open.");
        res.put("wardenStatus", getWardenStatus());
        return res;
    }
}
