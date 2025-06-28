import com.example.logistics.service.CacheService;
import com.example.logistics.service.impl.MapServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MapServiceImplTest {

    @Mock
    private RestTemplate restTemplate;
    
    @Mock
    private CacheService cacheService;
    
    @InjectMocks
    private MapServiceImpl mapService;

    @BeforeEach
    void setup() {
        ReflectionTestUtils.setField(mapService, "amapKey", "testKey");
        ReflectionTestUtils.setField(mapService, "baseUrl", "http://test");
    }

    @Test
    void getDrivingRoute_usesCacheWhenAvailable() {
        Map<String, Object> cached = Map.of("status", "1");
        when(cacheService.getRouteCache("orig:dest")).thenReturn(cached);

        Map<String, Object> result = mapService.getDrivingRoute("orig", "dest");

        assertEquals(cached, result);
        verify(restTemplate, never()).getForObject(anyString(), eq(Map.class));
        verify(cacheService, never()).saveRouteCache(anyString(), any());
        InOrder inOrder = inOrder(cacheService);
        inOrder.verify(cacheService).getRouteCache("orig:dest");
    }

    @Test
    void getDrivingRoute_savesCacheOnSuccess() {
        when(cacheService.getRouteCache("orig:dest")).thenReturn(null);
        Map<String, Object> response = new HashMap<>();
        response.put("status", "1");
        when(restTemplate.getForObject(anyString(), eq(Map.class))).thenReturn(response);

        Map<String, Object> result = mapService.getDrivingRoute("orig", "dest");

        assertEquals(response, result);
        InOrder inOrder = inOrder(cacheService, restTemplate);
        inOrder.verify(cacheService).getRouteCache("orig:dest");
        inOrder.verify(restTemplate).getForObject(anyString(), eq(Map.class));
        inOrder.verify(cacheService).saveRouteCache("orig:dest", response);
    }

    @Test
    void getGeocode_usesCacheWhenAvailable() {
        Map<String, Object> cached = Map.of("status", "1");
        when(cacheService.getGeocodeCache("addr")).thenReturn(cached);

        Map<String, Object> result = mapService.getGeocode("addr");

        assertEquals(cached, result);
        verify(restTemplate, never()).getForObject(anyString(), eq(Map.class));
        verify(cacheService, never()).saveGeocodeCache(anyString(), any());
        InOrder inOrder = inOrder(cacheService);
        inOrder.verify(cacheService).getGeocodeCache("addr");
    }

    @Test
    void getGeocode_savesCacheOnSuccess() {
        when(cacheService.getGeocodeCache("addr")).thenReturn(null);
        Map<String, Object> response = new HashMap<>();
        response.put("status", "1");
        when(restTemplate.getForObject(anyString(), eq(Map.class))).thenReturn(response);

        Map<String, Object> result = mapService.getGeocode("addr");

        assertEquals(response, result);
        InOrder inOrder = inOrder(cacheService, restTemplate);
        inOrder.verify(cacheService).getGeocodeCache("addr");
        inOrder.verify(restTemplate).getForObject(anyString(), eq(Map.class));
        inOrder.verify(cacheService).saveGeocodeCache("addr", response);
    }
}
