- Jetpack Compose UI
    
    함수형 프로그래밍을 통해, 효율적으로 UI 화면을 구성하고, 재사용 가능성, 유지보수를 증가시켰습니다.
    remember, mutableState를 통해 UI State를 손쉽게 관리하였습니다.
    
- Clean Architecture
    
    data - Network, Local DB(Room), Repository 구현체
    domain - Repository 추상화, Usecase
    presentation - UI, ViewModel, Navigation
    di - Module 선언
  
    앱 구조를 data, domain, presentation 으로 분기하여, 코드를 작성하였습니다.
    모듈화와 계층 분리를 통해, 코드 가독성, 유지보수 성을 증가시켰습니다.
    의존성 역전을 통해, 저수준 모듈을 고수준 모듈에 의존하게 하였고, 추상화 하였습니다.
    이를 통해 모듈 간의 결합도를 낮추고 유연성을 높였습니다.
    
- Dependency Injection
    
    Hilt 를 이용하여, Dagger의 복잡한 설정을 간소화하였습니다.
    - Compile 시간에 검증하여, 런타임 오류를 줄였습니다.
    - 의존성의 생명주기를 자동으로 관리하였습니다.
    
    의존성 주입
    - 클래스 간의 결합도를 낮추어, 코드의 유지 보수성과 확장성을 향상시켰습니다.
    - 구성 요소를 쉽게 재사용하고, 다른 구현으로 쉽게 교체할 수 있도록 하였습니다.

- Jetpack Compose Navigation
    
    Jetpack Compose Navigation 을 통해 앱 화면 간 이동을 관리하였습니다.
    선언적 접근 방식으로 UI와 탐색 로직을 더 직관적이고 간결하게 구성하였습니다.
    NavHost, NavController를 통해 앱 내에서 화면 간의 이동을 쉽게 관리하였습니다.
    유연성과 확장성을 증대하였습니다. ex) Bottom Navigation 과 같은 UI 구성 요소와의 통합을 쉽게 하였습니다.
    
- Paging 3
    
    대용량 데이터를 효율적으로 처리하고, 부드러운 스크롤링을 제공하였습니다. 또한 리소스 사용을 최적화하고, 앱의 성능을 증가시켰습니다.
    - 페이징 데이터를 메모리에 캐싱하여, 효율적으로 데이터를 처리하였습니다.
    - 한꺼번에 많은 데이터를 가져오는 것이 아닌, 화면에 보여지는 데이터를 페이지 단위로 구성하여 가져왔습니다.
    - 요청 중복을 제거하여, 불필요한 네트워크 요청 줄여 네트워크 리소스를 더욱 효율적으로 사용하였습니다.

- Retrofit
- 
    Retrofit을 이용해 REST API 통신으로 포켓몬 리스트, 정보들을 가져왔습니다.
    - @GET, @POST 등의 어노테이션을 통해, 간단히 HTTP 요청하였습니다.
    - JSON 방식으로 데이터를 수신하고 처리하였습니다.
      
- Room, SharedPreference
  
    Room을 통해, 간단한 방식으로 SQLite DB를 앱의 로컬 DB에 저장하였습니다. 전투를 마치고 포켓몬을 잡을 때, Room DB에 저장하였습니다.
    SharedPreference 를 이용하여, 최근 검색 기능을 Key-Value 형식으로 데이터를 간단히 저장하였습니다.
