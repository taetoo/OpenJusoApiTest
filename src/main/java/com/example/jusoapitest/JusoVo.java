package com.example.jusoapitest;

import lombok.Data;

import java.util.HashMap;
import java.util.List;


@Data
public class JusoVo {
    private HashMap<String, Object> meta;
    private List<Documents> documents;
}
@Data
class Documents {
    private HashMap<String, Object> results;
    private String roadAddr;
    private String jibunAddr;
    private String admCd;
}
