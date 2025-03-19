package com.wanwan.service;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;

public interface TraceService {
    Map<String, Object> uplink(HttpServletRequest request);
    Map<String, Object> getFruitInfo(String traceabilityCode);
    Map<String, Object> getFruitList(HttpServletRequest request);
    Map<String, Object> getAllFruitInfo();
    Map<String, Object> getFruitHistory(String traceabilityCode);
}
