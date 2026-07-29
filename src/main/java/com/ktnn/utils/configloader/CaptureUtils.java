package com.ktnn.utils.configloader;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.monte.media.Format;
import org.monte.media.FormatKeys;
import org.monte.media.Registry;
import org.monte.media.math.Rational;
import org.monte.screenrecorder.ScreenRecorder;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.ktnn.report.ReportConfig.*;
import static org.monte.media.FormatKeys.*;
import static org.monte.media.VideoFormatKeys.*;

/**
 * CaptureHelpers class cung cấp khả năng chụp ảnh hoặc quay video trong lúc chạy test
 * Dùng thư viện Monte Media
 */
@Slf4j
public class CaptureUtils extends ScreenRecorder {
    private static ScreenRecorder screenRecorder;
    String name;

    /**
     * Khởi tạo constructor
     */
    public CaptureUtils(GraphicsConfiguration cfg, Rectangle captureArea, Format fileFormat, Format screenFormat,
                        Format mouseFormat, Format audioFormat, File movieFolder, String name) throws IOException, AWTException {
        super(cfg, captureArea, fileFormat, screenFormat, mouseFormat, audioFormat, movieFolder);
        this.name = name;
    }

    /**
     * Bắt đầu quay video
     *
     * @param fileName : Tên file video
     */
    public static void startRecord(String fileName) {
        File file = new File( EXPORT_VIDEO_PATH + File.separator + fileName + File.separator);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = screenSize.width;
        int height = screenSize.height;

        Rectangle captureSize = new Rectangle(0, 0, width, height);

        GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice()
                .getDefaultConfiguration();
        try {
            screenRecorder = new CaptureUtils(gc, captureSize,
                    new Format(MediaTypeKey, FormatKeys.MediaType.FILE, MimeTypeKey, MIME_AVI),
                    new Format(MediaTypeKey, MediaType.VIDEO, EncodingKey, ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE,
                            CompressorNameKey, ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE, DepthKey, 24, FrameRateKey,
                            Rational.valueOf(15), QualityKey, 1.0f, KeyFrameIntervalKey, 15 * 60),
                    new Format(MediaTypeKey, MediaType.VIDEO, EncodingKey, "black", FrameRateKey, Rational.valueOf(30)),
                    null, file, fileName);

            screenRecorder.start();
        } catch (Exception e) {
            log.error("VException: {}", e.getMessage());
        }
    }

    /**
     * Dừng quay video
     */
    public static void stopRecord() {
        try {
            screenRecorder.stop();
            // Chờ video file được tạo
            log.info("stopRecord: Video file created at: {}", EXPORT_VIDEO_PATH);

        } catch (IOException e) {
            log.error("VException: {}", e.getMessage());
        }
    }

    /**
     * Chụp screenshot
     *
     * @param driver   : Selenium WebDriver
     * @param fileName :
     */
    public static void captureScreenshot(WebDriver driver, String fileName) {
        try {
            File file = new File(EXTENT_SCREENSHOT_PATH);
            if (!file.exists()) {
                file.mkdir();
                log.info("captureScreenshot: Create folder: {}", file);
            }

            TakesScreenshot ts = (TakesScreenshot) driver;
            File source = ts.getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(source, new File(EXTENT_SCREENSHOT_PATH + File.separator + fileName + "_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".png"));
            log.info("captureScreenshot: Screenshot taken current URL: {}", driver.getCurrentUrl());
        } catch (Exception e) {
            log.error("Exception while taking screenshot: {}", e.getMessage());
        }
    }

    /**
     * Tạo media file mới
     *
     * @param fileFormat : Format của file
     * @return Media file
     */
    @Override
    protected File createMovieFile(Format fileFormat) throws IOException {
        if (!movieFolder.exists()) {
            movieFolder.mkdirs();
        }

        if (!movieFolder.isDirectory()) {
            throw new IOException("\"" + movieFolder + "\" is not a directory.");
        }

        return new File(movieFolder,
                name + "-" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + "." + Registry.getInstance().getExtension(fileFormat));
    }

}
