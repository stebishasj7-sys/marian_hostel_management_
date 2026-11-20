package com.marian.hostel.handler;

import com.marian.hostel.service.ProfileService;
import com.marian.hostel.util.HttpHelper;
import com.marian.hostel.util.JsonUtil;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.*;

public class ApiProfileHandler implements HttpHandler {
    private final ProfileService profileService = new ProfileService();

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
            if (path.endsWith("/update") && "POST".equalsIgnoreCase(method)) {
                String body = HttpHelper.readBody(exchange);
                Map<String, Object> req = JsonUtil.parseObject(body);
                String targetId = (String) req.getOrDefault("userId", userId);
                Map<String, Object> res = profileService.updateProfile(targetId, req);
                HttpHelper.sendJsonResponse(exchange, 200, res);
            } else if (path.endsWith("/photo") && "POST".equalsIgnoreCase(method)) {
                String body = HttpHelper.readBody(exchange);
                Map<String, Object> req = JsonUtil.parseObject(body);
                String photoUrl = (String) req.get("photoUrl");
                String targetId = (String) req.getOrDefault("userId", userId);
                Map<String, Object> res = profileService.updatePhoto(targetId, photoUrl);
                HttpHelper.sendJsonResponse(exchange, 200, res);
            } else if ("GET".equalsIgnoreCase(method)) {
                Map<String, String> q = HttpHelper.parseQueryParams(exchange);
                String targetId = q.getOrDefault("userId", userId);
                Map<String, Object> res = profileService.getProfile(targetId);
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
