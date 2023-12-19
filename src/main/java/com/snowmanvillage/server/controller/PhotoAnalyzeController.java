package com.snowmanvillage.server.controller;

import com.snowmanvillage.server.service.GoogleVisionAiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@RequestMapping("")
@RestController
public class PhotoAnalyzeController {

    private final GoogleVisionAiService googleVisionAiService;

    @PostMapping("/photo/analyze")
    public ResponseEntity<Boolean> postPhotoAnalyze(@RequestParam("image") MultipartFile file) throws IOException {
        return ResponseEntity.ok(googleVisionAiService.postPhotoAnalyze(file));
    }

}
