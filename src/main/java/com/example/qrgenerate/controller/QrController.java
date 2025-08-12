package com.example.qrgenerate.controller;
import com.example.qrgenerate.exception.QrGenerationException;
import com.example.qrgenerate.service.QrService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para la generación de códigos QR.
 */
@RestController
@RequestMapping("/qr")
@Tag(name = "QR Controller", description = "API para generación de códigos QR")
public class QrController {
    private final QrService qrService;

    public QrController(QrService qrService) {
        this.qrService = qrService;
    }

    /**
     * Genera un código QR a partir de un texto proporcionado.
     *
     * @param text El texto a convertir en código QR
     * @return ResponseEntity con la imagen del código QR en formato PNG
     * @throws QrGenerationException Si ocurre un error durante la generación
     */
    @Operation(summary = "Genera un código QR", description = "Convierte el texto proporcionado en una imagen QR")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "QR generado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Parámetros inválidos")
    })
    @GetMapping(produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> generateQr(
            @Parameter(description = "Texto a codificar en QR", required = true)
            @RequestParam String text) throws QrGenerationException {
        QrService.QrResult result = qrService.generateAndMeasure(text);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=qr.png")
                .body(result.qrImage());
    }
}