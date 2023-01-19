package com.team9.bucket_list.utils.map.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

// 유저에게 입력받은 주소를 가지고 구글 api를 통해 위/경도 등등 데이터를 받은것 중에 필요한 데이터(주소, 위/경도)만 추출하는 DTO
@Getter
@Setter
@AllArgsConstructor
public class MapResponse {
    private String location;
    private double lat;
    private double lng;
}
