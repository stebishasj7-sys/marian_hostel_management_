package com.marian.hostel.handler;

import com.marian.hostel.service.RoomService;
import com.marian.hostel.util.HttpHelper;
import com.marian.hostel.util.JsonUtil;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.*;

public class ApiRoomHandler implements HttpHandler {
    private final RoomService roomService = new RoomService();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        if ("OPTIONS".equalsIgnoreCase(method)) {
            HttpHelper.sendCorsOptions(exchange);
            return;
        }

        String path = exchange.getRequestURI().getPath();

        try {
            if (path.endsWith("/grid") && "GET".equalsIgnoreCase(method)) {
                Map<String, Object> res = roomService.getRoomGrid();
                HttpHelper.sendJsonResponse(exchange, 200, res);
            } else if (path.endsWith("/allocate") && "POST".equalsIgnoreCase(method)) {
                String body = HttpHelper.readBody(exchange);
                Map<String, Object> req = JsonUtil.parseObject(body);
                String roomNo = (String) req.get("roomNo");
                String bedId = (String) req.get("bedId");
                String studentId = (String) req.get("studentId");
                Map<String, Object> res = roomService.allocateBed(roomNo, bedId, studentId);
                HttpHelper.sendJsonResponse(exchange, 200, res);
            } else if (path.endsWith("/vacate") && "POST".equalsIgnoreCase(method)) {
                String body = HttpHelper.readBody(exchange);
                Map<String, Object> req = JsonUtil.parseObject(body);
                String roomNo = (String) req.get("roomNo");
                String bedId = (String) req.get("bedId");
                Map<String, Object> res = roomService.vacateBed(roomNo, bedId);
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
