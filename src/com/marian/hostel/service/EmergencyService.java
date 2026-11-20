package com.marian.hostel.service;

import com.marian.hostel.model.*;
import com.marian.hostel.repository.DataStore;

import java.util.*;

public class EmergencyService {
    private final DataStore dataStore = DataStore.getInstance();

    public Map<String, Object> getEmergencyContacts() {
        Map<String, Object> res = new LinkedHashMap<>();
        List<EmergencyContact> list = dataStore.getEmergencyContacts();
        list.sort(Comparator.comparingInt(EmergencyContact::getPriority));

        List<Map<String, Object>> mapList = new ArrayList<>();
        for (EmergencyContact c : list) {
            mapList.add(c.toMap());
        }

        res.put("success", true);
        res.put("contacts", mapList);
        return res;
    }

    public Map<String, Object> saveContact(Map<String, Object> data, String wardenId) {
        Map<String, Object> res = new LinkedHashMap<>();
        User warden = dataStore.getUserById(wardenId);
        if (warden == null || !"WARDEN".equalsIgnoreCase(warden.getRole())) {
            res.put("success", false);
            res.put("message", "Permission denied. Only wardens can edit emergency directory.");
            return res;
        }

        EmergencyContact c = EmergencyContact.fromMap(data);
        if (c.getId() == null || c.getId().isEmpty()) {
            c.setId(UUID.randomUUID().toString());
        }

        dataStore.updateEmergencyContact(c);
        dataStore.saveToFile();

        res.put("success", true);
        res.put("message", "Emergency contact saved successfully!");
        res.put("contact", c.toMap());
        return res;
    }

    public Map<String, Object> deleteContact(String contactId, String wardenId) {
        Map<String, Object> res = new LinkedHashMap<>();
        User warden = dataStore.getUserById(wardenId);
        if (warden == null || !"WARDEN".equalsIgnoreCase(warden.getRole())) {
            res.put("success", false);
            res.put("message", "Permission denied. Only wardens can remove emergency contacts.");
            return res;
        }

        boolean removed = dataStore.deleteEmergencyContact(contactId);
        if (removed) {
            dataStore.saveToFile();
            res.put("success", true);
            res.put("message", "Emergency contact removed.");
        } else {
            res.put("success", false);
            res.put("message", "Contact not found.");
        }
        return res;
    }
}
