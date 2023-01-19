package com.team9.bucket_list.controller.front.map;


import com.team9.bucket_list.service.map.MapService;
import com.team9.bucket_list.utils.map.dto.MapResponse;
import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/post")
@AllArgsConstructor
@Slf4j
public class MapController {

    private final MapService mapService;

    // 게시글 작성 폼 페이지 이동
    @GetMapping("/CreateForm")
    public String location(){
        return "PostCreate";
    }
    

    //AJAX 데이터 받아오기
    @PostMapping(value="/save", produces = "application/json")
    public String getPostData(@RequestBody Map<String,Object> objectMap
    ) {
        String address = (String) objectMap.get("address");
        // Post에 위치 저장시키는 Service 기능 추가
        mapService.findLoction(address);        // 이거는 여기에 필요 없음
        log.info("ajax return");
        return "forward:/post/map";
    }

    // 게시물 데이터 출력(세부 게시물 데이터 출력)
    @PostMapping("/map")
    public String viewData(Model model) {
        System.out.println("map 페이지");
        double lat = 37.6391417;
        double lng = 127.1137335;
        String location = "경기 구리시 담터길32번길 111";
        model.addAttribute("lat",lat);
        model.addAttribute("lng",lng);
        model.addAttribute("location",location);
        return "map";
    }

}
