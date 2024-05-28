package com.qvc.thirdpartycontentservice.controller;


import com.fasterxml.jackson.databind.JsonNode;
import com.qvc.thirdpartycontentservice.service.CriteoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@Validated
public class CriteoData {
    @Autowired
    private CriteoService criteoService;

    @GetMapping("/criteo/data")
    public ResponseEntity<JsonNode> getCriteoData(
            @RequestHeader("retailer-visitor-id") String retailerVisitorId,
            @RequestHeader("customer-id") String customerId,
            @RequestParam("component-type") String componentType,
            @RequestParam(value = "page-id") String pageId,
            @RequestParam("event-type") String eventType,
            @RequestParam Map<String, String> queryParams
    ) throws Exception {
        if(componentType.isBlank() || retailerVisitorId.isBlank() || customerId.isBlank()){
            throw new Exception("Atleast one of the mandatory parameters is invalid or empty");
        }
        queryParams.remove("page-id");
        queryParams.remove("event-type");
        JsonNode transformedCriteoResponse = criteoService.getDataFromCriteo(retailerVisitorId, customerId, pageId, eventType, componentType, queryParams);
        return ResponseEntity.ok(transformedCriteoResponse);
    }
}
