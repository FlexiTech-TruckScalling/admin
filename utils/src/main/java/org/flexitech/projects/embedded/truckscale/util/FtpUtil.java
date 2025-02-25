package org.flexitech.projects.embedded.truckscale.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.imageio.ImageIO;

import org.apache.commons.net.ftp.FTPSClient;

public class FtpUtil {
    private final String host;
    private final String username;
    private final String password;

    public FtpUtil(String host, String username, String password) {
        this.host = host;
        this.username = username;
        this.password = password;
    }

    private FTPSClient getFtpClient() throws IOException, NoSuchAlgorithmException, KeyManagementException {
        FTPSClient ftpClient = new FTPSClient();
        ftpClient.setConnectTimeout(5000);
        ftpClient.connect(host);
        ftpClient.login(username, password);
        ftpClient.execPBSZ(0);
        ftpClient.execPROT("P");

		/* ftpClient.enterLocalPassiveMode(); */
        ftpClient.setFileType(FTPSClient.BINARY_FILE_TYPE);
        return ftpClient;
    }

    public boolean uploadFile(String localFilePath, String remoteFilePath) {
        FTPSClient ftpClient = null;
        try (FileInputStream fis = new FileInputStream(localFilePath)) {
            ftpClient = getFtpClient();
            boolean success = ftpClient.storeFile(remoteFilePath, fis);
            return success;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            closeFtpClient(ftpClient);
        }
    }

    public boolean downloadFile(String remoteFilePath, String localFilePath) {
        FTPSClient ftpClient = null;
        try (FileOutputStream fos = new FileOutputStream(localFilePath)) {
            ftpClient = getFtpClient();
            boolean success = ftpClient.retrieveFile(remoteFilePath, fos);
            return success;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            closeFtpClient(ftpClient);
        }
    }

    public boolean deleteFile(String remoteFilePath) {
        FTPSClient ftpClient = null;
        try {
            ftpClient = getFtpClient();
            return ftpClient.deleteFile(remoteFilePath);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            closeFtpClient(ftpClient);
        }
    }

    public String[] listDirectory(String remotePath) {
        FTPSClient ftpClient = null;
        try {
            ftpClient = getFtpClient();
            return ftpClient.listNames(remotePath);
        } catch (Exception e) {
            e.printStackTrace();
            return new String[0];
        } finally {
            closeFtpClient(ftpClient);
        }
    }

    public BufferedImage getImageFromFtp(String remoteFilePath) {
        FTPSClient ftpClient = null;
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ftpClient = getFtpClient();
            if (ftpClient.retrieveFile(remoteFilePath, baos)) {
                byte[] imageBytes = baos.toByteArray();
                try (ByteArrayInputStream bais = new ByteArrayInputStream(imageBytes)) {
                    return ImageIO.read(bais);
                }
            }
            System.err.println("FTP Image Download Failed");
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            closeFtpClient(ftpClient);
        }
    }

    private void closeFtpClient(FTPSClient ftpClient) {
        if (ftpClient != null && ftpClient.isConnected()) {
            try {
                ftpClient.logout();
                ftpClient.disconnect();
            } catch (IOException ignored) {
            }
        }
    }
}

