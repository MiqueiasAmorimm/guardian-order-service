package com.guardian.order_service.infrastructure;

import com.guardian.order_service.domain.exception.CatalogUnavailableException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CatalogClientTest {
    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private CatalogClient catalogClient;

    @Test
    void getProduct_shouldThrowCatalogUnavailable_whenCatalogIdDow() {
        UUID id = UUID.randomUUID();
        when(restTemplate.getForObject(anyString(), eq(ProductInfo.class)))
        .thenThrow(new ResourceAccessException("Connect refused"));

        assertThrows (CatalogUnavailableException.class,
                () -> catalogClient.getProduct(id));
    }
    @Test
    void getProduct_shouldThrowIllegalArgument_whenProductNotFound() {
        UUID id = UUID.randomUUID();
        when(restTemplate.getForObject(anyString(), eq(ProductInfo.class)))
                .thenThrow(HttpClientErrorException.create(
                        HttpStatus.NOT_FOUND, "Not Found",
                        HttpHeaders.EMPTY, new byte[0], null));

        assertThrows(IllegalArgumentException.class,
                () -> catalogClient.getProduct(id));
    }


}
