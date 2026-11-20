package com.marian.hostel.service;

import com.marian.hostel.model.*;
import com.marian.hostel.repository.DataStore;

import java.text.SimpleDateFormat;
import java.util.*;

public class FeeService {
    private final DataStore dataStore = DataStore.getInstance();
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public Map<String, Object> getFees(String userId) {
        Map<String, Object> res = new LinkedHashMap<>();
        User u = dataStore.getUserById(userId);
        if (u == null) {
            res.put("success", false);
            res.put("message", "User not found.");
            return res;
        }

        boolean isWarden = "WARDEN".equalsIgnoreCase(u.getRole());
        List<FeeRecord> records = isWarden ? dataStore.getAllFeeRecords() : dataStore.getFeeRecordsForStudent(userId);

        double totalPaid = 0;
        double totalPending = 0;
        int paidCount = 0;
        int pendingCount = 0;

        List<Map<String, Object>> mapList = new ArrayList<>();
        for (FeeRecord f : records) {
            mapList.add(f.toMap());
            if ("PAID".equalsIgnoreCase(f.getStatus())) {
                totalPaid += f.getTotalAmount();
                paidCount++;
            } else {
                totalPending += f.getTotalAmount();
                pendingCount++;
            }
        }

        res.put("success", true);
        res.put("isWarden", isWarden);
        res.put("feeRecords", mapList);
        res.put("totalPaid", totalPaid);
        res.put("totalPending", totalPending);
        res.put("paidCount", paidCount);
        res.put("pendingCount", pendingCount);
        return res;
    }

    public Map<String, Object> payFee(String feeId, String paymentMode, String userId) {
        Map<String, Object> res = new LinkedHashMap<>();
        FeeRecord f = dataStore.getFeeRecordById(feeId);
        if (f == null) {
            res.put("success", false);
            res.put("message", "Fee record not found.");
            return res;
        }

        f.setStatus("PAID");
        f.setPaymentDate(sdf.format(new Date()));
        f.setPaymentMode(paymentMode != null ? paymentMode : "Online UPI");
        f.setTransactionId("TXN-" + System.currentTimeMillis() % 100000000);
        f.setReceiptNo("MH-REC-" + (1000 + new Random().nextInt(9000)));

        dataStore.saveToFile();

        res.put("success", true);
        res.put("message", "Payment of ₹" + f.getTotalAmount() + " processed successfully! Receipt: " + f.getReceiptNo());
        res.put("feeRecord", f.toMap());
        return res;
    }

    public Map<String, Object> updateFeeRecord(String feeId, Map<String, Object> data, String wardenId) {
        Map<String, Object> res = new LinkedHashMap<>();
        User warden = dataStore.getUserById(wardenId);
        if (warden == null || !"WARDEN".equalsIgnoreCase(warden.getRole())) {
            res.put("success", false);
            res.put("message", "Permission denied. Only wardens can edit fee records.");
            return res;
        }

        FeeRecord f = dataStore.getFeeRecordById(feeId);
        if (f == null) {
            res.put("success", false);
            res.put("message", "Fee record not found.");
            return res;
        }

        if (data.containsKey("status")) f.setStatus((String) data.get("status"));
        if (data.containsKey("remarks")) f.setRemarks((String) data.get("remarks"));
        if (data.containsKey("rentAmount") && data.get("rentAmount") instanceof Number) {
            f.setRentAmount(((Number) data.get("rentAmount")).doubleValue());
        }
        if (data.containsKey("messAmount") && data.get("messAmount") instanceof Number) {
            f.setMessAmount(((Number) data.get("messAmount")).doubleValue());
        }
        if (data.containsKey("electricityAmount") && data.get("electricityAmount") instanceof Number) {
            f.setElectricityAmount(((Number) data.get("electricityAmount")).doubleValue());
        }
        f.setTotalAmount(f.getRentAmount() + f.getMessAmount() + f.getElectricityAmount());

        dataStore.saveToFile();

        res.put("success", true);
        res.put("message", "Fee record updated by Warden.");
        res.put("feeRecord", f.toMap());
        return res;
    }
}
