@echo off
title Marian Hostel Management System
echo ==============================================================
echo       MARIAN HOSTEL MANAGEMENT SYSTEM - LAUNCHER
echo ==============================================================
echo.

if not exist "MarianHostel.jar" (
    echo Building JAR file first...
    python build.py
)

echo Starting Marian Hostel Server on port 8080...
echo Opening application in your web browser...
java -jar MarianHostel.jar 8080
pause
