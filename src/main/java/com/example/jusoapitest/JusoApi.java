package com.example.jusoapitest;


import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;

@Slf4j
public class JusoApi {

    public static void main(String[] args) {

        // 파싱한 데이터를 저장할 변수
        String result = "";

        try {

            String fullJuso = "서울특별시 구로구 구로동 97-6 다인빌딩 406호";


            // 주소 검색, URLEncoder는 URL을 인코딩 하기위해 사용하는 클래스
            String keyword = URLEncoder.encode(fullJuso, "UTF-8");


            URL url = new URL("https://business.juso.go.kr/addrlink/addrLinkApi.do?currentPage=1&countPerPage=10&keyword="
                    + keyword + "&confmKey=devU01TX0FVVEgyMDIyMDkwMjE2MTYzOTExMjk0NDM=&resultType=json");

            BufferedReader bf;


            bf = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));

            result = bf.readLine();

//            System.out.println(result);

            // String 값을 JSON 형태로 추출하기 위해 사용하는 라이브러리
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject)jsonParser.parse(result);
            JSONObject addResult = (JSONObject)jsonObject.get("results");

            // 리스트 추출
            JSONArray jusoArray = (JSONArray)addResult.get("juso");

            // 컬렉션 추출 주소정보 뽑을 준비 완료!
            JSONObject jusoColl = (JSONObject) jusoArray.get(0);

            // 행정동 코드
            String h_code = jusoColl.get("admCd").toString();

            // 지번 특수문자, 띄어쓰기 빼고 6자리로 만들기
            String addName = jusoColl.get("jibunAddr").toString();
            String match = "[^\uAC00-\uD7A30-9a-zA-Z\\s]";
            addName = addName.replaceAll(match, "");                 // 특수문자 띄어쓰기 없애기
            String intAdd1 = addName.replaceAll("[^0-9]", ""); // 지번만 추출
            int intAdd2 = Integer.parseInt(intAdd1);                            // int 로 형변환

            String jibunNm = String.format("%06d",intAdd2);                       // 최종 지번 숫자만


            log.info("n차 통합코드: " + h_code + jibunNm);




        } catch (Exception e) {
            log.error("잘못된 접근입니다",e);
        }
        log.info("프로그램이 정상적으로 종료되었습니다.");
    }
}

