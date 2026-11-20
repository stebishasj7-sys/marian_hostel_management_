package com.marian.hostel.service;

import com.marian.hostel.model.*;
import com.marian.hostel.repository.DataStore;

import java.util.*;

public class MessTimetableService {
    private final DataStore dataStore = DataStore.getInstance();

    public Map<String, Object> getTimetableAndMenu() {
        Map<String, Object> res = new LinkedHashMap<>();

        List<Map<String, Object>> ttList = new ArrayList<>();
        for (Timetable t : dataStore.getTimetables()) {
            ttList.add(t.toMap());
        }

        List<Map<String, Object>> menuList = new ArrayList<>();
        for (MessMenu m : dataStore.getMessMenus()) {
            menuList.add(m.toMap());
        }

        res.put("success", true);
        res.put("timetables", ttList);
        res.put("messMenus", menuList);
        return res;
    }

    public Map<String, Object> updateMessDay(Map<String, Object> data, String userId) {
        Map<String, Object> res = new LinkedHashMap<>();
        User warden = dataStore.getUserById(userId);
        if (warden == null || !"WARDEN".equalsIgnoreCase(warden.getRole())) {
            res.put("success", false);
            res.put("message", "Permission denied. Only wardens can edit mess menus.");
            return res;
        }

        MessMenu m = MessMenu.fromMap(data);
        dataStore.updateMessMenu(m);
        dataStore.saveToFile();

        res.put("success", true);
        res.put("message", "Mess menu updated for " + m.getDay() + "!");
        res.put("menu", m.toMap());
        return res;
    }
}
