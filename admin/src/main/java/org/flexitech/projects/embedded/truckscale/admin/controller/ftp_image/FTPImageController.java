package org.flexitech.projects.embedded.truckscale.admin.controller.ftp_image;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.flexitech.projects.embedded.truckscale.common.CommonValidators;
import org.flexitech.projects.embedded.truckscale.common.SystemSettingConstants;
import org.flexitech.projects.embedded.truckscale.services.setting.SystemSettingService;
import org.flexitech.projects.embedded.truckscale.util.FtpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FTPImageController {

    private final Logger logger = LogManager.getLogger(getClass());

    @Autowired
    private SystemSettingService systemSettingService;

    @GetMapping("/ftp-photo")
    public ResponseEntity<InputStreamResource> getFtpImage(@RequestParam(required = false) String img) {
        logger.debug("Request received for FTP image: {}", img);

        if (!CommonValidators.validString(img)) {
            logger.warn("Invalid image URL received: {}", img);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        try {
            final String ftpHost = systemSettingService.getSettingByCode(SystemSettingConstants.FTP_HOST).getValue();
            final int ftpPort = 21;
            final String ftpUser = systemSettingService.getSettingByCode(SystemSettingConstants.FTP_USER).getValue();
            final String ftpPassword = systemSettingService.getSettingByCode(SystemSettingConstants.FTP_PASSWORD).getValue();
            final String ftpFolder = systemSettingService.getSettingByCode(SystemSettingConstants.FTP_FOLDER_PATH).getValue();

            String filePath = buildFtpPath(ftpFolder, img);
            logger.debug("Retrieving FTP file: {}", filePath);

            FtpUtil ftpUtil = new FtpUtil(ftpHost, ftpPort, ftpUser, ftpPassword);
            byte[] fileBytes = ftpUtil.getFileBytesFromFtp(filePath);

            if (fileBytes == null || fileBytes.length == 0) {
                logger.warn("File not found or empty: {}", filePath);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            ByteArrayInputStream inputStream = new ByteArrayInputStream(fileBytes);
            String format = determineFormat(img);

            return ResponseEntity.ok()
                    .contentType(determineMediaType(format))
                    .cacheControl(createCacheControl())
                    .body(new InputStreamResource(inputStream));

        } catch (Exception ex) {
            logger.error("Error while retrieving image from FTP: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    private String buildFtpPath(String baseFolder, String relativePath) {
        return baseFolder.replaceAll("/$", "") + "/" + relativePath.replaceAll("^/", "");
    }

    private byte[] imageToBytes(BufferedImage image, String format) throws Exception {
        try (java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream()) {
            boolean success = ImageIO.write(image, format, baos);
            if (!success) {
                throw new Exception("Unsupported image format: " + format);
            }
            return baos.toByteArray();
        }
    }

    private String determineFormat(String path) {
        String ext = path.substring(path.lastIndexOf('.') + 1).toLowerCase();
        return (ext.equals("jpg")) ? "jpeg" : ext; // Normalize JPG
    }

    private MediaType determineMediaType(String format) {
        switch (format.toLowerCase()) {
            case "jpeg":
            case "jpg":
                return MediaType.IMAGE_JPEG;
            case "png":
                return MediaType.IMAGE_PNG;
            case "gif":
                return MediaType.IMAGE_GIF;
            default:
                return MediaType.APPLICATION_OCTET_STREAM;
        }
    }

    private CacheControl createCacheControl() {
        return CacheControl.maxAge(7, TimeUnit.DAYS)
                           .cachePublic()
                           .mustRevalidate();
    }
}
