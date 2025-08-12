package com.example.qrgenerate.controller;
import com.example.qrgenerate.exception.QrGenerationException;
import com.example.qrgenerate.service.QrService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/qr")
public class QrController {
    private final QrService qrService;

    public QrController(QrService qrService) {
        this.qrService = qrService;
    }

    @GetMapping(produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> generateQr(@RequestParam String text) throws QrGenerationException {
        QrService.QrResult result = qrService.generateAndMeasure(text);
        System.out.println("Tiempo de generación: " + result.generationTimeMs() + " ms");
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=qr.png")
                .body(result.qrImage());
    }
}