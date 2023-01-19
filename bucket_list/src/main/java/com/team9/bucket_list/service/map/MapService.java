package com.team9.bucket_list.service.map;

import com.team9.bucket_list.utils.map.dto.MapResponse;
import com.team9.bucket_list.utils.map.dto.GoogleMapResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;

@Service
public class MapService {

    @Value("${google.map.key}")
    private Object API_KEY;// 실제 서버에서 구동할때는 무조건 환경변수에 숨겨야함 절대 노출되면 안됨!!!

    // 회원가입에서 입력받은 주소의 위/경도 좌표를 구하기 위해 구글 api 사용
    public MapResponse findLoction(String address){
        UriComponents uri = UriComponentsBuilder.newInstance()          // UriComponentsBuilder.newInstance = uri 주소를 직접 조립하여 만든다
                                                                        // https://maps.googleapis.com/maps/api/geocode/json?address="address"&key="API_KEY"와 같음
                                                                        // 위 처럼 한번에 사용하지 않고 조립해서 사용하는 이유는 address나 key값처럼 외부에서 값을 받아올때 쉽게 넣어 조립이 가능하기 때문
                .scheme("https")
                .host("maps.googleapis.com")
                .path("/maps/api/geocode/json")
                .queryParam("key",API_KEY)
                .queryParam("address",address)
                .build();
        System.out.println("MapService url : "+uri.toUriString());


        GoogleMapResponse response = new RestTemplate().getForEntity(uri.toUriString(), GoogleMapResponse.class).getBody();     // 구글 map api에서 반환해주는 json형식을 MapResponse클래스 형식에 맞춰 받아옴
        String location = Arrays.stream(response.getResult()).findFirst().get().getAddress();
        Double lat = Arrays.stream(response.getResult()).findFirst().get().getGeometry().getLocation().getLat();
        Double lng =Arrays.stream(response.getResult()).findFirst().get().getGeometry().getLocation().getLng();

        MapResponse mapResponse = new MapResponse(location,lat,lng);


        System.out.println("MapService lat : "+ mapResponse.getLat());
        System.out.println("MapService lng : "+mapResponse.getLng());
        System.out.println("MapService location : "+mapResponse.getLocation());

        return mapResponse;
    }


}
