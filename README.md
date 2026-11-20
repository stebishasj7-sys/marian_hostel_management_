# 🏛️ Marian Hostel Management System
### Enterprise Full-Stack Java Application & Micro-Project

An advanced, production-grade **Hostel Management System** crafted specifically for **Marian Hostel**. Built in pure, zero-dependency Java on the backend with an embedded high-performance REST HTTP server and a modern, responsive Single Page Application (SPA) web frontend.

---

## 🚀 How to Run the Project

### Quick 1-Click Launch (Windows)
Simply double-click **`run.bat`** in the project folder.  
It will automatically start the server on **`http://localhost:8080`** and launch your default browser!

### Running via Command Line
```bash
# If using the pre-compiled JAR:
java -jar MarianHostel.jar 8080

# To re-compile the project from scratch at any time:
python build.py
# or double click build.bat
```

Open **[http://localhost:8080](http://localhost:8080)** in your browser.

---

## 🔑 Pre-Seeded Demo Credentials

| Role | Name | Email | Password | Details |
| :--- | :--- | :--- | :--- | :--- |
| **Warden (Admin 1)** | Dr. Sister Rose Mary | `rose@marianhostel.edu` | `warden123` | Chief Warden |
| **Warden (Admin 2)** | Rev. Fr. Thomas Mathew | `thomas@marianhostel.edu` | `warden123` | Discipline & Welfare |
| **Student 1** | Alex Joseph | `alex@marian.edu` | `student123` | Room 101, Bed L-1 (CSE) |
| **Student 2** | Sneha George | `sneha@marian.edu` | `student123` | Room 102, Bed L-1 (ECE) |
| **Student 3** | Rahul Sharma | `rahul@marian.edu` | `student123` | Room 201, Bed L-1 (MECH) |
| **Student 4** | Ananya Pillai | `ananya@marian.edu` | `student123` | Room 202, Bed U-2 (IT) |

> 💡 **Self-Registration**: Anyone can also register a new student account using the **Register** tab!

---

## ✨ Core Feature Highlights

### 1. 🛡️ Dynamic Dual Login & 2-Warden Session Enforcement
- **Student & Warden Tabbed Login**: Anyone can log in as a student or warden.
- **2-Warden Session Lock**: As strictly specified, once 2 wardens are actively logged in, the **"Warden Login"** tab is automatically closed and completely hidden from incoming visitors! Incoming users only see the Student Login screen.
- A **Reset Sessions** utility button is provided on the login card for easy demonstration and grading.

### 2. 👤 Complete Resident Profile & Photo Management
- **Full Profile Fields**: Name, Department/Branch, Room No, Berth No, Mobile, Email, Gender, Date of Birth, Permanent Residential Address, Father's Name, Mother's Name, Guardian Emergency Phone, Nationality, Admission Number, Aadhaar Number.
- **Profile Photo Upload**: Students can upload their profile photo (local image file reader with Base64 encoding or image URL).
- Students can edit their personal details anytime with instant persistence.

### 3. Room & Berth Allocation
- **Train Coach / Berth Visualizer**:
  - Floor 1 (Rooms 101 - 105, 3-Sharing Delux)
  - Floor 2 (Rooms 201 - 205, 3-Sharing Standard)
  - Floor 3 (Rooms 301 - 305, 2-Sharing Premium)
- **Berth Types**: Lower Berth (L-1), Upper Berth (U-2), Window Berth (W-3).
- **Color-Coded Status**:
  - 🟢 **Green (Vacant)**: Click to instantly reserve/allocate berth.
  - 🔴 **Red (Occupied)**: Click to view resident occupant details (Name, Dept, Admission No).
  - 🟡 **Yellow (Maintenance)**: Under cleaning/repair.
- Real-time occupancy statistics (% full, total berths, available berths).
- Wardens have one-click controls to allocate or vacate berths.

### 4. 📢 Complaint Management with Likes, Comments & Warden Green Tick Mark
- Students can submit complaints across categories: Electrical, Plumbing, Wi-Fi, Food, Cleanliness, Furniture, Noise, etc.
- **Community Social Engagement**: Other residents can **like / upvote** complaints (with real-time counter) and post threaded **comments**.
- **✅ Warden Green Tick Mark**: When the warden clears/resolves an issue, a prominent **VERIFIED & RESOLVED** green tick badge is stamped on the complaint card, visible to everyone in the hostel.

### 5. 🎫 Hospital-Appointment Style Digital Outpass / Gate Pass
- **Hospital Appointment Flow**: Students apply for gate pass with purpose, destination, departure date/time, expected return date/time, and parent consent confirmation.
- **Warden Time-Limit Control**: Wardens set the permitted pass booking windows (e.g., Morning 08:00 AM - 09:00 AM, Evening 04:00 PM - 05:00 PM, and Night Curfew deadline).
- **Pre-Booking Supported**: Students can pre-book future dates.
- **Cryptographic Warden Verification Seal**: Approved passes generate an official **Digital Outpass Voucher** with a unique token number (e.g. `MH-PASS-2026-8941`), digital verification hash, and print-ready format for security gate officers.

### 6. 💰 Monthly Fee Tracking & Payment Gateway Simulation
- Monthly ledger (Room Rent, Mess Charges, Electricity & Maintenance).
- Color-coded status: Paid (Green), Pending (Amber), Overdue (Red).
- Student payment gateway simulation with instant **Official Printable Receipt** generation containing transaction ID and receipt number.
- Wardens can review hostel-wide fee collection statistics, edit dues, and record manual counter cash payments.

### 7. 📅 Night Curfew Attendance Register
- **Warden Roll-Call**: Wardens select date and session (Night Curfew 09:30 PM / Morning Assembly 07:00 AM), view students arranged by room, and mark Present, Absent, or On-Leave.
- **One-Click "Mark All Present"** for rapid night roll-calls.
- **Student Attendance Meter**: Students can view their personal attendance score (% presence, total days, verified leaves).

### 8. 🍽️ Day-Wise Mess Menu & Hostel Timetable
- Comprehensive 7-day weekly mess menu (Monday through Sunday) covering Breakfast, Lunch, Evening Snacks & Tea, Dinner, and Sunday Specials.
- Daily hostel routine timetable (Curfew, silent study hours, dining hours).
- Wardens can edit any day's menu in real time.

### 9. 🚨 Sudden Emergency Alerts & Notice Board
- Notice board with categories: General, Emergency, Mess, Fee, Academic, Cultural.
- **Sudden Emergency Broadcast**: Wardens can trigger an emergency flash alert which immediately activates a pulsating red banner at the top of every resident's screen!

### 10. 💬 Direct Confidential Warden - Student 1-on-1 Chat
- End-to-end private channel between resident and warden.
- Complete student-to-student privacy (students only see their own conversation with the warden).
- Wardens can select any student from their private threads list and respond in real time.

### 11. 📞 Emergency Helpline Directory
- Quick contact cards for Chief Warden, Discipline Warden, Campus Health Clinic & Ambulance, Security Gate, Hospital Casualty, and Police Station.
- One-click `tel:` direct dialing. Wardens can add, edit, or delete directory contacts.

---

## 🏗️ Architecture & Technology Stack

- **Backend**: Pure Java 26 / Standard JDK (`jdk.httpserver`).
  - Zero external Maven or Gradle runtime dependencies required!
  - Custom high-speed recursive-descent JSON parser & serializer (`JsonUtil.java`).
  - Thread-safe repository layer with JSON file persistence (`data/marian_hostel_db.json`).
  - Modular service architecture (`AuthService`, `RoomService`, `OutpassService`, `ComplaintService`, etc.).
- **Frontend**: Clean Single Page Application (HTML5, Modern CSS3 with Glassmorphism, Vanilla ES6+ JavaScript, FontAwesome 6).
- **Storage**: Automatic disk persistence in `data/marian_hostel_db.json`. Changes persist across server restarts.

---

## 📁 Project Directory Structure
```
marian-hostel/
├── src/
│   └── com/marian/hostel/
│       ├── Main.java                          // Application entry point, server starter & browser launcher
│       ├── config/AppConfig.java              // Port and file configurations
│       ├── model/                             // Data Models (User, Room, Bed, Notice, Outpass, etc.)
│       ├── repository/DataStore.java          // In-memory data store with JSON persistence
│       ├── service/                           // Business logic & services
│       ├── handler/                           // REST HTTP Context Handlers
│       └── util/                              // JsonUtil & HttpHelper
├── web/                                       // Modern Single Page Application
│   ├── index.html                             // SPA shell & UI templates
│   ├── css/styles.css                         // Glassmorphism & IRCTC berth styling
│   └── js/                                    // Modular Frontend controllers
├── data/                                      // Persistent JSON database
├── MarianHostel.jar                           // Compiled, standalone executable JAR
├── build.py & build.bat                       // Cross-platform build scripts
├── run.bat                                    // 1-Click Windows launcher
└── README.md                                  // Comprehensive documentation
```

---
*Developed for Marian Hostel Management System Micro-Project.*
