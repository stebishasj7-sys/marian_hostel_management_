package com.marian.hostel.model;

import java.util.*;

public class FeeRecord {
    private String id;
    private String studentId;
    private String studentName;
    private String studentRoom;
    private String month; // e.g. "January", "February"
    private int year;     // e.g. 2026
    private double rentAmount;
    private double messAmount;
    private double electricityAmount;
    private double totalAmount;
    private String status; // "PAID", "PENDING", "OVERDUE"
    private String paymentDate;
    private String transactionId;
    private String paymentMode; // "UPI", "Net Banking", "Debit/Credit Card", "Cash / Counter"
    private String receiptNo;
    private String remarks;

    public FeeRecord() {
        this.id = UUID.randomUUID().toString();
        this.status = "PENDING";
    }

    public FeeRecord(String studentId, String studentName, String studentRoom, String month, int year,
                     double rentAmount, double messAmount, double electricityAmount, String status) {
        this.id = UUID.randomUUID().toString();
        this.studentId = studentId;
        this.studentName = studentName;
        this.studentRoom = studentRoom;
        this.month = month;
        this.year = year;
        this.rentAmount = rentAmount;
        this.messAmount = messAmount;
        this.electricityAmount = electricityAmount;
        this.totalAmount = rentAmount + messAmount + electricityAmount;
        this.status = status;
        if ("PAID".equalsIgnoreCase(status)) {
            this.paymentDate = "2026-10-05";
            this.transactionId = "TXN" + System.currentTimeMillis() % 10000000;
            this.paymentMode = "UPI Online";
            this.receiptNo = "MH-REC-" + (1000 + new Random().nextInt(9000));
        }
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", id);
        map.put("studentId", studentId != null ? studentId : "");
        map.put("studentName", studentName != null ? studentName : "");
        map.put("studentRoom", studentRoom != null ? studentRoom : "");
        map.put("month", month != null ? month : "");
        map.put("year", year);
        map.put("rentAmount", rentAmount);
        map.put("messAmount", messAmount);
        map.put("electricityAmount", electricityAmount);
        map.put("totalAmount", totalAmount);
        map.put("status", status != null ? status : "PENDING");
        map.put("paymentDate", paymentDate != null ? paymentDate : "");
        map.put("transactionId", transactionId != null ? transactionId : "");
        map.put("paymentMode", paymentMode != null ? paymentMode : "");
        map.put("receiptNo", receiptNo != null ? receiptNo : "");
        map.put("remarks", remarks != null ? remarks : "");
        return map;
    }

    public static FeeRecord fromMap(Map<String, Object> map) {
        FeeRecord f = new FeeRecord();
        if (map.containsKey("id")) f.setId((String) map.get("id"));
        if (map.containsKey("studentId")) f.setStudentId((String) map.get("studentId"));
        if (map.containsKey("studentName")) f.setStudentName((String) map.get("studentName"));
        if (map.containsKey("studentRoom")) f.setStudentRoom((String) map.get("studentRoom"));
        if (map.containsKey("month")) f.setMonth((String) map.get("month"));
        if (map.containsKey("year") && map.get("year") instanceof Number) {
            f.setYear(((Number) map.get("year")).intValue());
        }
        if (map.containsKey("rentAmount") && map.get("rentAmount") instanceof Number) {
            f.setRentAmount(((Number) map.get("rentAmount")).doubleValue());
        }
        if (map.containsKey("messAmount") && map.get("messAmount") instanceof Number) {
            f.setMessAmount(((Number) map.get("messAmount")).doubleValue());
        }
        if (map.containsKey("electricityAmount") && map.get("electricityAmount") instanceof Number) {
            f.setElectricityAmount(((Number) map.get("electricityAmount")).doubleValue());
        }
        if (map.containsKey("totalAmount") && map.get("totalAmount") instanceof Number) {
            f.setTotalAmount(((Number) map.get("totalAmount")).doubleValue());
        }
        if (map.containsKey("status")) f.setStatus((String) map.get("status"));
        if (map.containsKey("paymentDate")) f.setPaymentDate((String) map.get("paymentDate"));
        if (map.containsKey("transactionId")) f.setTransactionId((String) map.get("transactionId"));
        if (map.containsKey("paymentMode")) f.setPaymentMode((String) map.get("paymentMode"));
        if (map.containsKey("receiptNo")) f.setReceiptNo((String) map.get("receiptNo"));
        if (map.containsKey("remarks")) f.setRemarks((String) map.get("remarks"));
        return f;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }
    public String getStudentRoom() { return studentRoom; }
    public void setStudentRoom(String studentRoom) { this.studentRoom = studentRoom; }
    public String getMonth() { return month; }
    public void setMonth(String month) { this.month = month; }
    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }
    public double getRentAmount() { return rentAmount; }
    public void setRentAmount(double rentAmount) { this.rentAmount = rentAmount; }
    public double getMessAmount() { return messAmount; }
    public void setMessAmount(double messAmount) { this.messAmount = messAmount; }
    public double getElectricityAmount() { return electricityAmount; }
    public void setElectricityAmount(double electricityAmount) { this.electricityAmount = electricityAmount; }
    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getPaymentDate() { return paymentDate; }
    public void setPaymentDate(String paymentDate) { this.paymentDate = paymentDate; }
    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }
    public String getPaymentMode() { return paymentMode; }
    public void setPaymentMode(String paymentMode) { this.paymentMode = paymentMode; }
    public String getReceiptNo() { return receiptNo; }
    public void setReceiptNo(String receiptNo) { this.receiptNo = receiptNo; }
    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }
}
