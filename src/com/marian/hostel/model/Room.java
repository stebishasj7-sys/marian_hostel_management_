package com.marian.hostel.model;

import java.util.*;

public class Room {
    private String roomNo;
    private int floor;
    private String roomType; // "2-Bed", "3-Bed", "4-Bed"
    private int capacity;
    private double monthlyRent;
    private List<Bed> beds = new ArrayList<>();

    public Room() {}

    public Room(String roomNo, int floor, String roomType, int capacity, double monthlyRent) {
        this.roomNo = roomNo;
        this.floor = floor;
        this.roomType = roomType;
        this.capacity = capacity;
        this.monthlyRent = monthlyRent;
    }

    public int getOccupiedCount() {
        int count = 0;
        for (Bed b : beds) {
            if ("OCCUPIED".equalsIgnoreCase(b.getStatus())) {
                count++;
            }
        }
        return count;
    }

    public int getVacantCount() {
        int count = 0;
        for (Bed b : beds) {
            if ("VACANT".equalsIgnoreCase(b.getStatus())) {
                count++;
            }
        }
        return count;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("roomNo", roomNo);
        map.put("floor", floor);
        map.put("roomType", roomType);
        map.put("capacity", capacity);
        map.put("monthlyRent", monthlyRent);
        map.put("occupiedCount", getOccupiedCount());
        map.put("vacantCount", getVacantCount());
        List<Map<String, Object>> bedList = new ArrayList<>();
        for (Bed b : beds) {
            bedList.add(b.toMap());
        }
        map.put("beds", bedList);
        return map;
    }

    @SuppressWarnings("unchecked")
    public static Room fromMap(Map<String, Object> map) {
        Room r = new Room();
        if (map.containsKey("roomNo")) r.setRoomNo((String) map.get("roomNo"));
        if (map.containsKey("floor") && map.get("floor") instanceof Number) {
            r.setFloor(((Number) map.get("floor")).intValue());
        }
        if (map.containsKey("roomType")) r.setRoomType((String) map.get("roomType"));
        if (map.containsKey("capacity") && map.get("capacity") instanceof Number) {
            r.setCapacity(((Number) map.get("capacity")).intValue());
        }
        if (map.containsKey("monthlyRent") && map.get("monthlyRent") instanceof Number) {
            r.setMonthlyRent(((Number) map.get("monthlyRent")).doubleValue());
        }
        if (map.containsKey("beds") && map.get("beds") instanceof List) {
            List<Object> rawList = (List<Object>) map.get("beds");
            for (Object obj : rawList) {
                if (obj instanceof Map) {
                    r.getBeds().add(Bed.fromMap((Map<String, Object>) obj));
                }
            }
        }
        return r;
    }

    public String getRoomNo() { return roomNo; }
    public void setRoomNo(String roomNo) { this.roomNo = roomNo; }
    public int getFloor() { return floor; }
    public void setFloor(int floor) { this.floor = floor; }
    public String getRoomType() { return roomType; }
    public void setRoomType(String roomType) { this.roomType = roomType; }
    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }
    public double getMonthlyRent() { return monthlyRent; }
    public void setMonthlyRent(double monthlyRent) { this.monthlyRent = monthlyRent; }
    public List<Bed> getBeds() { return beds; }
    public void setBeds(List<Bed> beds) { this.beds = beds; }
}
