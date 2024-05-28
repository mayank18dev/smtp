package com.qvc.thirdpartycontentservice.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class CriteoServiceTest {

    @Mock
    private RestTemplate restTemplate;

//    @Autowired
    private CriteoService criteoService;

    @Value("${criteo.api}")
    private String criteoUrl;

    @Value("${criteo.partner_id}")
    private String criteoPartnerId;

    @Value("${criteo.environment}")
    private String criteoEnvironment;

    @Value("${products.min_count}")
    private int minCount;

    @Value("${products.max_count}")
    private int maxCount;



    @BeforeEach
    void setUp() {
        criteoService = new CriteoService(restTemplate);
        criteoService.setDataTransform(new DataTransform());
        criteoService.setCriteoUrl(criteoUrl);
        criteoService.setCriteoEnvironment(criteoEnvironment);
        criteoService.setCriteoPartnerId(criteoPartnerId);
        criteoService.setMaxCount(maxCount);
        criteoService.setMinCount(minCount);
    }

    @Test
    void testGetDataFromCriteo_Success() throws Exception {
        // Arrange
        String expectedResponseText = "{\"status\":\"OK\",\"placements\":[{\"viewHome_desktop-Carousel\":[{\"format\":\"sponsored_products\",\"products\":[{\"ProductName\":\"Affinity 14K 1.50 cttw Asscher-Cut Diamond Ring, Size 7, null\",\"ProductId\":\"j487391-000-354\",\"Image\":\"https://images.qvc.com/is/image/j/91/j487391.001?$uslarge$\",\"Rating\":\"\",\"Price\":\"10628.00\",\"ComparePrice\":\"10628.00\",\"Shipping\":\"\",\"PromoText\":\"\",\"ParentSKU\":\"J487391+000+354\",\"ClientAdvertiserId\":\"1\"}]}]}],\"page-uid\":\"8ba80680-94ef-470c-b0c9-5fda6e06977e\"}";

        String transformedResponseText = "{\"header\":\"sponsored\",\"min_count\":\"4\",\"max_count\":\"20\",\"headerText\":\" \",\"headerUrl\":\" \",\"buttonText\":\" \",\"buttonUrl\":\" \",\"products\":[{\"reviews\":{\"count\":\"0\",\"averageRating\":\"\"},\"shortDescription\":\"Affinity 14K 1.50 cttw Asscher-Cut Diamond Ring, Size 7, null\",\"productNumber\":\"j487391-000-354\",\"imageUrl\":\"https://images.qvc.com/is/image/j/91/j487391.001?$uslarge$\",\"Price\":\"10628.00\"}]}";
        String retailerVisitorId = "visitor123";
        String pageId = "home";
        String eventType = "view";
        String customerId ="customer123";
        String componentType ="carousel";
        Map<String, String> queryParams = Collections.emptyMap();

        // Constructing the expected URL
        String expectedUri = String.format("%s?criteo-partner-id=%s&environment=%s&retailer-visitor-id=%s&customer-id=%s&page-id=%s&event-type=%s",
                criteoUrl,
                "partner123",
                "test",
                retailerVisitorId,
                "customer123",
                pageId,
                eventType);

        System.out.println("Expected URL: " + expectedUri); // Print the constructed URL for debugging

        // Stubbing the RestTemplate's behavior
        when(restTemplate.getForObject(expectedUri, String.class)).thenReturn(expectedResponseText);
        // Act
        JsonNode actualResponse = criteoService.getDataFromCriteo(retailerVisitorId, customerId, pageId, eventType,componentType, queryParams);

        // Assert
        verify(restTemplate, times(1)).getForObject(expectedUri, String.class);
    }



    @Test
    void testGetDataFromCriteo_withWrongCustomerId() throws Exception {
        // Arrange
        String retailerVisitorId = "visitor123";
        String pageId = "home";
        String eventType = "view";
        Map<String, String> queryParams = Collections.emptyMap();
        String customerId ="customer123";
        String componentType = "carousel";

        String expectedUri = String.format("%s?criteo-partner-id=%s&environment=%s&retailer-visitor-id=%s&customer-id=%s&page-id=%s&event-type=%s",
                criteoUrl,
                "partner123",
                "test",
                retailerVisitorId,
                "customer123",
                pageId,
                eventType);

        // Mock the restTemplate
        when(restTemplate.getForObject(expectedUri, String.class)).thenThrow(new RuntimeException("Required request header 'customer-id' for method parameter type String is not present"));

        // Act & Assert
        // Ensure that an exception is thrown
        Exception thrownException = assertThrows(Exception.class, () -> {
            criteoService.getDataFromCriteo(retailerVisitorId, customerId, pageId, eventType, componentType, queryParams);
        });

        // Assert the message of the thrown exception
        assertEquals("Required request header 'customer-id' for method parameter type String is not present", thrownException.getMessage());
    }

    // Add more test cases to cover edge cases and error scenarios
}
