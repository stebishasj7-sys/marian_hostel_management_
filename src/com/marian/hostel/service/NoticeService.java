package com.marian.hostel.service;

import com.marian.hostel.model.*;
import com.marian.hostel.repository.DataStore;

import java.text.SimpleDateFormat;
import java.util.*;

public class NoticeService {
    private final DataStore dataStore = DataStore.getInstance();
    private final SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy - hh:mm a");

    public Map<String, Object> getAllNotices() {
        Map<String, Object> res = new LinkedHashMap<>();
        List<Notice> notices = dataStore.getAllNotices();
        List<Map<String, Object>> list = new ArrayList<>();
        Notice activeEmergency = null;

        for (Notice n : notices) {
            list.add(n.toMap());
            if (n.isEmergency() && activeEmergency == null) {
                activeEmergency = n;
            }
        }

        res.put("success", true);
        res.put("notices", list);
        res.put("activeEmergency", activeEmergency != null ? activeEmergency.toMap() : null);
        return res;
    }

    public Map<String, Object> createNotice(Map<String, Object> data, String userId) {
        Map<String, Object> res = new LinkedHashMap<>();
        User warden = dataStore.getUserById(userId);
        if (warden == null || !"WARDEN".equalsIgnoreCase(warden.getRole())) {
            res.put("success", false);
            res.put("message", "Permission denied. Only wardens can post notices.");
            return res;
        }

        String title = (String) data.get("title");
        String content = (String) data.get("content");
        String category = (String) data.getOrDefault("category", "General");
        boolean isEmergency = Boolean.TRUE.equals(data.get("isEmergency"));

        if (title == null || title.trim().isEmpty() || content == null || content.trim().isEmpty()) {
            res.put("success", false);
            res.put("message", "Title and content cannot be blank.");
            return res;
        }

        Notice n = new Notice();
        n.setTitle(title.trim());
        n.setContent(content.trim());
        n.setCategory(category);
        n.setEmergency(isEmergency);
        n.setPostedBy(warden.getId());
        n.setPostedByName(warden.getName());
        n.setDateFormatted(sdf.format(new Date()));

        dataStore.addNotice(n);
        dataStore.saveToFile();

        res.put("success", true);
        res.put("message", isEmergency ? "🚨 Emergency broadcast notice posted with top alert!" : "Notice posted successfully!");
        res.put("notice", n.toMap());
        return res;
    }

    public Map<String, Object> deleteNotice(String noticeId, String userId) {
        Map<String, Object> res = new LinkedHashMap<>();
        User warden = dataStore.getUserById(userId);
        if (warden == null || !"WARDEN".equalsIgnoreCase(warden.getRole())) {
            res.put("success", false);
            res.put("message", "Permission denied. Only wardens can remove notices.");
            return res;
        }

        boolean removed = dataStore.deleteNotice(noticeId);
        if (removed) {
            dataStore.saveToFile();
            res.put("success", true);
            res.put("message", "Notice deleted.");
        } else {
            res.put("success", false);
            res.put("message", "Notice not found.");
        }
        return res;
    }
}
