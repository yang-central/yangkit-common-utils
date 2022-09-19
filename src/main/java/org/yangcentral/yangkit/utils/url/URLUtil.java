package org.yangcentral.yangkit.utils.url;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class URLUtil {

    public static String URLGet(URL url,Proxy proxy,Authenticator authenticator,String token,
                                int connectionTimeout, int readTimeout) throws IOException {
        URLConnection urlConnection;
        if(proxy != null){
            urlConnection = url.openConnection(proxy);
        } else {
            urlConnection = url.openConnection();
        }
        if(null != authenticator){
            Authenticator.setDefault(authenticator);
        }
        if(token != null){
            urlConnection.setRequestProperty("Authorization","Token " + token);
        }


        urlConnection.setConnectTimeout(connectionTimeout);
        urlConnection.setReadTimeout(readTimeout);
        if(urlConnection instanceof HttpURLConnection){
            if(urlConnection instanceof HttpsURLConnection){
                TrustManager[] trustManagers = new TrustManager[]{
                        new X509TrustManager() {
                            @Override
                            public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

                            }

                            @Override
                            public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

                            }

                            @Override
                            public X509Certificate[] getAcceptedIssuers() {
                                return new X509Certificate[0];
                            }
                        }
                };
                try {
                    SSLContext context = SSLContext.getInstance("TLS");
                    context.init(null,trustManagers,null);
                    ((HttpsURLConnection) urlConnection).setSSLSocketFactory(context.getSocketFactory());
                } catch (NoSuchAlgorithmException e) {
                    throw new RuntimeException(e);
                } catch (KeyManagementException e) {
                    throw new RuntimeException(e);
                }

            }
            HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;
            httpURLConnection.setRequestMethod("GET");
            if(httpURLConnection.getResponseCode() != 200){
                throw new RuntimeException("GET request:"+url+" failed with error code="+ httpURLConnection.getResponseCode());
            }
        }

        StringBuilder sb = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
        String output;
        while((output = bufferedReader.readLine())!= null){
            sb.append(output);
            sb.append("\n");
        }
        bufferedReader.close();
        urlConnection.getInputStream().close();
        return sb.toString();
    }

    public static String URLGet(URL url) throws IOException {
        return URLGet(url,null,null,null,60000,30000);
    }
    public static String URLGet(URL url,String token) throws IOException {
        return URLGet(url,null,null,token,60000,30000);
    }
    public static String URLGet(URL url,Proxy proxy,Authenticator authenticator) throws IOException {
        return URLGet(url,proxy,authenticator,null,60000,30000);
    }
    public static String URLGet(URL url,Proxy proxy,Authenticator authenticator,String token) throws IOException {
        return URLGet(url,proxy,authenticator,token,60000,30000);
    }
}
