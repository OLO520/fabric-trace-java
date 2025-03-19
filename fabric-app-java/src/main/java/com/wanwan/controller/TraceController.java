package com.wanwan.controller;


import com.wanwan.service.TraceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
public class TraceController {

    @Autowired
    private TraceService traceService;

    @PostMapping("/uplink")
    public ResponseEntity<Map<String, Object>> uplink(HttpServletRequest request) {
        Map<String, Object> response = traceService.uplink(request);
        if (response == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/getFruitInfo")
    public ResponseEntity<Map<String, Object>> getFruitInfo(@RequestParam("traceability_code") String traceabilityCode) {
        Map<String, Object> response = traceService.getFruitInfo(traceabilityCode);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/getFruitList")
    public ResponseEntity<Map<String, Object>> getFruitList(HttpServletRequest request) {
        Map<String, Object> response = traceService.getFruitList(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/getAllFruitInfo")
    public ResponseEntity<Map<String, Object>> getAllFruitInfo() {
        Map<String, Object> response = traceService.getAllFruitInfo();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/getFruitHistory")
    public ResponseEntity<Map<String, Object>> getFruitHistory(@RequestParam("traceabilityCode") String traceabilityCode) {
        Map<String, Object> response = traceService.getFruitHistory(traceabilityCode);
        return ResponseEntity.ok(response);
    }
}
