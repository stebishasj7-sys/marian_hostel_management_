package com.marian.hostel.repository;

import com.marian.hostel.config.AppConfig;
import com.marian.hostel.model.*;
import com.marian.hostel.service.SeedData;
import com.marian.hostel.util.JsonUtil;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;

public class DataStore {
    private static DataStore instance;

    private final Map<String, User> users = new LinkedHashMap<>();
    private final Map<String, Room> rooms = new LinkedHashMap<>();
    private final List<Notice> notices = new ArrayList<>();
    private final List<Timetable> timetables = new ArrayList<>();
    private final List<MessMenu> messMenus = new ArrayList<>();
    private final List<Complaint> complaints = new ArrayList<>();
    private final List<FeeRecord> feeRecords = new ArrayList<>();
    private final List<Attendance> attendances = new ArrayList<>();
    private final List<Outpass> outpasses = new ArrayList<>();
    private OutpassSetting outpassSetting = new OutpassSetting();
    private final List<ChatMessage> chatMessages = new ArrayList<>();
    private final List<EmergencyContact> emergencyContacts = new ArrayList<>();

    private DataStore() {
        loadFromFile();
        if (users.isEmpty()) {
            SeedData.seed(this);
            saveToFile();
        }
    }

    public static synchronized DataStore getInstance() {
        if (instance == null) {
            instance = new DataStore();
        }
        return instance;
    }

    // --- Persistence ---
    public synchronized void saveToFile() {
        try {
            File f = AppConfig.getDataFile();
            Map<String, Object> root = new LinkedHashMap<>();

            List<Map<String, Object>> userList = new ArrayList<>();
            for (User u : users.values()) userList.add(u.toMap());
            root.put("users", userList);

            List<Map<String, Object>> roomList = new ArrayList<>();
            for (Room r : rooms.values()) roomList.add(r.toMap());
            root.put("rooms", roomList);

            List<Map<String, Object>> noticeList = new ArrayList<>();
            for (Notice n : notices) noticeList.add(n.toMap());
            root.put("notices", noticeList);

            List<Map<String, Object>> ttList = new ArrayList<>();
            for (Timetable t : timetables) ttList.add(t.toMap());
            root.put("timetables", ttList);

            List<Map<String, Object>> menuList = new ArrayList<>();
            for (MessMenu m : messMenus) menuList.add(m.toMap());
            root.put("messMenus", menuList);

            List<Map<String, Object>> compList = new ArrayList<>();
            for (Complaint c : complaints) compList.add(c.toMap());
            root.put("complaints", compList);

            List<Map<String, Object>> feeList = new ArrayList<>();
            for (FeeRecord fe : feeRecords) feeList.add(fe.toMap());
            root.put("feeRecords", feeList);

            List<Map<String, Object>> attList = new ArrayList<>();
            for (Attendance a : attendances) attList.add(a.toMap());
            root.put("attendances", attList);

            List<Map<String, Object>> opList = new ArrayList<>();
            for (Outpass op : outpasses) opList.add(op.toMap());
            root.put("outpasses", opList);

            root.put("outpassSetting", outpassSetting.toMap());

            List<Map<String, Object>> chatList = new ArrayList<>();
            for (ChatMessage cm : chatMessages) chatList.add(cm.toMap());
            root.put("chatMessages", chatList);

            List<Map<String, Object>> contactList = new ArrayList<>();
            for (EmergencyContact ec : emergencyContacts) contactList.add(ec.toMap());
            root.put("emergencyContacts", contactList);

            String json = JsonUtil.toJson(root);
            Files.writeString(f.toPath(), json, StandardCharsets.UTF_8);
        } catch (Exception e) {
            System.err.println("[DataStore] Failed to save database file: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public synchronized void loadFromFile() {
        try {
            File f = AppConfig.getDataFile();
            if (!f.exists()) return;
            String json = Files.readString(f.toPath(), StandardCharsets.UTF_8);
            if (json == null || json.trim().isEmpty()) return;

            Map<String, Object> root = JsonUtil.parseObject(json);
            if (root.isEmpty()) return;

            users.clear();
            rooms.clear();
            notices.clear();
            timetables.clear();
            messMenus.clear();
            complaints.clear();
            feeRecords.clear();
            attendances.clear();
            outpasses.clear();
            chatMessages.clear();
            emergencyContacts.clear();

            if (root.get("users") instanceof List) {
                for (Object o : (List<Object>) root.get("users")) {
                    if (o instanceof Map) {
                        User u = User.fromMap((Map<String, Object>) o);
                        users.put(u.getId(), u);
                    }
                }
            }

            if (root.get("rooms") instanceof List) {
                for (Object o : (List<Object>) root.get("rooms")) {
                    if (o instanceof Map) {
                        Room r = Room.fromMap((Map<String, Object>) o);
                        rooms.put(r.getRoomNo(), r);
                    }
                }
            }

            if (root.get("notices") instanceof List) {
                for (Object o : (List<Object>) root.get("notices")) {
                    if (o instanceof Map) notices.add(Notice.fromMap((Map<String, Object>) o));
                }
            }

            if (root.get("timetables") instanceof List) {
                for (Object o : (List<Object>) root.get("timetables")) {
                    if (o instanceof Map) timetables.add(Timetable.fromMap((Map<String, Object>) o));
                }
            }

            if (root.get("messMenus") instanceof List) {
                for (Object o : (List<Object>) root.get("messMenus")) {
                    if (o instanceof Map) messMenus.add(MessMenu.fromMap((Map<String, Object>) o));
                }
            }

            if (root.get("complaints") instanceof List) {
                for (Object o : (List<Object>) root.get("complaints")) {
                    if (o instanceof Map) complaints.add(Complaint.fromMap((Map<String, Object>) o));
                }
            }

            if (root.get("feeRecords") instanceof List) {
                for (Object o : (List<Object>) root.get("feeRecords")) {
                    if (o instanceof Map) feeRecords.add(FeeRecord.fromMap((Map<String, Object>) o));
                }
            }

            if (root.get("attendances") instanceof List) {
                for (Object o : (List<Object>) root.get("attendances")) {
                    if (o instanceof Map) attendances.add(Attendance.fromMap((Map<String, Object>) o));
                }
            }

            if (root.get("outpasses") instanceof List) {
                for (Object o : (List<Object>) root.get("outpasses")) {
                    if (o instanceof Map) outpasses.add(Outpass.fromMap((Map<String, Object>) o));
                }
            }

            if (root.get("outpassSetting") instanceof Map) {
                this.outpassSetting = OutpassSetting.fromMap((Map<String, Object>) root.get("outpassSetting"));
            }

            if (root.get("chatMessages") instanceof List) {
                for (Object o : (List<Object>) root.get("chatMessages")) {
                    if (o instanceof Map) chatMessages.add(ChatMessage.fromMap((Map<String, Object>) o));
                }
            }

            if (root.get("emergencyContacts") instanceof List) {
                for (Object o : (List<Object>) root.get("emergencyContacts")) {
                    if (o instanceof Map) emergencyContacts.add(EmergencyContact.fromMap((Map<String, Object>) o));
                }
            }

            System.out.println("[DataStore] Loaded " + users.size() + " users, " + rooms.size() + " rooms.");
        } catch (Exception e) {
            System.err.println("[DataStore] Error loading data: " + e.getMessage());
        }
    }

    // --- Users & Warden Status ---
    public synchronized Collection<User> getAllUsers() { return users.values(); }
    public synchronized User getUserById(String id) { return users.get(id); }
    public synchronized User getUserByEmail(String email) {
        if (email == null) return null;
        for (User u : users.values()) {
            if (email.trim().equalsIgnoreCase(u.getEmail().trim())) {
                return u;
            }
        }
        return null;
    }

    public synchronized void addUser(User u) {
        users.put(u.getId(), u);
    }

    public synchronized int getActiveWardenCount() {
        int count = 0;
        for (User u : users.values()) {
            if ("WARDEN".equalsIgnoreCase(u.getRole()) && u.isLoggedIn()) {
                count++;
            }
        }
        return count;
    }

    public synchronized int getTotalWardenCount() {
        int count = 0;
        for (User u : users.values()) {
            if ("WARDEN".equalsIgnoreCase(u.getRole())) {
                count++;
            }
        }
        return count;
    }

    public synchronized boolean isWardenLoginAvailable() {
        // As requested: Once 2 wardens logged in, login as warden option is closed
        return getActiveWardenCount() < 2;
    }

    public synchronized void resetAllWardenLogins() {
        for (User u : users.values()) {
            if ("WARDEN".equalsIgnoreCase(u.getRole())) {
                u.setLoggedIn(false);
            }
        }
        saveToFile();
    }

    // --- Rooms ---
    public synchronized Collection<Room> getAllRooms() { return rooms.values(); }
    public synchronized Room getRoom(String roomNo) { return rooms.get(roomNo); }
    public synchronized void addRoom(Room r) { rooms.put(r.getRoomNo(), r); }

    // --- Notices ---
    public synchronized List<Notice> getAllNotices() { return new ArrayList<>(notices); }
    public synchronized void addNotice(Notice n) {
        notices.add(0, n); // newest first
    }
    public synchronized boolean deleteNotice(String id) {
        return notices.removeIf(n -> n.getId().equals(id));
    }

    // --- Timetable ---
    public synchronized List<Timetable> getTimetables() { return new ArrayList<>(timetables); }
    public synchronized void addTimetable(Timetable t) { timetables.add(t); }

    // --- Mess Menu ---
    public synchronized List<MessMenu> getMessMenus() { return new ArrayList<>(messMenus); }
    public synchronized void addMessMenu(MessMenu m) { messMenus.add(m); }
    public synchronized void updateMessMenu(MessMenu updated) {
        for (int i = 0; i < messMenus.size(); i++) {
            if (messMenus.get(i).getDay().equalsIgnoreCase(updated.getDay())) {
                messMenus.set(i, updated);
                return;
            }
        }
        messMenus.add(updated);
    }

    // --- Complaints ---
    public synchronized List<Complaint> getAllComplaints() { return new ArrayList<>(complaints); }
    public synchronized Complaint getComplaintById(String id) {
        for (Complaint c : complaints) {
            if (c.getId().equals(id)) return c;
        }
        return null;
    }
    public synchronized void addComplaint(Complaint c) {
        complaints.add(0, c);
    }

    // --- Fees ---
    public synchronized List<FeeRecord> getAllFeeRecords() { return new ArrayList<>(feeRecords); }
    public synchronized List<FeeRecord> getFeeRecordsForStudent(String studentId) {
        List<FeeRecord> list = new ArrayList<>();
        for (FeeRecord f : feeRecords) {
            if (f.getStudentId().equals(studentId)) list.add(f);
        }
        return list;
    }
    public synchronized void addFeeRecord(FeeRecord f) { feeRecords.add(f); }
    public synchronized FeeRecord getFeeRecordById(String id) {
        for (FeeRecord f : feeRecords) {
            if (f.getId().equals(id)) return f;
        }
        return null;
    }

    // --- Attendance ---
    public synchronized List<Attendance> getAllAttendances() { return new ArrayList<>(attendances); }
    public synchronized void addAttendance(Attendance a) { attendances.add(a); }
    public synchronized List<Attendance> getAttendanceByDateAndSession(String date, String session) {
        List<Attendance> list = new ArrayList<>();
        for (Attendance a : attendances) {
            if (a.getDate().equals(date) && a.getSession().equalsIgnoreCase(session)) {
                list.add(a);
            }
        }
        return list;
    }

    // --- Outpass ---
    public synchronized List<Outpass> getAllOutpasses() { return new ArrayList<>(outpasses); }
    public synchronized Outpass getOutpassById(String id) {
        for (Outpass o : outpasses) {
            if (o.getId().equals(id)) return o;
        }
        return null;
    }
    public synchronized void addOutpass(Outpass o) { outpasses.add(0, o); }
    public synchronized OutpassSetting getOutpassSetting() { return outpassSetting; }
    public synchronized void setOutpassSetting(OutpassSetting s) { this.outpassSetting = s; }

    // --- Chat ---
    public synchronized List<ChatMessage> getChatHistory(String studentId, String wardenId) {
        List<ChatMessage> list = new ArrayList<>();
        for (ChatMessage m : chatMessages) {
            if (m.getStudentId().equals(studentId)) {
                list.add(m);
            }
        }
        return list;
    }
    public synchronized void addChatMessage(ChatMessage m) { chatMessages.add(m); }
    public synchronized List<ChatMessage> getAllChatMessages() { return new ArrayList<>(chatMessages); }

    // --- Emergency Contacts ---
    public synchronized List<EmergencyContact> getEmergencyContacts() { return new ArrayList<>(emergencyContacts); }
    public synchronized void addEmergencyContact(EmergencyContact c) { emergencyContacts.add(c); }
    public synchronized void updateEmergencyContact(EmergencyContact c) {
        for (int i = 0; i < emergencyContacts.size(); i++) {
            if (emergencyContacts.get(i).getId().equals(c.getId())) {
                emergencyContacts.set(i, c);
                return;
            }
        }
        emergencyContacts.add(c);
    }
    public synchronized boolean deleteEmergencyContact(String id) {
        return emergencyContacts.removeIf(c -> c.getId().equals(id));
    }
}
