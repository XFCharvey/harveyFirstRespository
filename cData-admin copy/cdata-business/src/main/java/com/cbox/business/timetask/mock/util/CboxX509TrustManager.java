package com.cbox.business.timetask.mock.util;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

/**
 * @ClassName: MyX509TrustManager 
 * @Function: 用作绕过https的证书校验  
 * 
 * @author qiuzq 
 * @date 2021年11月9日 上午11:04:55 
 * @version 1.0
 */
public class CboxX509TrustManager implements X509TrustManager {
    /* (non-Javadoc)
    * @see javax.net.ssl.X509TrustManager#checkClientTrusted(java.security.cert.X509Certificate[], java.lang.String)
    */
    public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {

    }

    /* (non-Javadoc)
     * @see javax.net.ssl.X509TrustManager#checkServerTrusted(java.security.cert.X509Certificate[], java.lang.String)
     */
    public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {

    }

    /* (non-Javadoc)
     * @see javax.net.ssl.X509TrustManager#getAcceptedIssuers()
     */
    public X509Certificate[] getAcceptedIssuers() {
        return null;
    }

}
