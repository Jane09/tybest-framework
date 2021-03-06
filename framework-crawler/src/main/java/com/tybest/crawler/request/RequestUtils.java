package com.tybest.crawler.request;

import com.tybest.crawler.config.UserAgent;
import org.apache.http.HttpEntity;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author tb
 * @date 2018/11/27 15:21
 */
public final class RequestUtils {

    private static final int NUM_200 = 200;
    private static final int NUM_300 = 300;
    public static final String ERROR_PREFIX = "err_";

    public static String doGet(String url) {
        HttpGet get = new HttpGet(url);
        try {
            CloseableHttpResponse response = HttpClients.createDefault().execute(get);
            return processResponse(response);
        } catch (IOException e) {
            return ERROR_PREFIX+e.getMessage();
        }
    }

    public static String doPost(String url) {
        HttpPost post = new HttpPost(url);
        try {
            CloseableHttpResponse response = HttpClients.createDefault().execute(post);
            return processResponse(response);
        } catch (IOException e) {
            return ERROR_PREFIX+e.getMessage();
        }
    }


    public static void loadImage(String url, String storagePath) {
        HttpGet get = new HttpGet(url);
        get.setHeader("Referer",url);
        get.setHeader("User-Agent", UserAgent.CHROME_FOR_MAC);
        try {
            CloseableHttpResponse response = HttpClients.createDefault().execute(get);
            IOUtils.copyLarge(response.getEntity().getContent(),new FileOutputStream(new File(storagePath+System.currentTimeMillis()+".jpg")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static String processResponse(CloseableHttpResponse response) throws IOException {
        int status = response.getStatusLine().getStatusCode();
        if (status >= NUM_200 && status < NUM_300) {
            HttpEntity entity = response.getEntity();
            return entity != null ? EntityUtils.toString(entity,"UTF-8") : "";
        }
        return ERROR_PREFIX+response.getStatusLine().getReasonPhrase();
    }
}
