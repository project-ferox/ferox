package com.tantaman.ferox.server.priv;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.Security;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SslConfig {
	private static final Logger log = LoggerFactory.getLogger(SslConfig.class);
	private static final String PROTOCOL = "TLS";

	private final SSLContext context;

	private static final SslConfig INSTANCE = new SslConfig();
	
	private SslConfig() {
		SSLContext serverContext = null;
		try {
			// Key store (Server side certificate)
			String algorithm = Security.getProperty("ssl.KeyManagerFactory.algorithm");
			if (algorithm == null) {
				algorithm = "SunX509";
			}

			try {
				String keyStoreFilePath = System.getProperty("keystore.file.path");
				String keyStoreFilePassword = System.getProperty("keystore.file.password");

				KeyStore ks = KeyStore.getInstance("JKS");
				FileInputStream fin = new FileInputStream(keyStoreFilePath);
				ks.load(fin, keyStoreFilePassword.toCharArray());

				// Set up key manager factory to use our key store
				// Assume key password is the same as the key store file
				// password
				KeyManagerFactory kmf = KeyManagerFactory.getInstance(algorithm);
				kmf.init(ks, keyStoreFilePassword.toCharArray());

				// Initialise the SSLContext to work with our key managers.
				serverContext = SSLContext.getInstance(PROTOCOL);
				serverContext.init(kmf.getKeyManagers(), null, null);
			} catch (Exception e) {
				throw new Error("Failed to initialize the server-side SSLContext", e);
			}
		} catch (Exception ex) {
			log.warn("Error initializing SslContextManager.", ex);
		} finally {
			context = serverContext;
		}
	}

	public static SSLContext context() {
		return INSTANCE.context;
	}
}
