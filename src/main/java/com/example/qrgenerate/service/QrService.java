package com.example.qrgenerate.service;
import com.example.qrgenerate.exception.QrGenerationException;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.util.HashMap;
import java.util.Map;

@Service
public class QrService {
    public byte[] generateQrCode(String text) throws QrGenerationException {
        // Validaciones
        if (text == null || text.trim().isEmpty()) {
            throw new QrGenerationException("El texto no puede ser nulo o vacío.");
        }
        if (text.length() > 200) {
            throw new QrGenerationException("El texto excede el límite de 200 caracteres.");
        }

        try {
            int width = 300;
            int height = 300;

            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height, hints);

            try (ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream()) {
                MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
                return pngOutputStream.toByteArray();
            }
        } catch (WriterException | IOException e) {
            throw new QrGenerationException("Error generando el código QR: " + e.getMessage());
        }
    }

    // Método que calcula el tiempo y devuelve resultado junto con el QR
    public QrResult generateAndMeasure(String text) throws QrGenerationException {
        long start = System.currentTimeMillis();
        byte[] qrBytes = generateQrCode(text);
        long end = System.currentTimeMillis();
        return new QrResult(qrBytes, (end - start));
    }

    // Clase para encapsular QR + tiempo
    public record QrResult(byte[] qrImage, long generationTimeMs) {}
}