package com.marian.hostel.handler;

import com.marian.hostel.service.ComplaintService;
import com.marian.hostel.util.HttpHelper;
import com.marian.hostel.util.JsonUtil;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.*;

public class ApiComplaintHandler implements HttpHandler {
    private final ComplaintService service = new ComplaintService();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        if ("OPTIONS".equalsIgnoreCase(method)) {
            HttpHelper.sendCorsOptions(exchange);
            return;
        }

        String path = exchange.getRequestURI().getPath();
        String userId = HttpHelper.getAuthUserId(exchange);

        try {
            if (path.endsWith("/create") && "POST".equalsIgnoreCase(method)) {
                String body = HttpHelper.readBody(exchange);
                Map<String, Object> req = JsonUtil.parseObject(body);
                Map<String, Object> res = service.createComplaint(req, userId);
                HttpHelper.sendJsonResponse(exchange, 200, res);
            } else if (path.endsWith("/like") && "POST".equalsIgnoreCase(method)) {
                String body = HttpHelper.readBody(exchange);
                Map<String, Object> req = JsonUtil.parseObject(body);
                String complaintId = (String) req.get("complaintId");
                Map<String, Object> res = service.toggleLike(complaintId, userId);
                HttpHelper.sendJsonResponse(exchange, 200, res);
            } else if (path.endsWith("/comment") && "POST".equalsIgnoreCase(method)) {
                String body = HttpHelper.readBody(exchange);
                Map<String, Object> req = JsonUtil.parseObject(body);
                String complaintId = (String) req.get("complaintId");
                String text = (String) req.get("text");
                Map<String, Object> res = service.addComment(complaintId, text, userId);
                HttpHelper.sendJsonResponse(exchange, 200, res);
            } else if (path.endsWith("/resolve") && "POST".equalsIgnoreCase(method)) {
                String body = HttpHelper.readBody(exchange);
                Map<String, Object> req = JsonUtil.parseObject(body);
                String complaintId = (String) req.get("complaintId");
                String remarks = (String) req.get("remarks");
                Map<String, Object> res = service.resolveComplaint(complaintId, remarks, userId);
                HttpHelper.sendJsonResponse(exchange, 200, res);
            } else if ("GET".equalsIgnoreCase(method)) {
                Map<String, Object> res = service.getAllComplaints();
                HttpHelper.sendJsonResponse(exchange, 200, res);
            } else {
                HttpHelper.sendJsonResponse(exchange, 404, Collections.singletonMap("error", "Not Found"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            HttpHelper.sendJsonResponse(exchange, 500, Collections.singletonMap("message", e.getMessage()));
        }
    }
}
