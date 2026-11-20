package com.marian.hostel;

import com.marian.hostel.config.AppConfig;
import com.marian.hostel.handler.*;
import com.marian.hostel.repository.DataStore;
import com.sun.net.httpserver.HttpServer;

import java.awt.Desktop;
import java.io.File;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.concurrent.Executors;

public class Main {
    private static HttpServer server;

    public static void main(String[] args) {
        int port = AppConfig.DEFAULT_PORT;
        if (args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException ignored) {}
        }

        // Try binding to port, fallback if busy
        int maxAttempts = 5;
        for (int i = 0; i < maxAttempts; i++) {
            try {
                server = HttpServer.create(new InetSocketAddress(port), 0);
                break;
            } catch (Exception e) {
                System.out.println("[Server] Port " + port + " is busy, trying " + (port + 1) + "...");
                port++;
            }
        }

        if (server == null) {
            System.err.println("[Server] Could not bind to any port!");
            System.exit(1);
        }

        // Initialize DataStore & Seed Data
        DataStore ds = DataStore.getInstance();

        // Register API Handlers
        server.createContext("/api/auth", new ApiAuthHandler());
        server.createContext("/api/profile", new ApiProfileHandler());
        server.createContext("/api/rooms", new ApiRoomHandler());
        server.createContext("/api/notices", new ApiNoticeHandler());
        server.createContext("/api/mess-timetable", new ApiMessTimetableHandler());
        server.createContext("/api/complaints", new ApiComplaintHandler());
        server.createContext("/api/fees", new ApiFeeHandler());
        server.createContext("/api/attendance", new ApiAttendanceHandler());
        server.createContext("/api/outpass", new ApiOutpassHandler());
        server.createContext("/api/chat", new ApiChatHandler());
        server.createContext("/api/emergency", new ApiEmergencyHandler());

        // Static Web Files Handler
        String webDir = "web";
        if (!new File(webDir).exists()) {
            webDir = "../web";
        }
        server.createContext("/", new StaticFileHandler(webDir));

        server.setExecutor(Executors.newCachedThreadPool());
        server.start();

        final int activePort = port;
        String url = "http://localhost:" + activePort;

        printBanner(activePort);

        // Auto-open browser
        try {
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(new URI(url));
            }
        } catch (Throwable t) {
            // Headless or browse not supported
        }

        // Shutdown hook to save data cleanly
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("\n[Server] Shutting down Marian Hostel Server... saving data.");
            DataStore.getInstance().saveToFile();
            if (server != null) {
                server.stop(1);
            }
        }));
    }

    private static void printBanner(int port) {
        System.out.println("==================================================================");
        System.out.println("   🏛️   MARIAN HOSTEL MANAGEMENT SYSTEM - JAVA MICRO-PROJECT      ");
        System.out.println("==================================================================");
        System.out.println("   Status   :  ACTIVE & RUNNING                                    ");
        System.out.println("   Web URL  :  http://localhost:" + port + "                         ");
        System.out.println("   Version  :  " + AppConfig.VERSION + "           ");
        System.out.println("------------------------------------------------------------------");
        System.out.println("   ⭐ Demo Logins:                                                 ");
        System.out.println("   1. Warden Login : rose@marianhostel.edu    / Pass: warden123     ");
        System.out.println("   2. Warden Login : thomas@marianhostel.edu  / Pass: warden123     ");
        System.out.println("   3. Student Login: alex@marian.edu          / Pass: student123    ");
        System.out.println("   4. Student Login: sneha@marian.edu         / Pass: student123    ");
        System.out.println("   5. Student Login: rahul@marian.edu         / Pass: student123    ");
        System.out.println("   * Self-Registration is open for anyone on the Register tab!     ");
        System.out.println("==================================================================");
        System.out.println("   Press Ctrl+C to stop the server at any time.                   \n");
    }
}
