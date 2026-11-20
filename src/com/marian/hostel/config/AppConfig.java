package com.marian.hostel.config;

import java.io.File;

public class AppConfig {
    public static final int DEFAULT_PORT = 8080;
    public static final String APP_NAME = "Marian Hostel Management System";
    public static final String VERSION = "1.0.0 Enterprise Micro-Project";
    public static final String DATA_FILE = "data/marian_hostel_db.json";

    public static File getDataFile() {
        File f = new File(DATA_FILE);
        File parent = f.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }
        return f;
    }
}
