package com.gift3dconc3pts.batch.processing.client;

import com.gift3dconc3pts.batch.processing.core.model.dtos.TasBetcDTO;
import com.gift3dconc3pts.batch.processing.core.model.dtos.TasBetcResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
@Slf4j
public class TasBetcClient {

    private final WebClient webClient;

    public TasBetcDTO saveTasBetc(TasBetcDTO tasBetcDTO, Long fileUploadHeaderId){

        URI uri = UriComponentsBuilder.fromHttpUrl("http://localhost:8086/tas-betc-api/tasBetc").build().encode().toUri();
        HttpEntity<?> httpEntity = build(tasBetcDTO, fileUploadHeaderId);

        return webClient.post()
                .uri(uri)
                .headers(httpHeaders -> httpHeaders.addAll(httpEntity.getHeaders()))
                .bodyValue(Objects.requireNonNull(httpEntity.getBody()))
                .retrieve()
                .bodyToMono(TasBetcDTO.class)
                .block();
    }

    public TasBetcResponseDTO saveTasBetcs(List<TasBetcDTO> tasBetcDTOS){

        URI uri = UriComponentsBuilder.fromHttpUrl("http://localhost:8086/tas-betc-api/tasBetc/saveAll").build().encode().toUri();
        HttpEntity<?> httpEntity = build(tasBetcDTOS);

        return webClient.post()
                .uri(uri)
                .headers(httpHeaders -> httpHeaders.addAll(httpEntity.getHeaders()))
                .bodyValue(Objects.requireNonNull(httpEntity.getBody()))
                .retrieve()
                .bodyToMono(TasBetcResponseDTO.class)
                .block();
    }

    private HttpEntity<?> build(TasBetcDTO tasBetcDTO, Long fileUploadHeaderId){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        headers.set("uploadHeaderId", String.valueOf(fileUploadHeaderId));

        return new HttpEntity<>(tasBetcDTO, headers);
    }

    private HttpEntity<?> build(List<TasBetcDTO> tasBetcDTOS){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);

        return new HttpEntity<>(TasBetcResponseDTO.builder().tasBetcDTOS(tasBetcDTOS).build(), headers);
    }
}
