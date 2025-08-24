package org.flexitech.projects.embedded.truckscale.util;

import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

public class FtpUtil {
    private final String host;
    private final int port;
    private final String username;
    private final String password;

    public FtpUtil(String host, int port, String username, String password) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
    }

    private FTPClient connect() throws IOException {
        FTPClient ftp = new FTPClient();
        ftp.setConnectTimeout(5000);
        
        ftp.connect(host, port);

        ftp.setSoTimeout(5000);
        
        int reply = ftp.getReplyCode();
        if (!FTPReply.isPositiveCompletion(reply)) {
            ftp.disconnect();
            throw new IOException("FTP server refused connection. Reply code: " + reply);
        }

        boolean loggedIn = ftp.login(username, password);
        if (!loggedIn) {
            ftp.logout();
            ftp.disconnect();
            throw new IOException("FTP login failed.");
        }

        ftp.enterLocalPassiveMode();
        ftp.setFileType(FTP.BINARY_FILE_TYPE);
        return ftp;
    }

    public boolean uploadFile(String localFilePath, String remoteFilePath) {
        FTPClient ftp = null;
        try (InputStream input = new FileInputStream(localFilePath)) {
            ftp = connect();
            boolean success = ftp.storeFile(remoteFilePath, input);
            return success;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            disconnect(ftp);
        }
    }

    public boolean downloadFile(String remoteFilePath, String localFilePath) {
        FTPClient ftp = null;
        try (OutputStream output = new FileOutputStream(localFilePath)) {
            ftp = connect();
            boolean success = ftp.retrieveFile(remoteFilePath, output);
			/*
			 * if (success) { ftp.completePendingCommand(); }
			 */
            return success;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            disconnect(ftp);
        }
    }

    public boolean deleteFile(String remoteFilePath) {
        FTPClient ftp = null;
        try {
            ftp = connect();
            return ftp.deleteFile(remoteFilePath);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            disconnect(ftp);
        }
    }

    public String[] listDirectory(String remotePath) {
        FTPClient ftp = null;
        try {
            ftp = connect();
            return ftp.listNames(remotePath);
        } catch (IOException e) {
            e.printStackTrace();
            return new String[0];
        } finally {
            disconnect(ftp);
        }
    }

    public BufferedImage getImageFromFtp(String remoteFilePath) {
        FTPClient ftp = null;
        try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            ftp = connect();
            boolean success = ftp.retrieveFile(remoteFilePath, output);
            if (success) {
//                ftp.completePendingCommand();
                byte[] imageData = output.toByteArray();
                try (InputStream input = new ByteArrayInputStream(imageData)) {
                    return ImageIO.read(input);
                }
            } else {
                System.err.println("FTP Image Download Failed: " + remoteFilePath);
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            disconnect(ftp);
        }
    }

    /**
     * New method added to fetch raw file bytes directly from FTP
     * Useful for serving images directly without decoding/re-encoding
     */
    public byte[] getFileBytesFromFtp(String remoteFilePath) {
        FTPClient ftp = null;
        
        try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            ftp = connect();
            
            boolean success = ftp.retrieveFile(remoteFilePath, output);
            if (success) {
//                ftp.completePendingCommand();
                return output.toByteArray();
            } else {
                System.err.println("FTP file retrieval failed: " + remoteFilePath);
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            disconnect(ftp);
        }
    }

    private void disconnect(FTPClient ftp) {
        if (ftp != null && ftp.isConnected()) {
            try {
                ftp.logout();
                ftp.disconnect();
            } catch (IOException ignored) {
            }
        }
    }
}
