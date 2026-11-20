package com.marian.hostel.handler;

import com.marian.hostel.service.OutpassService;
import com.marian.hostel.util.HttpHelper;
import com.marian.hostel.util.JsonUtil;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.*;

public class ApiOutpassHandler implements HttpHandler {
    private final OutpassService service = new OutpassService();

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
            if (path.endsWith("/request") && "POST".equalsIgnoreCase(method)) {
                String body = HttpHelper.readBody(exchange);
                Map<String, Object> req = JsonUtil.parseObject(body);
                Map<String, Object> res = service.requestOutpass(req, userId);
                HttpHelper.sendJsonResponse(exchange, 200, res);
            } else if (path.endsWith("/settings") && "POST".equalsIgnoreCase(method)) {
                String body = HttpHelper.readBody(exchange);
                Map<String, Object> req = JsonUtil.parseObject(body);
                Map<String, Object> res = service.updateSettings(req, userId);
                HttpHelper.sendJsonResponse(exchange, 200, res);
            } else if (path.endsWith("/verify") && "POST".equalsIgnoreCase(method)) {
                String body = HttpHelper.readBody(exchange);
                Map<String, Object> req = JsonUtil.parseObject(body);
                String outpassId = (String) req.get("outpassId");
                String action = (String) req.get("action");
                String remarks = (String) req.get("remarks");
                Map<String, Object> res = service.verifyOutpass(outpassId, action, remarks, userId);
                HttpHelper.sendJsonResponse(exchange, 200, res);
            } else if ("GET".equalsIgnoreCase(method)) {
                Map<String, Object> res = service.getOutpassData(userId);
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
