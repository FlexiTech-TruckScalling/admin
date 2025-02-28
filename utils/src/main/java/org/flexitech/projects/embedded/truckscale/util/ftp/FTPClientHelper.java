package org.flexitech.projects.embedded.truckscale.util.ftp;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Locale;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSessionContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.net.ftp.FTPSClient;

public class FTPClientHelper extends FTPSClient {

	static {
		System.setProperty("jdk.tls.useExtendedMasterSecret", "false");
		System.setProperty("jdk.tls.client.enableSessionTicketExtension", "false");
	}

	public FTPClientHelper() throws NoSuchAlgorithmException, KeyManagementException {
		super(configureTrustAll());
	}

	private static SSLContext configureTrustAll() throws NoSuchAlgorithmException, KeyManagementException {
		SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
		sslContext.init(null, new TrustManager[] { new X509TrustManager() {
			public void checkClientTrusted(X509Certificate[] chain, String authType) {
			}

			public void checkServerTrusted(X509Certificate[] chain, String authType) {
			}

			public X509Certificate[] getAcceptedIssuers() {
				return new X509Certificate[0];
			}
		} }, new SecureRandom());
		return sslContext;
	}

	@Override
	protected void _connectAction_() throws IOException {
		super._connectAction_();
		execPBSZ(0);
		execPROT("P");
	}

	@Override
	protected void _prepareDataSocket_(Socket socket) throws IOException {
		if (socket instanceof SSLSocket) {
			// Control socket is SSL
			final SSLSession session = ((SSLSocket) _socket_).getSession();
			if (session.isValid()) {
				final SSLSessionContext context = session.getSessionContext();
				try {
					final Field sessionHostPortCache = context.getClass().getDeclaredField("sessionHostPortCache");
					sessionHostPortCache.setAccessible(true);
					final Object cache = sessionHostPortCache.get(context);
					final Method putMethod = cache.getClass().getDeclaredMethod("put", Object.class, Object.class);
					putMethod.setAccessible(true);
					Method getHostMethod;
					try {
						getHostMethod = socket.getClass().getMethod("getPeerHost");
					} catch (NoSuchMethodException e) {
						// Running in IKVM
						getHostMethod = socket.getClass().getDeclaredMethod("getHost");
					}
					getHostMethod.setAccessible(true);
					Object peerHost = getHostMethod.invoke(socket);
					InetAddress iAddr = socket.getInetAddress();
					int port = socket.getPort();
					putMethod.invoke(cache, String.format("%s:%s", peerHost, port).toLowerCase(Locale.ROOT), session);
					putMethod.invoke(cache, String.format("%s:%s", iAddr.getHostName(), port).toLowerCase(Locale.ROOT),
							session);
					putMethod.invoke(cache,
							String.format("%s:%s", iAddr.getHostAddress(), port).toLowerCase(Locale.ROOT), session);
				} catch (Exception e) {
					throw new IOException(e);
				}
			} else {
				throw new IOException("Invalid SSL Session");
			}
		}
	}
}