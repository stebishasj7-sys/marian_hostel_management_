package com.marian.hostel.handler;

import com.marian.hostel.service.FeeService;
import com.marian.hostel.util.HttpHelper;
import com.marian.hostel.util.JsonUtil;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.*;

public class ApiFeeHandler implements HttpHandler {
    private final FeeService feeService = new FeeService();

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
            if (path.endsWith("/pay") && "POST".equalsIgnoreCase(method)) {
                String body = HttpHelper.readBody(exchange);
                Map<String, Object> req = JsonUtil.parseObject(body);
                String feeId = (String) req.get("feeId");
                String paymentMode = (String) req.get("paymentMode");
                Map<String, Object> res = feeService.payFee(feeId, paymentMode, userId);
                HttpHelper.sendJsonResponse(exchange, 200, res);
            } else if (path.endsWith("/update") && "POST".equalsIgnoreCase(method)) {
                String body = HttpHelper.readBody(exchange);
                Map<String, Object> req = JsonUtil.parseObject(body);
                String feeId = (String) req.get("feeId");
                Map<String, Object> res = feeService.updateFeeRecord(feeId, req, userId);
                HttpHelper.sendJsonResponse(exchange, 200, res);
            } else if ("GET".equalsIgnoreCase(method)) {
                Map<String, Object> res = feeService.getFees(userId);
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
