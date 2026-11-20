package com.marian.hostel.service;

import com.marian.hostel.model.*;
import com.marian.hostel.repository.DataStore;

import java.text.SimpleDateFormat;
import java.util.*;

public class ComplaintService {
    private final DataStore dataStore = DataStore.getInstance();
    private final SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy - hh:mm a");

    public Map<String, Object> getAllComplaints() {
        Map<String, Object> res = new LinkedHashMap<>();
        List<Complaint> list = dataStore.getAllComplaints();
        List<Map<String, Object>> mapList = new ArrayList<>();
        int pendingCount = 0;
        int resolvedCount = 0;

        for (Complaint c : list) {
            mapList.add(c.toMap());
            if ("RESOLVED".equalsIgnoreCase(c.getStatus())) resolvedCount++;
            else pendingCount++;
        }

        res.put("success", true);
        res.put("complaints", mapList);
        res.put("totalComplaints", list.size());
        res.put("pendingCount", pendingCount);
        res.put("resolvedCount", resolvedCount);
        return res;
    }

    public Map<String, Object> createComplaint(Map<String, Object> data, String userId) {
        Map<String, Object> res = new LinkedHashMap<>();
        User u = dataStore.getUserById(userId);
        if (u == null) {
            res.put("success", false);
            res.put("message", "User not found.");
            return res;
        }

        String title = (String) data.get("title");
        String description = (String) data.get("description");
        String category = (String) data.getOrDefault("category", "General");
        String room = (String) data.getOrDefault("roomNo", u.getRoomNo());

        if (title == null || title.trim().isEmpty() || description == null || description.trim().isEmpty()) {
            res.put("success", false);
            res.put("message", "Title and description are required.");
            return res;
        }

        Complaint c = new Complaint();
        c.setStudentId(u.getId());
        c.setStudentName(u.getName());
        c.setStudentRoom(room != null ? room : "N/A");
        c.setStudentDept(u.getDepartment());
        c.setCategory(category);
        c.setTitle(title.trim());
        c.setDescription(description.trim());
        c.setDateFormatted(sdf.format(new Date()));
        c.setStatus("PENDING");
        c.setWardenTick(false);

        dataStore.addComplaint(c);
        dataStore.saveToFile();

        res.put("success", true);
        res.put("message", "Complaint registered successfully! Hostellers can now comment or like, and the warden will review it.");
        res.put("complaint", c.toMap());
        return res;
    }

    public Map<String, Object> toggleLike(String complaintId, String userId) {
        Map<String, Object> res = new LinkedHashMap<>();
        Complaint c = dataStore.getComplaintById(complaintId);
        if (c == null) {
            res.put("success", false);
            res.put("message", "Complaint not found.");
            return res;
        }

        boolean nowLiked;
        if (c.getLikes().contains(userId)) {
            c.getLikes().remove(userId);
            nowLiked = false;
        } else {
            c.getLikes().add(userId);
            nowLiked = true;
        }

        dataStore.saveToFile();

        res.put("success", true);
        res.put("liked", nowLiked);
        res.put("likesCount", c.getLikes().size());
        res.put("complaint", c.toMap());
        return res;
    }

    public Map<String, Object> addComment(String complaintId, String text, String userId) {
        Map<String, Object> res = new LinkedHashMap<>();
        Complaint c = dataStore.getComplaintById(complaintId);
        if (c == null) {
            res.put("success", false);
            res.put("message", "Complaint not found.");
            return res;
        }

        User u = dataStore.getUserById(userId);
        if (u == null) {
            res.put("success", false);
            res.put("message", "User not found.");
            return res;
        }

        if (text == null || text.trim().isEmpty()) {
            res.put("success", false);
            res.put("message", "Comment text cannot be empty.");
            return res;
        }

        Comment comment = new Comment(u.getId(), u.getName(), u.getRole(), text.trim(), sdf.format(new Date()));
        c.getComments().add(comment);
        dataStore.saveToFile();

        res.put("success", true);
        res.put("message", "Comment posted.");
        res.put("complaint", c.toMap());
        return res;
    }

    public Map<String, Object> resolveComplaint(String complaintId, String remarks, String wardenId) {
        Map<String, Object> res = new LinkedHashMap<>();
        User warden = dataStore.getUserById(wardenId);
        if (warden == null || !"WARDEN".equalsIgnoreCase(warden.getRole())) {
            res.put("success", false);
            res.put("message", "Permission denied. Only a warden can resolve complaints.");
            return res;
        }

        Complaint c = dataStore.getComplaintById(complaintId);
        if (c == null) {
            res.put("success", false);
            res.put("message", "Complaint not found.");
            return res;
        }

        c.setStatus("RESOLVED");
        c.setWardenTick(true); // GREEN CHECKMARK TICK!
        c.setResolvedBy(warden.getName());
        c.setResolvedAt(sdf.format(new Date()));
        c.setWardenRemarks(remarks != null && !remarks.trim().isEmpty() ? remarks.trim() : "Verified & Resolved by Warden");

        dataStore.saveToFile();

        res.put("success", true);
        res.put("message", "✅ Complaint cleared and verified with green tick mark!");
        res.put("complaint", c.toMap());
        return res;
    }
}
