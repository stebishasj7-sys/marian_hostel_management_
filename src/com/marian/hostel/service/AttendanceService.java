package com.marian.hostel.service;

import com.marian.hostel.model.*;
import com.marian.hostel.repository.DataStore;

import java.text.SimpleDateFormat;
import java.util.*;

public class AttendanceService {
    private final DataStore dataStore = DataStore.getInstance();
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public Map<String, Object> getDailyAttendance(String date, String session) {
        if (date == null || date.isEmpty()) date = sdf.format(new Date());
        if (session == null || session.isEmpty()) session = "NIGHT_CURFEW";

        Map<String, Object> res = new LinkedHashMap<>();
        List<Attendance> recorded = dataStore.getAttendanceByDateAndSession(date, session);

        Map<String, Attendance> studentMap = new HashMap<>();
        for (Attendance a : recorded) {
            studentMap.put(a.getStudentId(), a);
        }

        List<Map<String, Object>> studentList = new ArrayList<>();
        int present = 0;
        int absent = 0;
        int onLeave = 0;

        for (User u : dataStore.getAllUsers()) {
            if ("STUDENT".equalsIgnoreCase(u.getRole())) {
                Map<String, Object> item = new LinkedHashMap<>();
                item.put("studentId", u.getId());
                item.put("name", u.getName());
                item.put("roomNo", u.getRoomNo());
                item.put("department", u.getDepartment());

                Attendance a = studentMap.get(u.getId());
                if (a != null) {
                    item.put("status", a.getStatus());
                    item.put("markedBy", a.getMarkedBy());
                    if ("PRESENT".equalsIgnoreCase(a.getStatus())) present++;
                    else if ("ABSENT".equalsIgnoreCase(a.getStatus())) absent++;
                    else onLeave++;
                } else {
                    item.put("status", "NOT_MARKED");
                    item.put("markedBy", "");
                }
                studentList.add(item);
            }
        }

        res.put("success", true);
        res.put("date", date);
        res.put("session", session);
        res.put("totalStudents", studentList.size());
        res.put("presentCount", present);
        res.put("absentCount", absent);
        res.put("onLeaveCount", onLeave);
        res.put("students", studentList);
        return res;
    }

    public Map<String, Object> markSingleAttendance(String studentId, String status, String date, String session, String wardenId) {
        Map<String, Object> res = new LinkedHashMap<>();
        User warden = dataStore.getUserById(wardenId);
        if (warden == null || !"WARDEN".equalsIgnoreCase(warden.getRole())) {
            res.put("success", false);
            res.put("message", "Permission denied. Only wardens can mark attendance.");
            return res;
        }

        User student = dataStore.getUserById(studentId);
        if (student == null) {
            res.put("success", false);
            res.put("message", "Student not found.");
            return res;
        }

        if (date == null || date.isEmpty()) date = sdf.format(new Date());
        if (session == null || session.isEmpty()) session = "NIGHT_CURFEW";

        List<Attendance> list = dataStore.getAttendanceByDateAndSession(date, session);
        Attendance existing = null;
        for (Attendance a : list) {
            if (a.getStudentId().equals(studentId)) {
                existing = a;
                break;
            }
        }

        if (existing != null) {
            existing.setStatus(status);
            existing.setMarkedBy(warden.getName());
        } else {
            Attendance a = new Attendance(date, session, student.getId(), student.getName(), student.getRoomNo(), status, warden.getName());
            dataStore.addAttendance(a);
        }

        dataStore.saveToFile();

        res.put("success", true);
        res.put("message", "Attendance for " + student.getName() + " marked as " + status + ".");
        return res;
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> markBulkAttendance(Map<String, Object> payload, String wardenId) {
        Map<String, Object> res = new LinkedHashMap<>();
        User warden = dataStore.getUserById(wardenId);
        if (warden == null || !"WARDEN".equalsIgnoreCase(warden.getRole())) {
            res.put("success", false);
            res.put("message", "Permission denied. Only wardens can mark attendance.");
            return res;
        }

        String date = (String) payload.getOrDefault("date", sdf.format(new Date()));
        String session = (String) payload.getOrDefault("session", "NIGHT_CURFEW");
        List<Map<String, Object>> entries = (List<Map<String, Object>>) payload.get("entries");

        if (entries != null) {
            for (Map<String, Object> e : entries) {
                String studentId = (String) e.get("studentId");
                String status = (String) e.get("status");
                if (studentId != null && status != null) {
                    markSingleAttendance(studentId, status, date, session, wardenId);
                }
            }
        }

        res.put("success", true);
        res.put("message", "Roll-call attendance recorded successfully for " + date + "!");
        return res;
    }

    public Map<String, Object> getStudentAttendanceSummary(String studentId) {
        Map<String, Object> res = new LinkedHashMap<>();
        int total = 0;
        int present = 0;
        int absent = 0;
        int onLeave = 0;

        List<Map<String, Object>> history = new ArrayList<>();
        for (Attendance a : dataStore.getAllAttendances()) {
            if (a.getStudentId().equals(studentId)) {
                total++;
                if ("PRESENT".equalsIgnoreCase(a.getStatus())) present++;
                else if ("ABSENT".equalsIgnoreCase(a.getStatus())) absent++;
                else if ("ON_LEAVE".equalsIgnoreCase(a.getStatus())) onLeave++;
                history.add(a.toMap());
            }
        }

        double percentage = total > 0 ? ((double) present / total) * 100 : 100.0;

        res.put("success", true);
        res.put("totalDays", total);
        res.put("presentDays", present);
        res.put("absentDays", absent);
        res.put("leaveDays", onLeave);
        res.put("attendancePercentage", Math.round(percentage * 10.0) / 10.0);
        res.put("history", history);
        return res;
    }
}
