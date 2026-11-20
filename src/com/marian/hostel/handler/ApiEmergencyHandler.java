package com.marian.hostel.handler;

import com.marian.hostel.service.EmergencyService;
import com.marian.hostel.util.HttpHelper;
import com.marian.hostel.util.JsonUtil;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.*;

public class ApiEmergencyHandler implements HttpHandler {
    private final EmergencyService service = new EmergencyService();

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
            if (path.endsWith("/save") && "POST".equalsIgnoreCase(method)) {
                String body = HttpHelper.readBody(exchange);
                Map<String, Object> req = JsonUtil.parseObject(body);
                Map<String, Object> res = service.saveContact(req, userId);
                HttpHelper.sendJsonResponse(exchange, 200, res);
            } else if (path.endsWith("/delete") && "POST".equalsIgnoreCase(method)) {
                String body = HttpHelper.readBody(exchange);
                Map<String, Object> req = JsonUtil.parseObject(body);
                String contactId = (String) req.get("contactId");
                Map<String, Object> res = service.deleteContact(contactId, userId);
                HttpHelper.sendJsonResponse(exchange, 200, res);
            } else if ("GET".equalsIgnoreCase(method)) {
                Map<String, Object> res = service.getEmergencyContacts();
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
