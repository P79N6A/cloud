package com.springframework.http.configure;

import com.springframework.http.utils.FakeX509TrustManager;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * @author summer
 * 2018/8/8
 */
@Configuration
public class MySSLConnectionSocketFactory {

    @Bean
    public SSLConnectionSocketFactory ssLConnectionSocketFactory() {
        SSLContext context;
        try {
            context = SSLContext.getInstance("TLS");
            TrustManager[] _trustManagers = new TrustManager[]{new FakeX509TrustManager()};
            context.init(null, _trustManagers, new SecureRandom());
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                    context,
                    new String[]{"TLSv1.2", "TLSv1.1", "TLSv1", "SSLv3"},
                    null,
                    SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            return sslsf;
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            throw new IllegalStateException(e.getMessage());
        }

    }
}
