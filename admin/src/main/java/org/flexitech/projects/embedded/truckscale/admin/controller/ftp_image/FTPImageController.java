package org.flexitech.projects.embedded.truckscale.admin.controller.ftp_image;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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
	public ResponseEntity<InputStreamResource> getFtpImage(@RequestParam(required = false) String url) {
		logger.debug("Request received for FTP image: {}", url);

		if (!CommonValidators.validString(url)) {
			logger.warn("Invalid path requested: {}", url);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}

		// Get FTP configuration
		final String ftpHost = systemSettingService.getSettingByCode(SystemSettingConstants.FTP_HOST).getValue();
		final String ftpUsername = systemSettingService.getSettingByCode(SystemSettingConstants.FTP_USER).getValue();
		final String ftpPassword = systemSettingService.getSettingByCode(SystemSettingConstants.FTP_PASSWORD)
				.getValue();
		final String ftpFolder = systemSettingService.getSettingByCode(SystemSettingConstants.FTP_FOLDER_PATH)
				.getValue();

		String filePath = buildFtpPath(ftpFolder, url);
		logger.debug("Attempting to retrieve file: {}", filePath);

	
		
		try {
			FtpUtil ftpUtil = new FtpUtil(ftpHost, ftpUsername, ftpPassword);
			
			BufferedImage image = ftpUtil.getImageFromFtp(filePath);
			
			if (image == null) {
				logger.warn("File not found or transfer failed: {}", filePath);
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
			}

			ByteArrayInputStream imageStream = new ByteArrayInputStream(imageToBytes(image, determineFormat(url)));
			return ResponseEntity.ok().cacheControl(createCacheControl()).contentType(determineMediaType(url))
					.body(new InputStreamResource(imageStream));

		} catch (Exception e) {
			logger.error("FTP error: ", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	private byte[] imageToBytes(BufferedImage image, String format) throws Exception {
		try (java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream()) {
			ImageIO.write(image, format, baos);
			return baos.toByteArray();
		}
	}

	private String determineFormat(String path) {
		String extension = path.substring(path.lastIndexOf('.') + 1).toLowerCase();
		return (extension.equals("jpg") || extension.equals("jpeg")) ? "jpeg" : extension;
	}

	private MediaType determineMediaType(String path) {
		String extension = path.substring(path.lastIndexOf('.') + 1).toLowerCase();
		switch (extension) {
		case "jpg":
		case "jpeg":
			return MediaType.IMAGE_JPEG;
		case "png":
			return MediaType.IMAGE_PNG;
		case "gif":
			return MediaType.IMAGE_GIF;
		default:
			return MediaType.APPLICATION_OCTET_STREAM;
		}
	}

	private String buildFtpPath(String baseFolder, String relativePath) {
		return baseFolder.replaceAll("/$", "") + "/" + relativePath.replaceAll("^/", "");
	}

	private CacheControl createCacheControl() {
		return CacheControl.maxAge(7, TimeUnit.DAYS).cachePublic().mustRevalidate();
	}
}
