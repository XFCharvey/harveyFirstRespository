package com.cbox.base.utils;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.FileSystems;
import java.nio.file.Path;

import com.cbox.base.utils.uuid.IdUtils;
import com.cbox.base.utils.uuid.UUID;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class QRCodeGenerator {

    private static int width = 350;
    private static int height = 350;

    /**
     * 生成二维码
     * 
     * @param text 二维码内容
     * @param width 宽度
     * @param height 高度
     * @param filePath 文件路径
     * @throws IOException
     * @throws WriterException
     */
    public static void generateQRCodeImage(String text, int width, int height, String filePath) throws IOException, WriterException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);
        Path path = FileSystems.getDefault().getPath(filePath);
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
    }

    /**
     * 生成二维码
     * 
     * @param text 二维码内容
     * @param filePath 文件路径
     * @throws IOException
     * @throws WriterException
     */
    public static void generateQRCodeImage(String text, String filePath) throws IOException, WriterException {
        generateQRCodeImage(text, 350, 350, filePath);
    }

    /**
     * 生成二维码
     * 
     * @param text
     * @param stream
     * @throws IOException
     * @throws WriterException
     */
    public static void generateQRCodeImage(String text, OutputStream stream) throws IOException, WriterException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", stream);
    }

    public static void main(String[] args) {
        try {
            String uuid = IdUtils.simpleUUID();
            generateQRCodeImage(uuid, 350, 350, "f://aaa.png");
        } catch (WriterException e) {
            System.out.println("Could not generate QR Code, WriterException :: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Could not generate QR Code, IOException :: " + e.getMessage());
        }

    }

}