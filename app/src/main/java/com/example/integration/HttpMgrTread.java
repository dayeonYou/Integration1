package com.example.integration;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpMgrTread extends Thread {
    public HttpMgrTread(){
    }
    public void run(){
        reqHttp();
    }
    public void reqHttp(){
        URL url = null;
        try{
            url = new URL("http://www.naver.com");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.connect();
        }catch (MalformedURLException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
