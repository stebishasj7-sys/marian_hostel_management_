package com.marian.hostel.handler;

import com.marian.hostel.service.AuthService;
import com.marian.hostel.repository.DataStore;
import com.marian.hostel.util.HttpHelper;
import com.marian.hostel.util.JsonUtil;
import com.marian.hostel.model.User;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.*;

public class ApiAuthHandler implements HttpHandler {
    private final AuthService authService = new AuthService();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        if ("OPTIONS".equalsIgnoreCase(method)) {
            HttpHelper.sendCorsOptions(exchange);
            return;
        }

        String path = exchange.getRequestURI().getPath();

        try {
            if (path.endsWith("/register") && "POST".equalsIgnoreCase(method)) {
                String body = HttpHelper.readBody(exchange);
                Map<String, Object> req = JsonUtil.parseObject(body);
                Map<String, Object> res = authService.register(req);
                HttpHelper.sendJsonResponse(exchange, Boolean.TRUE.equals(res.get("success")) ? 200 : 400, res);
            } else if (path.endsWith("/login") && "POST".equalsIgnoreCase(method)) {
                String body = HttpHelper.readBody(exchange);
                Map<String, Object> req = JsonUtil.parseObject(body);
                String email = (String) req.get("email");
                String password = (String) req.get("password");
                String loginMode = (String) req.getOrDefault("role", "STUDENT");
                Map<String, Object> res = authService.login(email, password, loginMode);
                HttpHelper.sendJsonResponse(exchange, Boolean.TRUE.equals(res.get("success")) ? 200 : 401, res);
            } else if (path.endsWith("/logout") && "POST".equalsIgnoreCase(method)) {
                String userId = HttpHelper.getAuthUserId(exchange);
                Map<String, Object> res = authService.logout(userId);
                HttpHelper.sendJsonResponse(exchange, 200, res);
            } else if (path.endsWith("/warden-status") && "GET".equalsIgnoreCase(method)) {
                Map<String, Object> res = authService.getWardenStatus();
                HttpHelper.sendJsonResponse(exchange, 200, res);
            } else if (path.endsWith("/reset-warden-logins") && "POST".equalsIgnoreCase(method)) {
                Map<String, Object> res = authService.resetWardenLogins();
                HttpHelper.sendJsonResponse(exchange, 200, res);
            } else if (path.endsWith("/me") && "GET".equalsIgnoreCase(method)) {
                String userId = HttpHelper.getAuthUserId(exchange);
                Map<String, Object> res = new LinkedHashMap<>();
                if (userId != null) {
                    User u = DataStore.getInstance().getUserById(userId);
                    if (u != null) {
                        res.put("success", true);
                        res.put("user", u.toMap());
                        HttpHelper.sendJsonResponse(exchange, 200, res);
                        return;
                    }
                }
                res.put("success", false);
                res.put("message", "Not authenticated");
                HttpHelper.sendJsonResponse(exchange, 401, res);
            } else {
                Map<String, Object> err = new HashMap<>();
                err.put("error", "Not Found");
                HttpHelper.sendJsonResponse(exchange, 404, err);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> err = new HashMap<>();
            err.put("success", false);
            err.put("message", "Internal server error: " + e.getMessage());
            HttpHelper.sendJsonResponse(exchange, 500, err);
        }
    }
}
