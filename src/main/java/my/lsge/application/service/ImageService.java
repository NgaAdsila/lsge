package my.lsge.application.service;

import lombok.extern.slf4j.Slf4j;
import my.lsge.application.dto.user.UploadImageRes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Slf4j
@Component
public class ImageService {

    @Value("${image.host.api_upload}")
    private String serverUrl;

    public UploadImageRes upload(String image) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("image", image);

            HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(body, headers);
            ResponseEntity<UploadImageRes> response = restTemplate
                    .postForEntity(serverUrl, request, UploadImageRes.class);
            return response.getBody();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
