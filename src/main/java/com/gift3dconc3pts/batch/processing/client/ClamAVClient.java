package com.gift3dconc3pts.batch.processing.client;

import com.gift3dconc3pts.batch.processing.core.model.dtos.ClamAVResponseDTO;
import com.gift3dconc3pts.batch.processing.core.services.StorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;

@Component
@RequiredArgsConstructor
@Slf4j
public class ClamAVClient {

    private final StorageService storageService;
    private final WebClient webClient;

    public ResponseEntity<ClamAVResponseDTO> scanFile(final String fileName) throws IOException {

        Resource resource = storageService.loadAsResource(fileName);

        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("file", resource.getFile());

//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
//        MultiValueMap<String, Object> body
//                = new LinkedMultiValueMap<>();
//        body.add("file", resource.getFile());
//        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(body, headers);
        URI uri = UriComponentsBuilder.fromHttpUrl("http://localhost:8087/gifted-virus-scanner/scan").build().encode().toUri();

        return webClient.post()
                .uri(uri)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(builder.build()))
                .retrieve()
                .bodyToMono(ResponseEntity.class)
                .block();
    }
}
