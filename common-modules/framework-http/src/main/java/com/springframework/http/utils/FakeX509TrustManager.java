package com.springframework.http.utils;

import javax.net.ssl.SSLEngine;
import javax.net.ssl.X509ExtendedTrustManager;
import java.net.Socket;
import java.security.cert.X509Certificate;

/**
 * @author summer
 */
public class FakeX509TrustManager extends X509ExtendedTrustManager {

    private static final X509Certificate[] _AcceptedIssuers = new X509Certificate[]{};

    @Override
    public void checkClientTrusted(X509Certificate[] arg0, String arg1) {

    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType) {

    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return _AcceptedIssuers;
    }

    @Override
    public void checkClientTrusted(X509Certificate[] arg0, String arg1, Socket arg2) {

    }

    @Override
    public void checkClientTrusted(X509Certificate[] arg0, String arg1, SSLEngine arg2) {

    }

    @Override
    public void checkServerTrusted(X509Certificate[] arg0, String arg1, Socket arg2) {

    }

    @Override
    public void checkServerTrusted(X509Certificate[] arg0, String arg1, SSLEngine arg2) {

    }
}
