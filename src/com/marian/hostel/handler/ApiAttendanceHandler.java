package com.marian.hostel.handler;

import com.marian.hostel.service.AttendanceService;
import com.marian.hostel.util.HttpHelper;
import com.marian.hostel.util.JsonUtil;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.*;

public class ApiAttendanceHandler implements HttpHandler {
    private final AttendanceService service = new AttendanceService();

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
            if (path.endsWith("/daily") && "GET".equalsIgnoreCase(method)) {
                Map<String, String> q = HttpHelper.parseQueryParams(exchange);
                String date = q.get("date");
                String session = q.get("session");
                Map<String, Object> res = service.getDailyAttendance(date, session);
                HttpHelper.sendJsonResponse(exchange, 200, res);
            } else if (path.endsWith("/mark") && "POST".equalsIgnoreCase(method)) {
                String body = HttpHelper.readBody(exchange);
                Map<String, Object> req = JsonUtil.parseObject(body);
                String studentId = (String) req.get("studentId");
                String status = (String) req.get("status");
                String date = (String) req.get("date");
                String session = (String) req.get("session");
                Map<String, Object> res = service.markSingleAttendance(studentId, status, date, session, userId);
                HttpHelper.sendJsonResponse(exchange, 200, res);
            } else if (path.endsWith("/bulk") && "POST".equalsIgnoreCase(method)) {
                String body = HttpHelper.readBody(exchange);
                Map<String, Object> req = JsonUtil.parseObject(body);
                Map<String, Object> res = service.markBulkAttendance(req, userId);
                HttpHelper.sendJsonResponse(exchange, 200, res);
            } else if (path.endsWith("/summary") && "GET".equalsIgnoreCase(method)) {
                Map<String, String> q = HttpHelper.parseQueryParams(exchange);
                String studentId = q.getOrDefault("studentId", userId);
                Map<String, Object> res = service.getStudentAttendanceSummary(studentId);
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
