package com.milano.milanopastas.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
public class SupabaseStorageService {

    private final RestTemplate restTemplate;
    private final String supabaseUrl;
    private final String supabaseKey;

    private static final String BUCKET = "milano-products";

    public SupabaseStorageService(
            @Value("${supabase.url}") String supabaseUrl,
            @Value("${supabase.key}") String supabaseKey
    ) {
        this.supabaseUrl = supabaseUrl;
        this.supabaseKey = supabaseKey;
        this.restTemplate = new RestTemplate();
    }

    public String uploadImage(MultipartFile file, String originalFileName) {
        try {
            // Generar nombre único
            String extension = originalFileName.contains(".")
                    ? originalFileName.substring(originalFileName.lastIndexOf("."))
                    : ".jpg";
            String fileName = "product_" + UUID.randomUUID() + extension;

            // Crear headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(file.getContentType()));
            headers.set("apikey", supabaseKey);
            headers.set("Authorization", "Bearer " + supabaseKey);

            // Cuerpo con bytes del archivo
            HttpEntity<byte[]> requestEntity = new HttpEntity<>(file.getBytes(), headers);

            // Endpoint Supabase Storage
            String uploadUrl = supabaseUrl + "/storage/v1/object/" + BUCKET + "/" + fileName;

            // Enviar PUT
            restTemplate.exchange(uploadUrl, HttpMethod.PUT, requestEntity, String.class);

            // Devolver URL pública
            return supabaseUrl + "/storage/v1/object/public/" + BUCKET + "/" + fileName;

        } catch (Exception e) {
            throw new RuntimeException("Error al subir la imagen: " + e.getMessage(), e);
        }
    }
}
