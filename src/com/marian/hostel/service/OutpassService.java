package com.marian.hostel.service;

import com.marian.hostel.model.*;
import com.marian.hostel.repository.DataStore;

import java.text.SimpleDateFormat;
import java.util.*;

public class OutpassService {
    private final DataStore dataStore = DataStore.getInstance();
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public Map<String, Object> getOutpassData(String userId) {
        Map<String, Object> res = new LinkedHashMap<>();
        User u = dataStore.getUserById(userId);
        if (u == null) {
            res.put("success", false);
            res.put("message", "User not found.");
            return res;
        }

        boolean isWarden = "WARDEN".equalsIgnoreCase(u.getRole());
        List<Outpass> all = dataStore.getAllOutpasses();
        List<Map<String, Object>> list = new ArrayList<>();

        int pendingCount = 0;
        int approvedCount = 0;

        for (Outpass op : all) {
            if (isWarden || op.getStudentId().equals(userId)) {
                list.add(op.toMap());
                if ("PENDING".equalsIgnoreCase(op.getStatus())) pendingCount++;
                else if ("APPROVED".equalsIgnoreCase(op.getStatus())) approvedCount++;
            }
        }

        res.put("success", true);
        res.put("isWarden", isWarden);
        res.put("outpasses", list);
        res.put("settings", dataStore.getOutpassSetting().toMap());
        res.put("pendingCount", pendingCount);
        res.put("approvedCount", approvedCount);
        return res;
    }

    public Map<String, Object> requestOutpass(Map<String, Object> req, String userId) {
        Map<String, Object> res = new LinkedHashMap<>();
        User student = dataStore.getUserById(userId);
        if (student == null) {
            res.put("success", false);
            res.put("message", "Student profile not found.");
            return res;
        }

        String purpose = (String) req.get("purpose");
        String destination = (String) req.get("destination");
        String departureDate = (String) req.get("departureDate");
        String departureTime = (String) req.get("departureTime"); // HH:mm
        String returnDate = (String) req.get("returnDate");
        String returnTime = (String) req.get("returnTime");       // HH:mm
        boolean parentConsent = Boolean.TRUE.equals(req.get("parentConsentConfirmed"));

        if (purpose == null || purpose.trim().isEmpty() ||
            destination == null || destination.trim().isEmpty() ||
            departureDate == null || departureTime == null ||
            returnDate == null || returnTime == null) {
            res.put("success", false);
            res.put("message", "All outpass details (purpose, destination, departure date/time, return date/time) are mandatory.");
            return res;
        }

        OutpassSetting settings = dataStore.getOutpassSetting();

        // Validate Warden Permitted Time Limit
        boolean isWithinWindow = true;
        String warningMsg = null;
        if (settings.isEnforceTimeLimits() && departureTime.length() >= 4) {
            boolean inMorning = departureTime.compareTo(settings.getMorningStart()) >= 0 && departureTime.compareTo(settings.getMorningEnd()) <= 0;
            boolean inEvening = departureTime.compareTo(settings.getEveningStart()) >= 0 && departureTime.compareTo(settings.getEveningEnd()) <= 0;

            if (!inMorning && !inEvening) {
                isWithinWindow = false;
                warningMsg = "Note: Departure time " + departureTime + " is outside regular warden gate pass slots (" +
                        settings.getMorningStart() + "-" + settings.getMorningEnd() + " & " +
                        settings.getEveningStart() + "-" + settings.getEveningEnd() + "). Submitted as special permit request.";
            }
        }

        boolean isPrebooked = false;
        String today = sdf.format(new Date());
        if (departureDate.compareTo(today) > 0) {
            isPrebooked = true;
        }

        Outpass op = new Outpass();
        op.setStudentId(student.getId());
        op.setStudentName(student.getName());
        op.setRoomNo(student.getRoomNo());
        op.setDepartment(student.getDepartment());
        op.setPhone(student.getPhone());
        op.setParentPhone(student.getGuardianPhone());
        op.setPurpose(purpose.trim());
        op.setDestination(destination.trim());
        op.setDepartureDate(departureDate);
        op.setDepartureTime(departureTime);
        op.setReturnDate(returnDate);
        op.setReturnTime(returnTime);
        op.setPrebooked(isPrebooked);
        op.setParentConsentVerified(parentConsent);
        op.setStatus("PENDING");

        dataStore.addOutpass(op);
        dataStore.saveToFile();

        res.put("success", true);
        res.put("message", "Outpass token " + op.getTokenNo() + " booked successfully! Awaiting warden digital verification." + (warningMsg != null ? " " + warningMsg : ""));
        res.put("outpass", op.toMap());
        return res;
    }

    public Map<String, Object> updateSettings(Map<String, Object> data, String wardenId) {
        Map<String, Object> res = new LinkedHashMap<>();
        User warden = dataStore.getUserById(wardenId);
        if (warden == null || !"WARDEN".equalsIgnoreCase(warden.getRole())) {
            res.put("success", false);
            res.put("message", "Permission denied. Only wardens can configure outpass time limits.");
            return res;
        }

        OutpassSetting s = OutpassSetting.fromMap(data);
        dataStore.setOutpassSetting(s);
        dataStore.saveToFile();

        res.put("success", true);
        res.put("message", "Warden outpass time windows updated! Morning: " + s.getMorningStart() + "-" + s.getMorningEnd() + ", Evening: " + s.getEveningStart() + "-" + s.getEveningEnd());
        res.put("settings", s.toMap());
        return res;
    }

    public Map<String, Object> verifyOutpass(String outpassId, String action, String remarks, String wardenId) {
        Map<String, Object> res = new LinkedHashMap<>();
        User warden = dataStore.getUserById(wardenId);
        if (warden == null || !"WARDEN".equalsIgnoreCase(warden.getRole())) {
            res.put("success", false);
            res.put("message", "Permission denied. Only wardens can digitally verify outpasses.");
            return res;
        }

        Outpass op = dataStore.getOutpassById(outpassId);
        if (op == null) {
            res.put("success", false);
            res.put("message", "Outpass token not found.");
            return res;
        }

        if ("APPROVE".equalsIgnoreCase(action)) {
            op.setStatus("APPROVED");
            op.setVerifiedByWardenId(warden.getId());
            op.setVerifiedByWardenName(warden.getName());
            op.setVerifiedAt(System.currentTimeMillis());
            op.setDigitalVerificationHash("MARIAN-VERIFIED-" + op.getTokenNo() + "-WRD" + System.currentTimeMillis() % 10000);
            op.setWardenRemarks(remarks != null && !remarks.trim().isEmpty() ? remarks.trim() : "Digitally Verified & Approved by " + warden.getName());
            res.put("message", "✅ Outpass " + op.getTokenNo() + " approved with Warden Digital Seal!");
        } else {
            op.setStatus("REJECTED");
            op.setVerifiedByWardenId(warden.getId());
            op.setVerifiedByWardenName(warden.getName());
            op.setVerifiedAt(System.currentTimeMillis());
            op.setWardenRemarks(remarks != null && !remarks.trim().isEmpty() ? remarks.trim() : "Request rejected by Warden.");
            res.put("message", "Outpass request rejected.");
        }

        dataStore.saveToFile();

        res.put("success", true);
        res.put("outpass", op.toMap());
        return res;
    }
}
