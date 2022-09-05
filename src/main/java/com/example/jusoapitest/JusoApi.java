package com.example.jusoapitest;


import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;


public class JusoApi {

    public static void main(String[] args) {

        // 파싱한 데이터를 저장할 변수
        String result = "";
        String confmKey = "devU01TX0FVVEgyMDIyMDkwMjE2MTYzOTExMjk0NDM=";

        try {
            // 인증키 (개인이 받아와야함)

            String keyword = URLEncoder.encode("도곡로 218", "UTF-8");


            URL url = new URL("https://business.juso.go.kr/addrlink/addrLinkApi.do?currentPage=1&countPerPage=10&keyword="
                    + keyword + "&confmKey=devU01TX0FVVEgyMDIyMDkwMjE2MTYzOTExMjk0NDM=&resultType=json");

            BufferedReader bf;


            bf = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));

            result = bf.readLine();

            System.out.println(result);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

