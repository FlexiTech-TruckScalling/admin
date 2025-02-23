package org.flexitech.projects.embedded.truckscale.admin.controller.ftp_image;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.flexitech.projects.embedded.truckscale.common.CommonValidators;
import org.flexitech.projects.embedded.truckscale.common.SystemSettingConstants;
import org.flexitech.projects.embedded.truckscale.services.setting.SystemSettingService;
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

	@SuppressWarnings("deprecation")
	@GetMapping("/ftp-photo")
	public ResponseEntity<InputStreamResource> getFtpImage(@RequestParam(required = false) String url) {
		logger.debug("Request received for FTP image: {}", url);

		if (!CommonValidators.validString(url)) {
			logger.warn("Invalid path requested: {}", url);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}

		FTPClient ftpClient = new FTPClient();
		try {
			// Get FTP configuration
			final String ftpHost = systemSettingService.getSettingByCode(SystemSettingConstants.FTP_HOST).getValue();
			final String ftpUsername = systemSettingService.getSettingByCode(SystemSettingConstants.FTP_USER)
					.getValue();
			final String ftpPassword = systemSettingService.getSettingByCode(SystemSettingConstants.FTP_PASSWORD)
					.getValue();
			final String ftpFolder = systemSettingService.getSettingByCode(SystemSettingConstants.FTP_FOLDER_PATH)
					.getValue();

			// Configure FTP client
			ftpClient.setConnectTimeout(5000);
			ftpClient.setDataTimeout(10000);

			logger.debug("Connecting to FTP server: {}", ftpHost);
			ftpClient.connect(ftpHost, 21);
			
			logger.debug("FTP credentials> username: {}, password: {}", ftpUsername, ftpPassword);

			if (!ftpClient.login(ftpUsername, ftpPassword)) {
				logger.error("FTP login failed for user: {}", ftpUsername);
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
			}

			ftpClient.enterLocalPassiveMode();
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

			// Build full file path
			final String filePath = buildFtpPath(ftpFolder, url);
			logger.debug("Attempting to retrieve file: {}", filePath);

			try (InputStream inputStream = ftpClient.retrieveFileStream(filePath)) {
				if (inputStream == null) {
					logger.warn("File not found on FTP server: {}", filePath);
					return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
				}

				// Verify successful transfer
				if (!ftpClient.completePendingCommand()) {
					logger.error("FTP transfer failed for file: {}", filePath);
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
				}

				return ResponseEntity.ok().cacheControl(createCacheControl()).contentType(determineMediaType(url))
						.body(new InputStreamResource(inputStream));
			}
		} catch (IOException e) {
			logger.error("FTP operation failed: {}", ExceptionUtils.getRootCauseMessage(e));
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		} finally {
			quietlyDisconnectFtp(ftpClient);
		}
	}

	private MediaType determineMediaType(String path) {
		String extension = path.substring(path.lastIndexOf('.') + 1).toLowerCase();
		switch (extension) {
		case "jpg":
			return MediaType.IMAGE_JPEG;
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

	private void quietlyDisconnectFtp(FTPClient ftpClient) {
		try {
			if (ftpClient.isConnected()) {
				ftpClient.logout();
				ftpClient.disconnect();
			}
		} catch (IOException e) {
			logger.warn("Error disconnecting from FTP: {}", ExceptionUtils.getRootCauseMessage(e));
		}
	}
}