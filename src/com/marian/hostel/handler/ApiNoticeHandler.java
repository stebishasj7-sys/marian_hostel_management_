package com.marian.hostel.handler;

import com.marian.hostel.service.NoticeService;
import com.marian.hostel.util.HttpHelper;
import com.marian.hostel.util.JsonUtil;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.*;

public class ApiNoticeHandler implements HttpHandler {
    private final NoticeService noticeService = new NoticeService();

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
            if ("GET".equalsIgnoreCase(method)) {
                Map<String, Object> res = noticeService.getAllNotices();
                HttpHelper.sendJsonResponse(exchange, 200, res);
            } else if (path.endsWith("/create") && "POST".equalsIgnoreCase(method)) {
                String body = HttpHelper.readBody(exchange);
                Map<String, Object> req = JsonUtil.parseObject(body);
                Map<String, Object> res = noticeService.createNotice(req, userId);
                HttpHelper.sendJsonResponse(exchange, Boolean.TRUE.equals(res.get("success")) ? 200 : 403, res);
            } else if (path.endsWith("/delete") && ("POST".equalsIgnoreCase(method) || "DELETE".equalsIgnoreCase(method))) {
                String body = HttpHelper.readBody(exchange);
                Map<String, Object> req = JsonUtil.parseObject(body);
                String noticeId = (String) req.get("noticeId");
                Map<String, Object> res = noticeService.deleteNotice(noticeId, userId);
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
