package com.marian.hostel.service;

import com.marian.hostel.model.*;
import com.marian.hostel.repository.DataStore;
import java.util.*;

public class SeedData {

    public static void seed(DataStore ds) {
        // --- 1. WARDENS ---
        User w1 = new User();
        w1.setId("warden-01");
        w1.setName("Dr. Sister Rose Mary");
        w1.setEmail("rose@marianhostel.edu");
        w1.setPassword("warden123");
        w1.setRole("WARDEN");
        w1.setDepartment("Hostel Administration");
        w1.setPhone("+91 94471 23456");
        w1.setGender("Female");
        w1.setDob("1978-05-14");
        w1.setAddress("Marian Convent & Residence, Block A");
        w1.setNationality("Indian");
        w1.setAdmissionNo("EMP-WRD-001");
        w1.setAadhaar("7890-1234-5678");
        w1.setPhotoUrl("https://images.unsplash.com/photo-1544005313-94ddf0286df2?auto=format&fit=crop&w=300&q=80");
        w1.setActive(true);
        w1.setLoggedIn(false);
        ds.addUser(w1);

        User w2 = new User();
        w2.setId("warden-02");
        w2.setName("Rev. Fr. Thomas Mathew");
        w2.setEmail("thomas@marianhostel.edu");
        w2.setPassword("warden123");
        w2.setRole("WARDEN");
        w2.setDepartment("Hostel Discipline & Welfare");
        w2.setPhone("+91 94472 98765");
        w2.setGender("Male");
        w2.setDob("1982-11-20");
        w2.setAddress("Marian Fathers House, Block C");
        w2.setNationality("Indian");
        w2.setAdmissionNo("EMP-WRD-002");
        w2.setAadhaar("8901-2345-6789");
        w2.setPhotoUrl("https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?auto=format&fit=crop&w=300&q=80");
        w2.setActive(true);
        w2.setLoggedIn(false);
        ds.addUser(w2);

        // --- 2. STUDENTS ---
        User s1 = new User();
        s1.setId("student-01");
        s1.setName("Alex Joseph");
        s1.setEmail("alex@marian.edu");
        s1.setPassword("student123");
        s1.setRole("STUDENT");
        s1.setDepartment("Computer Science & Engineering");
        s1.setRoomNo("101");
        s1.setBedNo("L-1");
        s1.setPhone("+91 98950 11223");
        s1.setGender("Male");
        s1.setDob("2004-03-12");
        s1.setAddress("Hill View Villa, Kuttikkanam P.O, Idukki, Kerala");
        s1.setFatherName("Joseph Varghese");
        s1.setMotherName("Mary Joseph");
        s1.setGuardianPhone("+91 94460 33445");
        s1.setNationality("Indian");
        s1.setAdmissionNo("CS-2023-042");
        s1.setAadhaar("4321-8765-1092");
        s1.setPhotoUrl("https://images.unsplash.com/photo-1539571696357-5a69c17a67c6?auto=format&fit=crop&w=300&q=80");
        s1.setActive(true);
        s1.setLoggedIn(false);
        ds.addUser(s1);

        User s2 = new User();
        s2.setId("student-02");
        s2.setName("Sneha George");
        s2.setEmail("sneha@marian.edu");
        s2.setPassword("student123");
        s2.setRole("STUDENT");
        s2.setDepartment("Electronics & Communication");
        s2.setRoomNo("102");
        s2.setBedNo("L-1");
        s2.setPhone("+91 98470 55667");
        s2.setGender("Female");
        s2.setDob("2004-08-25");
        s2.setAddress("Grace Cottage, Pala, Kottayam, Kerala");
        s2.setFatherName("George Mathew");
        s2.setMotherName("Lissy George");
        s2.setGuardianPhone("+91 94470 88990");
        s2.setNationality("Indian");
        s2.setAdmissionNo("EC-2023-018");
        s2.setAadhaar("5432-1098-7654");
        s2.setPhotoUrl("https://images.unsplash.com/photo-1494790108377-be9c29b29330?auto=format&fit=crop&w=300&q=80");
        s2.setActive(true);
        s2.setLoggedIn(false);
        ds.addUser(s2);

        User s3 = new User();
        s3.setId("student-03");
        s3.setName("Rahul Sharma");
        s3.setEmail("rahul@marian.edu");
        s3.setPassword("student123");
        s3.setRole("STUDENT");
        s3.setDepartment("Mechanical Engineering");
        s3.setRoomNo("201");
        s3.setBedNo("L-1");
        s3.setPhone("+91 97440 22334");
        s3.setGender("Male");
        s3.setDob("2003-12-05");
        s3.setAddress("Flat 4B, Metro Residency, Ernakulam, Kerala");
        s3.setFatherName("Ramesh Sharma");
        s3.setMotherName("Sunita Sharma");
        s3.setGuardianPhone("+91 98460 77112");
        s3.setNationality("Indian");
        s3.setAdmissionNo("ME-2023-085");
        s3.setAadhaar("6543-2109-8765");
        s3.setPhotoUrl("https://images.unsplash.com/photo-1500648767791-00dcc994a43e?auto=format&fit=crop&w=300&q=80");
        s3.setActive(true);
        s3.setLoggedIn(false);
        ds.addUser(s3);

        User s4 = new User();
        s4.setId("student-04");
        s4.setName("Ananya Pillai");
        s4.setEmail("ananya@marian.edu");
        s4.setPassword("student123");
        s4.setRole("STUDENT");
        s4.setDepartment("Information Technology");
        s4.setRoomNo("202");
        s4.setBedNo("U-2");
        s4.setPhone("+91 96330 99887");
        s4.setGender("Female");
        s4.setDob("2004-06-18");
        s4.setAddress("Prasanth Nagar, Kowdiar, Thiruvananthapuram, Kerala");
        s4.setFatherName("Suresh Pillai");
        s4.setMotherName("Deepa Pillai");
        s4.setGuardianPhone("+91 94475 66778");
        s4.setNationality("Indian");
        s4.setAdmissionNo("IT-2023-031");
        s4.setAadhaar("7654-3210-9876");
        s4.setPhotoUrl("https://images.unsplash.com/photo-1517841905240-472988babdf9?auto=format&fit=crop&w=300&q=80");
        s4.setActive(true);
        s4.setLoggedIn(false);
        ds.addUser(s4);

        // --- 3. RAILWAY-STYLE ROOM ALLOCATION (Floors 1, 2, 3) ---
        String[] bedLabels3 = {"Lower Berth (L-1)", "Upper Berth (U-2)", "Window Berth (W-3)"};
        String[] bedLabels2 = {"Lower Berth (L-1)", "Upper Berth (U-2)"};

        // Floor 1 (Rooms 101 - 105)
        for (int r = 101; r <= 105; r++) {
            Room room = new Room(String.valueOf(r), 1, "3-Sharing Delux", 3, 4500.0);
            for (int b = 0; b < 3; b++) {
                String bedId = r + "-" + (b + 1);
                Bed bed = new Bed(bedId, bedLabels3[b], "VACANT");
                if (r == 101 && b == 0) {
                    bed.setStatus("OCCUPIED");
                    bed.setStudentId(s1.getId());
                    bed.setStudentName(s1.getName());
                    bed.setStudentDept(s1.getDepartment());
                } else if (r == 102 && b == 0) {
                    bed.setStatus("OCCUPIED");
                    bed.setStudentId(s2.getId());
                    bed.setStudentName(s2.getName());
                    bed.setStudentDept(s2.getDepartment());
                } else if (r == 103 && b == 1) {
                    bed.setStatus("MAINTENANCE");
                }
                room.getBeds().add(bed);
            }
            ds.addRoom(room);
        }

        // Floor 2 (Rooms 201 - 205)
        for (int r = 201; r <= 205; r++) {
            Room room = new Room(String.valueOf(r), 2, "3-Sharing Standard", 3, 4200.0);
            for (int b = 0; b < 3; b++) {
                String bedId = r + "-" + (b + 1);
                Bed bed = new Bed(bedId, bedLabels3[b], "VACANT");
                if (r == 201 && b == 0) {
                    bed.setStatus("OCCUPIED");
                    bed.setStudentId(s3.getId());
                    bed.setStudentName(s3.getName());
                    bed.setStudentDept(s3.getDepartment());
                } else if (r == 202 && b == 1) {
                    bed.setStatus("OCCUPIED");
                    bed.setStudentId(s4.getId());
                    bed.setStudentName(s4.getName());
                    bed.setStudentDept(s4.getDepartment());
                }
                room.getBeds().add(bed);
            }
            ds.addRoom(room);
        }

        // Floor 3 (Rooms 301 - 305) - 2 Sharing
        for (int r = 301; r <= 305; r++) {
            Room room = new Room(String.valueOf(r), 3, "2-Sharing Premium", 2, 5200.0);
            for (int b = 0; b < 2; b++) {
                String bedId = r + "-" + (b + 1);
                Bed bed = new Bed(bedId, bedLabels2[b], "VACANT");
                room.getBeds().add(bed);
            }
            ds.addRoom(room);
        }

        // --- 4. NOTICES (Including Sudden Emergency Alert) ---
        Notice n1 = new Notice();
        n1.setTitle("⚠️ Urgent: Overhead Water Tank Disinfection");
        n1.setContent("Routine cleaning and chlorination of all overhead water tanks will occur tomorrow from 1:00 PM to 4:00 PM. Water supply in all blocks will be briefly suspended. Please store required water in advance.");
        n1.setCategory("Emergency");
        n1.setEmergency(true);
        n1.setPostedBy("warden-01");
        n1.setPostedByName("Dr. Sister Rose Mary (Chief Warden)");
        n1.setDateFormatted("Nov 02, 2026 - 06:30 PM");
        ds.addNotice(n1);

        Notice n2 = new Notice();
        n2.setTitle("Hostel Annual Cultural Night 'Marian Aura 2026'");
        n2.setContent("Auditions for acoustic music, solo dance, and mime acts will be held this Saturday at 05:30 PM in the Recreational Hall. All hostel residents are encouraged to participate.");
        n2.setCategory("General");
        n2.setEmergency(false);
        n2.setPostedBy("warden-02");
        n2.setPostedByName("Rev. Fr. Thomas Mathew");
        n2.setDateFormatted("Nov 01, 2026 - 08:15 PM");
        ds.addNotice(n2);

        Notice n3 = new Notice();
        n3.setTitle("Mandatory Study Hours & Silence Enforcement");
        n3.setContent("In view of upcoming mid-semester examinations, strict silent study hours will be observed every night between 09:30 PM and 06:00 AM. Loud music and corridor roaming are strictly prohibited.");
        n3.setCategory("Academic");
        n3.setEmergency(false);
        n3.setPostedBy("warden-01");
        n3.setPostedByName("Dr. Sister Rose Mary");
        n3.setDateFormatted("Oct 28, 2026 - 07:00 PM");
        ds.addNotice(n3);

        // --- 5. HOSTEL TIMETABLE ---
        ds.addTimetable(new Timetable("06:00 AM - 06:45 AM", "Morning Fitness & Meditation", "Hostel gymnasium, badminton court & quiet reflection chapel open.", "All Days"));
        ds.addTimetable(new Timetable("07:15 AM - 08:30 AM", "Breakfast Hours", "Buffet breakfast served in main dining refectory. Gate opens at 07:15 AM.", "Mon - Sun"));
        ds.addTimetable(new Timetable("08:45 AM - 04:15 PM", "College Academic Hours", "Hostel gates open for classes. ID card mandatory at campus gate.", "Mon - Fri"));
        ds.addTimetable(new Timetable("04:30 PM - 05:30 PM", "Evening Tea & Snacks", "Hot tea/coffee and evening snacks. Games & sports on grounds.", "All Days"));
        ds.addTimetable(new Timetable("06:00 PM - 08:00 PM", "Evening Study / Library Hours", "Reading room & block study halls open. High-speed Wi-Fi prioritized.", "All Days"));
        ds.addTimetable(new Timetable("08:00 PM - 09:15 PM", "Dinner & Recreation", "Dinner served. Common TV lounge and indoor games accessible.", "All Days"));
        ds.addTimetable(new Timetable("09:30 PM - 09:45 PM", "Night Curfew & Roll-Call", "Hostel outer gates closed. Digital biometric roll-call by Warden.", "Mandatory Daily"));
        ds.addTimetable(new Timetable("10:30 PM - 06:00 AM", "Silent Night / Lights Out", "Corridor lights dimmed. Quiet individual room study allowed.", "Every Night"));

        // --- 6. MESS MENU (Monday to Sunday) ---
        ds.addMessMenu(new MessMenu("Monday", "Steamed Idli, Medu Vada, Coconut Chutney & Vegetable Sambar, Tea/Coffee", "Kerala Matta Rice, Fish Curry / Kadala Curry, Cabbage Thoran, Moru Curry, Papad", "Parippu Vada & Ginger Cardamom Tea", "Soft Chapatis, Paneer Butter Masala / Chicken Curry, Jeera Rice, Dal Fry", "Special Monday Kheer"));
        ds.addMessMenu(new MessMenu("Tuesday", "Appam with Vegetable Stew / Egg Roast, Fresh Banana, Tea/Coffee", "Steamed Rice, Moru Kootan, Sambar, Beetroot Thoran, Crisp Papadam, Pickle", "Banana Fritters (Pazham Pori) & Masala Chai", "Wheat Parotta / Chapatis, Mixed Veg Curry, Vegetable Pulao, Curd Salad", "Fresh Fruit Salad"));
        ds.addMessMenu(new MessMenu("Wednesday", "Puttu with Kadala Curry, Pappadam, Tea/Coffee", "Rice, Kerala Chicken Roast / Paneer Masala, Rasam, Beans Mezhukkupuratti, Curd", "Onion Pakoda & Fresh Lemon Tea", "Phulkas, Dal Tadka, Aloo Gobi Masala, Steamed Rice, Butter Milk", "Sweet Semiya Payasam"));
        ds.addMessMenu(new MessMenu("Thursday", "Masala Dosa, Tomato Chutney, Potato Masala, Sambar, Coffee", "Steamed White Rice, Tomato Rasam, Aviyal, Kovakka Mezhukkupuratti, Curd", "Cutlet (Veg / Egg) & Green Mint Dip, Tea", "Idiyappam with Green Peas Curry / Egg Kurma, Ghee Rice", "Carrot Halwa"));
        ds.addMessMenu(new MessMenu("Friday", "Wheat Upma with Green Peas / Banana, Tea/Coffee", "Kerala Brown Rice, Fish Fry / Paneer Fry, Pulissery, Cabbage Thoran, Rasam", "Samosa with Tamarind Chutney & Coffee", "Malabar Parotta / Chapatis, Chicken Chettinad / Mushroom Curry", "Pineapple Payasam"));
        ds.addMessMenu(new MessMenu("Saturday", "Poori Masala, Coconut Chutney, Tea/Coffee", "Variety Rice (Curd Rice & Lemon Rice), Potato Podimas, Crispy Appalam", "Biscuits, Masala Bun & Hot Tea", "Soft Rotis, Dal Makhani, Veg Fried Rice with Gobi Manchurian", "Ice Cream Cup"));
        ds.addMessMenu(new MessMenu("Sunday", "Bread Butter Jam, Boiled Eggs / Veg Cutlet, Tea/Coffee", "Marian Grand Sunday Biryani (Chicken Dum Biryani / Paneer Biryani), Raita, Mint Chutney, Pickle", "Sundal / Tea Cake & Bru Coffee", "Light Chapatis, Dal Fry, Jeera Rice, Papad, Curd", "Marian Special Ada Pradhaman"));

        // --- 7. COMPLAINTS WITH COMMENTS, LIKES & WARDEN TICK MARK ---
        Complaint c1 = new Complaint();
        c1.setId("comp-101");
        c1.setStudentId(s1.getId());
        c1.setStudentName(s1.getName());
        c1.setStudentRoom(s1.getRoomNo());
        c1.setStudentDept(s1.getDepartment());
        c1.setCategory("Wi-Fi/Network");
        c1.setTitle("High latency and frequent disconnects on 1st Floor Block A");
        c1.setDescription("The Wi-Fi access point in the corridor of 1st floor drops every 10 minutes, making online lab submissions very difficult.");
        c1.setDateFormatted("Nov 01, 2026 - 10:15 AM");
        c1.setStatus("PENDING");
        c1.setWardenTick(false);
        c1.getLikes().add(s2.getId());
        c1.getLikes().add(s3.getId());
        c1.getLikes().add(s4.getId());
        c1.getComments().add(new Comment(s2.getId(), s2.getName(), "STUDENT", "Facing the exact same issue in room 102! Please rectify soon.", "Nov 01, 11:00 AM"));
        c1.getComments().add(new Comment(w1.getId(), w1.getName(), "WARDEN", "Technician from IT cell has been contacted. Will be inspected today by 4 PM.", "Nov 01, 01:20 PM"));
        ds.addComplaint(c1);

        Complaint c2 = new Complaint();
        c2.setId("comp-102");
        c2.setStudentId(s3.getId());
        c2.setStudentName(s3.getName());
        c2.setStudentRoom(s3.getRoomNo());
        c2.setStudentDept(s3.getDepartment());
        c2.setCategory("Plumbing");
        c2.setTitle("Water heater temperature valve repaired in 2nd Floor Washroom");
        c2.setDescription("Geyser thermostat was tripping constantly and not producing warm water.");
        c2.setDateFormatted("Oct 29, 2026 - 08:30 AM");
        c2.setStatus("RESOLVED");
        c2.setWardenTick(true); // ✅ GREEN TICK MARK!
        c2.setResolvedBy("Dr. Sister Rose Mary");
        c2.setResolvedAt("Oct 29, 2026 - 04:45 PM");
        c2.setWardenRemarks("Heating element replaced by maintenance electrician Babu. Tested and operational.");
        c2.getLikes().add(s1.getId());
        c2.getLikes().add(s3.getId());
        c2.getComments().add(new Comment(s3.getId(), s3.getName(), "STUDENT", "Hot water is now working smoothly. Thank you Warden!", "Oct 29, 05:10 PM"));
        ds.addComplaint(c2);

        Complaint c3 = new Complaint();
        c3.setId("comp-103");
        c3.setStudentId(s4.getId());
        c3.setStudentName(s4.getName());
        c3.setStudentRoom(s4.getRoomNo());
        c3.setStudentDept(s4.getDepartment());
        c3.setCategory("Mess/Food");
        c3.setTitle("Request for jaggery/brown sugar option in evening tea");
        c3.setDescription("Many students prefer jaggery instead of refined white sugar for fitness and health reasons. Can we keep a small container in the counter?");
        c3.setDateFormatted("Oct 31, 2026 - 05:40 PM");
        c3.setStatus("PENDING");
        c3.setWardenTick(false);
        c3.getLikes().add(s1.getId());
        c3.getLikes().add(s2.getId());
        c3.getComments().add(new Comment(s1.getId(), s1.getName(), "STUDENT", "Great suggestion Ananya, would love to have this!", "Oct 31, 06:15 PM"));
        ds.addComplaint(c3);

        // --- 8. FEE RECORDS (Monthly tracking, Paid vs Pending) ---
        String[] months = {"September", "October", "November"};
        for (User st : Arrays.asList(s1, s2, s3, s4)) {
            // Sept - Paid
            FeeRecord fSept = new FeeRecord(st.getId(), st.getName(), st.getRoomNo(), "September", 2026, 4500, 3200, 500, "PAID");
            fSept.setPaymentDate("2026-09-04");
            fSept.setPaymentMode("UPI Online");
            fSept.setTransactionId("UPI-9847291084");
            fSept.setReceiptNo("MH-2026-09" + st.getRoomNo());
            ds.addFeeRecord(fSept);

            // Oct - Paid for Alex & Sneha, Pending for Rahul
            if (st.getId().equals(s3.getId())) {
                FeeRecord fOct = new FeeRecord(st.getId(), st.getName(), st.getRoomNo(), "October", 2026, 4200, 3200, 480, "PENDING");
                ds.addFeeRecord(fOct);
            } else {
                FeeRecord fOct = new FeeRecord(st.getId(), st.getName(), st.getRoomNo(), "October", 2026, 4500, 3200, 450, "PAID");
                fOct.setPaymentDate("2026-10-06");
                fOct.setPaymentMode("Net Banking");
                fOct.setTransactionId("NB-7788441122");
                fOct.setReceiptNo("MH-2026-10" + st.getRoomNo());
                ds.addFeeRecord(fOct);
            }

            // Nov - Current Month (Pending for everyone)
            FeeRecord fNov = new FeeRecord(st.getId(), st.getName(), st.getRoomNo(), "November", 2026, 4500, 3200, 500, "PENDING");
            ds.addFeeRecord(fNov);
        }

        // --- 9. ATTENDANCE (Marked by Warden) ---
        String today = "2026-11-02";
        String yesterday = "2026-11-01";
        ds.addAttendance(new Attendance(yesterday, "NIGHT_CURFEW", s1.getId(), s1.getName(), s1.getRoomNo(), "PRESENT", "Dr. Sister Rose Mary"));
        ds.addAttendance(new Attendance(yesterday, "NIGHT_CURFEW", s2.getId(), s2.getName(), s2.getRoomNo(), "PRESENT", "Dr. Sister Rose Mary"));
        ds.addAttendance(new Attendance(yesterday, "NIGHT_CURFEW", s3.getId(), s3.getName(), s3.getRoomNo(), "PRESENT", "Dr. Sister Rose Mary"));
        ds.addAttendance(new Attendance(yesterday, "NIGHT_CURFEW", s4.getId(), s4.getName(), s4.getRoomNo(), "ON_LEAVE", "Dr. Sister Rose Mary"));

        ds.addAttendance(new Attendance(today, "NIGHT_CURFEW", s1.getId(), s1.getName(), s1.getRoomNo(), "PRESENT", "Rev. Fr. Thomas Mathew"));
        ds.addAttendance(new Attendance(today, "NIGHT_CURFEW", s2.getId(), s2.getName(), s2.getRoomNo(), "PRESENT", "Rev. Fr. Thomas Mathew"));
        ds.addAttendance(new Attendance(today, "NIGHT_CURFEW", s3.getId(), s3.getName(), s3.getRoomNo(), "PRESENT", "Rev. Fr. Thomas Mathew"));
        ds.addAttendance(new Attendance(today, "NIGHT_CURFEW", s4.getId(), s4.getName(), s4.getRoomNo(), "PRESENT", "Rev. Fr. Thomas Mathew"));

        // --- 10. DIGITAL OUTPASS (Appointments & Verification) ---
        Outpass op1 = new Outpass();
        op1.setId("pass-101");
        op1.setTokenNo("MH-PASS-2026-8941");
        op1.setStudentId(s1.getId());
        op1.setStudentName(s1.getName());
        op1.setRoomNo(s1.getRoomNo());
        op1.setDepartment(s1.getDepartment());
        op1.setPhone(s1.getPhone());
        op1.setParentPhone(s1.getGuardianPhone());
        op1.setPurpose("Medical Consultation at Mary Queens Hospital");
        op1.setDestination("Kanjirappally Town");
        op1.setDepartureDate("2026-11-03");
        op1.setDepartureTime("16:15");
        op1.setReturnDate("2026-11-03");
        op1.setReturnTime("19:30");
        op1.setPrebooked(true);
        op1.setStatus("APPROVED");
        op1.setParentConsentVerified(true);
        op1.setVerifiedByWardenId(w1.getId());
        op1.setVerifiedByWardenName(w1.getName());
        op1.setDigitalVerificationHash("MARIAN-SEAL-VERIFIED-9801");
        op1.setWardenRemarks("Approved. Maintain prescription receipt and return before 7:30 PM.");
        op1.setVerifiedAt(System.currentTimeMillis() - 7200000);
        ds.addOutpass(op1);

        Outpass op2 = new Outpass();
        op2.setId("pass-102");
        op2.setTokenNo("MH-PASS-2026-3392");
        op2.setStudentId(s3.getId());
        op2.setStudentName(s3.getName());
        op2.setRoomNo(s3.getRoomNo());
        op2.setDepartment(s3.getDepartment());
        op2.setPhone(s3.getPhone());
        op2.setParentPhone(s3.getGuardianPhone());
        op2.setPurpose("Weekend Home Visit");
        op2.setDestination("Ernakulam Residence");
        op2.setDepartureDate("2026-11-06");
        op2.setDepartureTime("16:30");
        op2.setReturnDate("2026-11-08");
        op2.setReturnTime("18:00");
        op2.setPrebooked(true);
        op2.setStatus("PENDING");
        op2.setParentConsentVerified(true);
        ds.addOutpass(op2);

        // --- 11. EMERGENCY CONTACTS ---
        ds.addEmergencyContact(new EmergencyContact("Hostel Chief Warden", "Dr. Sister Rose Mary", "+91 94471 23456", "+91 94471 23457", "Warden Quarter Block A", "24x7 Available", 1));
        ds.addEmergencyContact(new EmergencyContact("Hostel Discipline Warden", "Rev. Fr. Thomas Mathew", "+91 94472 98765", "+91 94472 98760", "Fathers Residence Block C", "24x7 Available", 2));
        ds.addEmergencyContact(new EmergencyContact("Marian Health Center & Ambulance", "Duty Medical Officer & Driver", "+91 94460 11999", "04869-232555", "Health Clinic, Campus Gate 2", "24x7 Emergency", 3));
        ds.addEmergencyContact(new EmergencyContact("Campus Main Security Gate", "Chief Security Officer", "04869-232111", "+91 98470 12345", "Main Security Booth", "24x7 Active", 4));
        ds.addEmergencyContact(new EmergencyContact("St. Mary's Multi-Speciality Hospital", "Casualty & Emergency Desk", "04828-202020", "04828-202021", "Kanjirappally (3 km)", "24 Hours Emergency", 5));
        ds.addEmergencyContact(new EmergencyContact("Local Police Station", "Sub-Inspector of Police", "112 / 04869-232233", "04869-232234", "Peermade Police Station", "Emergency Call", 6));
        ds.addEmergencyContact(new EmergencyContact("Hostel Maintenance Electrician & Plumber", "Babu & Shaji", "+91 97450 44332", "+91 97450 44333", "Workshop Room 004", "07:00 AM - 08:00 PM", 7));

        // --- 12. DIRECT PRIVATE CHAT (Student & Warden) ---
        ChatMessage m1 = new ChatMessage();
        m1.setStudentId(s1.getId());
        m1.setStudentName(s1.getName());
        m1.setWardenId(w1.getId());
        m1.setWardenName(w1.getName());
        m1.setSenderId(s1.getId());
        m1.setSenderName(s1.getName());
        m1.setSenderRole("STUDENT");
        m1.setMessage("Good evening Sister, I submitted an outpass request for my medical appointment tomorrow. Kindly verify it when possible.");
        m1.setTimeFormatted("05:15 PM");
        m1.setRead(true);
        ds.addChatMessage(m1);

        ChatMessage m2 = new ChatMessage();
        m2.setStudentId(s1.getId());
        m2.setStudentName(s1.getName());
        m2.setWardenId(w1.getId());
        m2.setWardenName(w1.getName());
        m2.setSenderId(w1.getId());
        m2.setSenderName(w1.getName());
        m2.setSenderRole("WARDEN");
        m2.setMessage("Hello Alex, I reviewed your request and approved it with digital seal MH-PASS-2026-8941. Take care and return by 7:30 PM.");
        m2.setTimeFormatted("05:30 PM");
        m2.setRead(true);
        ds.addChatMessage(m2);
    }
}
