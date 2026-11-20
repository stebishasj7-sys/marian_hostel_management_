package com.marian.hostel.service;

import com.marian.hostel.model.*;
import com.marian.hostel.repository.DataStore;

import java.util.*;

public class RoomService {
    private final DataStore dataStore = DataStore.getInstance();

    public Map<String, Object> getRoomGrid() {
        Map<String, Object> res = new LinkedHashMap<>();
        Collection<Room> allRooms = dataStore.getAllRooms();

        int totalBeds = 0;
        int occupiedBeds = 0;
        int vacantBeds = 0;
        int maintenanceBeds = 0;

        Map<Integer, List<Map<String, Object>>> floorMap = new TreeMap<>();

        for (Room r : allRooms) {
            totalBeds += r.getCapacity();
            for (Bed b : r.getBeds()) {
                if ("OCCUPIED".equalsIgnoreCase(b.getStatus())) occupiedBeds++;
                else if ("VACANT".equalsIgnoreCase(b.getStatus())) vacantBeds++;
                else maintenanceBeds++;
            }
            floorMap.computeIfAbsent(r.getFloor(), k -> new ArrayList<>()).add(r.toMap());
        }

        double occupancyRate = totalBeds > 0 ? ((double) occupiedBeds / totalBeds) * 100 : 0;

        res.put("totalRooms", allRooms.size());
        res.put("totalBeds", totalBeds);
        res.put("occupiedBeds", occupiedBeds);
        res.put("vacantBeds", vacantBeds);
        res.put("maintenanceBeds", maintenanceBeds);
        res.put("occupancyRate", Math.round(occupancyRate * 10.0) / 10.0);
        res.put("floors", floorMap);
        return res;
    }

    public Map<String, Object> allocateBed(String roomNo, String bedId, String studentId) {
        Map<String, Object> res = new LinkedHashMap<>();
        Room r = dataStore.getRoom(roomNo);
        if (r == null) {
            res.put("success", false);
            res.put("message", "Room " + roomNo + " does not exist.");
            return res;
        }

        User u = dataStore.getUserById(studentId);
        if (u == null) {
            res.put("success", false);
            res.put("message", "Student not found.");
            return res;
        }

        Bed targetBed = null;
        for (Bed b : r.getBeds()) {
            if (b.getBedId().equalsIgnoreCase(bedId)) {
                targetBed = b;
                break;
            }
        }

        if (targetBed == null) {
            res.put("success", false);
            res.put("message", "Bed " + bedId + " not found in room " + roomNo);
            return res;
        }

        if ("OCCUPIED".equalsIgnoreCase(targetBed.getStatus()) && !studentId.equals(targetBed.getStudentId())) {
            res.put("success", false);
            res.put("message", "This berth is already booked/occupied by " + targetBed.getStudentName());
            return res;
        }

        targetBed.setStatus("OCCUPIED");
        targetBed.setStudentId(u.getId());
        targetBed.setStudentName(u.getName());
        targetBed.setStudentDept(u.getDepartment());

        u.setRoomNo(roomNo);
        u.setBedNo(targetBed.getBedLabel());

        dataStore.saveToFile();

        res.put("success", true);
        res.put("message", "Berth " + targetBed.getBedLabel() + " in Room " + roomNo + " successfully allocated to " + u.getName() + "!");
        res.put("room", r.toMap());
        return res;
    }

    public Map<String, Object> vacateBed(String roomNo, String bedId) {
        Map<String, Object> res = new LinkedHashMap<>();
        Room r = dataStore.getRoom(roomNo);
        if (r == null) {
            res.put("success", false);
            res.put("message", "Room not found.");
            return res;
        }

        for (Bed b : r.getBeds()) {
            if (b.getBedId().equalsIgnoreCase(bedId)) {
                if (b.getStudentId() != null && !b.getStudentId().isEmpty()) {
                    User u = dataStore.getUserById(b.getStudentId());
                    if (u != null) {
                        u.setRoomNo("");
                        u.setBedNo("");
                    }
                }
                b.setStatus("VACANT");
                b.setStudentId("");
                b.setStudentName("");
                b.setStudentDept("");
                dataStore.saveToFile();

                res.put("success", true);
                res.put("message", "Berth " + b.getBedLabel() + " is now vacated and marked VACANT.");
                return res;
            }
        }

        res.put("success", false);
        res.put("message", "Bed not found.");
        return res;
    }
}
