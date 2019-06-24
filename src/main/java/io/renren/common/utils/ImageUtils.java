package io.renren.common.utils;

import net.coobird.thumbnailator.Thumbnails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigDecimal;

public class ImageUtils {
    private static final Logger logger = LoggerFactory.getLogger(ImageUtils.class);

    /**
     * 根据指定大小和指定精度压缩图片
     *
     * @param srcBytes    源图片二进制
     * @param desFileSize 指定图片大小，单位kb
     * @param accuracy    精度，递归压缩的比率，建议小于0.9
     * @return
     */
    public static byte[] commpressPicForScale(byte[] srcBytes, long desFileSize, double accuracy) {
        if (srcBytes == null || srcBytes.length == 0) {
            return null;
        }
        try {
            int imageSize = srcBytes.length;
            while (imageSize > desFileSize * 1024) {
                srcBytes = commpressPicCycle(srcBytes, accuracy);
                imageSize = srcBytes.length;
            }
        } catch (Exception e) {
            logger.error(e.toString(), e);
        }
        return srcBytes;
    }

    public static InputStream commpressPicForScale(InputStream srcInputStream, long desFileSize, double accuracy) {
        if (srcInputStream == null) {
            return null;
        }
        try {
            int imageSize = srcInputStream.available();
            while (imageSize > desFileSize * 1024) {
                OutputStream outputStream = commpressPicCycle(srcInputStream, accuracy);
                srcInputStream = parse2InputStream(outputStream);
                imageSize = srcInputStream.available();
            }

        } catch (Exception e) {
            logger.error(e.toString(), e);
        }
        return srcInputStream;
    }

    private static byte[] commpressPicCycle(byte[] srcBytes, double accuracy) throws IOException {
        ByteArrayInputStream srcInputStream = new ByteArrayInputStream(srcBytes);
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        // 计算宽高
        BufferedImage bim = ImageIO.read(srcInputStream);
        int srcWdith = bim.getWidth();
        int srcHeigth = bim.getHeight();
        int desWidth = new BigDecimal(srcWdith).multiply(
                new BigDecimal(accuracy)).intValue();
        int desHeight = new BigDecimal(srcHeigth).multiply(
                new BigDecimal(accuracy)).intValue();

        Thumbnails.of(srcInputStream).size(desWidth, desHeight)
                .outputQuality(accuracy).toOutputStream(outStream);
        return outStream.toByteArray();
    }

    private static OutputStream commpressPicCycle(InputStream srcInputStream, double accuracy) throws IOException {
        OutputStream outStream = new ByteArrayOutputStream();
        // 计算宽高
        BufferedImage bim = ImageIO.read(srcInputStream);
        int srcWdith = bim.getWidth();
        int srcHeigth = bim.getHeight();
        int desWidth = new BigDecimal(srcWdith).multiply(
                new BigDecimal(accuracy)).intValue();
        int desHeight = new BigDecimal(srcHeigth).multiply(
                new BigDecimal(accuracy)).intValue();

        Thumbnails.of(srcInputStream).size(desWidth, desHeight)
                .outputQuality(accuracy).toOutputStream(outStream);
        return outStream;
    }


    // outputStream转inputStream
    private static ByteArrayInputStream parse2InputStream(OutputStream out) {
        ByteArrayOutputStream baos = (ByteArrayOutputStream) out;
        return new ByteArrayInputStream(baos.toByteArray());
    }

}
