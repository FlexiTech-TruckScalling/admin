package org.flexitech.projects.embedded.truckscale.util.ftp;

import java.io.IOException;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

public class FTPClientHelper {

    private final String host;
    private final int port;
    private final String username;
    private final String password;

    private FTPClient ftpClient;

    public FTPClientHelper(String host, int port, String username, String password) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
    }

    /**
     * Connects and logs into the FTP server.
     */
    public FTPClient connect() throws IOException {
        ftpClient = new FTPClient();
        ftpClient.connect(host, port);

        int reply = ftpClient.getReplyCode();
        if (!FTPReply.isPositiveCompletion(reply)) {
            ftpClient.disconnect();
            throw new IOException("FTP server refused connection.");
        }

        boolean loggedIn = ftpClient.login(username, password);
        if (!loggedIn) {
            ftpClient.logout();
            ftpClient.disconnect();
            throw new IOException("FTP login failed.");
        }

        ftpClient.enterLocalPassiveMode();
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
        return ftpClient;
    }

    /**
     * Disconnects from the FTP server safely.
     */
    public void disconnect() {
        if (ftpClient != null && ftpClient.isConnected()) {
            try {
                ftpClient.logout();
                ftpClient.disconnect();
            } catch (IOException ignored) {
            }
        }
    }

    public FTPClient getFtpClient() {
        return ftpClient;
    }
}
