package org.flexitech.projects.embedded.truckscale.util.ftp;

import org.apache.commons.net.ftp.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

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
        FTPClient ftpClient = new FTPClient();
        ftpClient.connect(host, port);

        int reply = ftpClient.getReplyCode();
        if (!FTPReply.isPositiveCompletion(reply)) {
            ftpClient.disconnect();
            throw new IOException("FTP server refused connection.");
        }

        if (!ftpClient.login(username, password)) {
            ftpClient.logout();
            ftpClient.disconnect();
            throw new IOException("FTP login failed.");
        }

        ftpClient.enterLocalPassiveMode();
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
        return ftpClient;
    }

    public boolean uploadFile(String localFilePath, String remoteFilePath) {
        FTPClient ftpClient = null;
        InputStream inputStream = null;

        try {
            ftpClient = connect();
            inputStream = new FileInputStream(localFilePath);
            return ftpClient.storeFile(remoteFilePath, inputStream);
        } catch (IOException e) {
            System.err.println("FTP Upload Error: " + e.getMessage());
            return false;
        } finally {
            try {
                if (inputStream != null) inputStream.close();
                if (ftpClient != null && ftpClient.isConnected()) {
                    ftpClient.logout();
                    ftpClient.disconnect();
                }
            } catch (IOException ignored) {}
        }
    }

    public boolean downloadFile(String remoteFilePath, String localFilePath) {
        FTPClient ftpClient = null;
        OutputStream outputStream = null;

        try {
            ftpClient = connect();
            outputStream = new FileOutputStream(localFilePath);
            return ftpClient.retrieveFile(remoteFilePath, outputStream);
        } catch (IOException e) {
            System.err.println("FTP Download Error: " + e.getMessage());
            return false;
        } finally {
            try {
                if (outputStream != null) outputStream.close();
                if (ftpClient != null && ftpClient.isConnected()) {
                    ftpClient.logout();
                    ftpClient.disconnect();
                }
            } catch (IOException ignored) {}
        }
    }

    public InputStream getFileStream(String remoteFilePath) throws IOException {
        FTPClient ftpClient = connect();
        InputStream inputStream = ftpClient.retrieveFileStream(remoteFilePath);

        if (inputStream == null) {
            ftpClient.logout();
            ftpClient.disconnect();
            throw new IOException("Failed to retrieve file: " + remoteFilePath);
        }

        // Wrap input stream to ensure command completion + disconnect on close
        return new FilterInputStream(inputStream) {
            @Override
            public void close() throws IOException {
                super.close();
                ftpClient.completePendingCommand();
                ftpClient.logout();
                ftpClient.disconnect();
            }
        };
    }

    public boolean deleteFile(String remoteFilePath) {
        FTPClient ftpClient = null;
        try {
            ftpClient = connect();
            return ftpClient.deleteFile(remoteFilePath);
        } catch (IOException e) {
            System.err.println("FTP Delete Error: " + e.getMessage());
            return false;
        } finally {
            try {
                if (ftpClient != null && ftpClient.isConnected()) {
                    ftpClient.logout();
                    ftpClient.disconnect();
                }
            } catch (IOException ignored) {}
        }
    }

    public List<String> listFiles(String remoteDir) {
        FTPClient ftpClient = null;
        List<String> fileList = new ArrayList<>();
        try {
            ftpClient = connect();
            FTPFile[] files = ftpClient.listFiles(remoteDir);
            for (FTPFile file : files) {
                fileList.add(file.getName());
            }
        } catch (IOException e) {
            System.err.println("FTP List Error: " + e.getMessage());
        } finally {
            try {
                if (ftpClient != null && ftpClient.isConnected()) {
                    ftpClient.logout();
                    ftpClient.disconnect();
                }
            } catch (IOException ignored) {}
        }
        return fileList;
    }
}