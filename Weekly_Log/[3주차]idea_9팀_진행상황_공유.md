

## 👩‍💻 팀 내부 회의 진행 회차 및 일자

---

| 회차 | 날짜 | 시간 | 불참 팀원 명 |
| --- | --- | --- | --- |
| 9 | 1/30 | 14:00 - 18:00 | - |
| 10 | 1/31 | 09:00 - 09:30 | - |
| 11 | 2/01 | 09:00 - 09:30 | - |
| 12 | 2/02 | 필요할 때 마다 | - |

## 📈 개인별 역할과 현재까지 개발 과정 요약 + 소감

---

### 🙍‍♂️ 고관운

- SNS 로그인 유저 JWT 저장 (accessToken : localStorage, refreshToken : DB)
- 버킷리스트 카테고리별 filter 처리
- 좋아요, 마이피드 기능 및 UI 구현
- 버킷리스트 세부/리스트 UI 구현
- 소감
    - 프론트와 API 통신하는 것에 익숙해졌고, 웹 페이지가 점점 완성도가 높아지는 것 같아서 좋았다.
    - 깃을 활용한 협업이 처음에는 힘들었지만, 현재는 깃 충돌이 나도 혼자 해결할 수 있을 정도로 성장했다.

### 🙍‍♂️ 박은빈

- 채팅 비동기 저장후 업데이트된 리스트 전송
- 채팅 예외처리 구현중
- 채팅 UI 구현중
- 소감
    - 풀리지  않을 것 같았던 채팅 에러가 해결되어서 아주 좋았다 맥이 뚫리는 기분
    - 이제 속도를 내서 작업할 수 있을거 같다

### 🙍‍♂️ 배지원

- 포스트 작성 페이지 ↔ 서버 데이터 통신 과정 구현(axios await.get)
- 포스트 세부 페이지 ↔ 서버 데이터 통신 과정 구현(axios await.get/await post)
- 구글맵 api 사용을 위한 구조 변경
- 댓글, 대댓글 페이지 ↔ 서버 데이터 통신 과정 진행중
- 소감
    - 구글 api를 사용하기 위해서는 https을 통해 요청을 해야지만 사용할 수 있다는 사실을 알았다… 서버에 지도가 띄워지지가 않아서 몇일동안 고생을 했었으나 이제는 출력이 되어 신난다
    - 프론트와 백엔드 서버간 데이터 통신을 하는 방식을 원래는 MVC패턴으로만 했었는데 이번 기회에 api통신을 통해 구현해봤는데 처음이라 어려웠지만 JSON 형태로 데이터를 송수신하는 방식을 알 수 있어 좋았다.

### 🙍‍♂️ 변지환

- 마이피드 수정 및 사진첨부 기능 추가
- 마이피드 UI 구현중
- 소감
    - 마이피드 사진을 첨부하는 기능을 넣었다. 그런데 s3라는 곳에 사진을 저장해야 한다는 말을 듣고 무슨 말인지 몰라서 공부했다… 아무래도 많은 코드를 고쳐야 할 거 같다.
    - 프론트 쪽은 정말 처음이라서 당혹스러운데 다른 분들은 척척 잘하는 거 같아서 마음이 급하다. 강의를 들으면서 하고 있어서 속도가 더디지만 꾸준하게 하다 보면 얻어가는 것이 있을 거라고 생각한다.

### 🙍‍♂️ 정재현

- 로그인 버그 수정
- 참가 신청서 기능 구현
- 전체 게시글 목록 화면 구현
- 전체적으로 구현한 화면 조립, 버그 수정
- 소감
    - 미리 구현 해 놓은 api를 이용해서 화면을 통해 결과물들을 볼 수 있어서 좋다
    - 예전에는 비동기를 많이 어려워했었는데 지금은 이해도가 높아진 느낌이다.

### 🙍‍♂️ 최수정

- Swagger 기능 추가
- Post 사진 첨부 에러 처리 중
- Chat UI 구현
- 소감
    - Spring 같은 개념적인 것을 보다가 프로젝트 적용을 위한 코드를 짜다 보니, 구글링 실력이 점점 늘어가고 있으며 복붙을 하더라도 진행 중인 프로젝트에 적용할 수 있게 분석하는 능력이 올라갔다.
    - 채팅 기능을 팀원과 같이 구현하며 몰랐던 개발도구에 대해서도 많이 알게 되고, 학습해 가는 방법을 배울 수 있어서 좋았다.
    - Git Merge 재밌다!
    - JavaScript를 공부하면서 구현하다 보니 새로운 걸 해보는 즐거움이 있어서 좋았다.

## ❓ 개발 과정에서 나왔던 질문

---

### 1️⃣ refresh token을 이용한 로그인 연장을 어떻게 할까

- 로그인 진행 시 서버는 access token, refresh token 을 발급합니다.
- **access token은 client로** 보내는 응답에 포함 시킵니다.
- **refresh token은 DB**에 member의 pk값과 함께 저장합니다.
- 클라이언트는 응답에 포함되어 있는 **access token을 local storage**에 저장합니다.
- **local storage에 저장되어있는 jwt은 앞으로 서버로 보내는 요청에 포함** 시키거나
- 화면에 로그인 되어 있는 유저의 정보를 나타낼 때 사용됩니다.
- **axios의 interceptors 기능**을 사용하여 모든 요청, 응답을 인터셉터에서 가로채 관리합니다.
    - 요청 보낼 때 - access token을 같이 보냅니다.
    - 응답 받을 때 - 만료된 access token이라는 401 error가 있다면 재발급 요청을 서버에 보내고 재발급에 성공하면 전에 실패했던 original 요청을 재발급 받은 토큰과 함께 다시 보냅니다.

### 2️⃣ SpringBoot 3.0.2 에 Swagger Docs가 적용이 안되는 문제 해결

- 이전 개인 프로젝트에서 사용했던 springfox의 Swagger가 아닌 가독성이 더 좋은 springdocs를 적용하고 싶었다.
- SpringBoot의 버전에 따라 사용 방법이 달라져서 에러로 고생했다.
- 해결방법
    1. 의존성 추가
        - 다른 버전은 안되고, 꼭 저 `webmvc-ui:2.0.2` 가 들어가 있는 의존성을 추가한다.

    ```bash
    implementation 'org.springdoc:springdoc-openapi-starter-webmvc-ui:2.0.2'
    ```

    1. Config 추가
        - 우리는 JWT 방식을 사용하기 때문에 아래와 같이 작성했다.
        - 프로젝트에 SpringSecurity를 적용해주고 있으므로 토큰 값을 전역으로 넣어줘 스웨거 기능단위마다 일일이 토큰값을 넣지 않아도 되도록 편의성을 추가했다.

    ```bash
    @Configuration
    public class SwaggerConfig {
    
        @Bean
        public OpenAPI customizeOpenAPI() {
            final String securitySchemeName = "bearerAuth";
    
            return new OpenAPI()
                    .addSecurityItem(new SecurityRequirement()
                            .addList(securitySchemeName))
                    .components(new Components()
                            .addSecuritySchemes(securitySchemeName, new SecurityScheme()
                                    .name(securitySchemeName)
                                    .type(SecurityScheme.Type.HTTP)
                                    .scheme("bearer")
                                    .bearerFormat("JWT")));
        }
    }
    ```


## 개발 결과물 공유

---

**코드 리포지토리**

[S2uJeong / BucketList · GitLab](https://gitlab.com/S2uJeong1/bucketlist)

**회의 사진**

<img src="./3차 위클리.png">

