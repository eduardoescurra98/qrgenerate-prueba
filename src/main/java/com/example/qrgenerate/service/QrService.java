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
import java.util.Map;

/**
 * Servicio para la generación de códigos QR.
 */
@Service
public class QrService {

    /**
     * Genera un código QR a partir de un texto.
     *
     * @param text El texto a convertir en código QR
     * @return Array de bytes representando la imagen del código QR
     * @throws QrGenerationException Si ocurre un error durante la generación
     */
    public byte[] generateQrCode(String text) throws QrGenerationException {
        validateInput(text);
        return generateQrImage(text);
    }

    /**
     * Genera un código QR y mide el tiempo de generación.
     *
     * @param text El texto a convertir en código QR
     * @return QrResult conteniendo la imagen y el tiempo de generación
     * @throws QrGenerationException Si ocurre un error durante la generación
     */
    public QrResult generateAndMeasure(String text) throws QrGenerationException {
        long start = System.currentTimeMillis();
        byte[] qrBytes = generateQrCode(text);
        long end = System.currentTimeMillis();
        return new QrResult(qrBytes, (end - start));
    }

    private void validateInput(String text) throws QrGenerationException {
        if (text == null || text.trim().isEmpty()) {
            throw new QrGenerationException("El texto no puede ser nulo o vacío.");
        }
        if (text.length() > 200) {
            throw new QrGenerationException("El texto excede el límite de 200 caracteres.");
        }
    }

    private byte[] generateQrImage(String text) throws QrGenerationException {
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, 300, 300,
                    Map.of(EncodeHintType.CHARACTER_SET, "UTF-8"));

            try (ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream()) {
                MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
                return pngOutputStream.toByteArray();
            }
        } catch (WriterException | IOException e) {
            throw new QrGenerationException("Error generando el código QR: " + e.getMessage());
        }
    }

    /**
     * Record que encapsula el resultado de la generación del QR.
     *
     * @param qrImage Imagen del código QR en bytes
     * @param generationTimeMs Tiempo de generación en milisegundos
     */
    public record QrResult(byte[] qrImage, long generationTimeMs) {}
}