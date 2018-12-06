[![Build Status](https://travis-ci.org/suminb/koob.svg?branch=develop)](https://travis-ci.org/suminb/koob)

# Koob

Koob is a meeting room reservation system. If you read it backward, it's pretty much self-explanatory ;-)

## 내 발에 총 쏘기

꼭 성공시켜야 하는 프로젝트가 있다면 일반적으로 이미 경험했던 기술을 사용하는 것이 좋습니다. 익숙한 기술을 사용해도 실패 (e.g., 일정 지연 등) 리스크를 온전히 배제할 수 없는 상황에서 새로운 기술을 사용하는 것은 아주 큰 모험이기 때문입니다. 그럼에도 불구하고 꼭 새로운 기술을 도입해보고 싶다면 딱 하나의 부분에서만 새로운 기술을 도입하는 것이 좋습니다. 그래야 혹시라도 일이 계획대로 진행되지 않았을 때 실패의 원인을 신기술로 돌릴 수 있기 때문입니다. (농담입니다.) 과학 실험을 할 때 통제변인(controlled variables)을 하나씩만 바꾸어 보는 이유와 일맥상통합니다.

이러한 상황에서 저는 무모한 도전을 감행하기로 했습니다. 백엔드와 프론트엔드 모두 처음 써보는 기술들을 이용해서 작성하기로 한 것입니다. 백엔드는 [Spring Boot](https://spring.io/projects/spring-boot), 프론트엔드는 [React](https://reactjs.org/)를 이용해서 구성하기로 했습니다. 자바는 2010년 이후로 8년만에 처음 사용하는 것이라 무언가 익숙하면서도 익숙하지 않았습니다.

호기롭게 시작한 도전이었지만, 방대한 스프링의 세계를 이해하는 일은 단기간에 이뤄내기 생각보다 훨씬 어려운 일이었습니다. `CrudRepository`에서 내부적으로 SQL 구문을 생성할 때 파라미터가 어떻게 바인드 되는지, `@Autowired`, `@Service`, `@Configuration`, `@ComponentScan`과 같은 annotation들이 어떻게 작동되고 내부적으로 어떤 부수효과를 가지는지 제대로 이해하지 못한 채로 코딩을 하다보니 익숙한 도구를 이용해서 작업할 때와 비교하여 생산성이 현저히 낮아지는 것을 경험할 수 있었습니다. 그리고 로컬 환경에서는 어떻게든 돌아가도록 만들었는데, CI에서 통합 테스트 코드 일부에 문제가 생기는 등 부수적인 환경 구축에서 생긴 문제를 해결하는 것이 너무나도 힘들었습니다.

데드라인까지 하루 남은 시점, 지금과 같은 추세라면 요구사항을 모두 구현하지 못 할 가능성이 높다고 판단하여 지금까지 작성한 서버 코드를 모두 폐기하고 파이썬으로 새로 작성하기로 결정하였습니다. 다만, 자바+스프링으로 작성한 코드를 열람할 수 있도록 Git 태그([`spring`](https://github.com/suminb/koob/releases/tag/spring))를 달아놓았습니다.

익숙한 [Python Flask](http://flask.pocoo.org/)와 (프론트엔드 기술에는 익숙하지 않지만, 그나마 조금 더 친숙한) [jQuery](https://jquery.com/), [Vue.js](https://vuejs.org/)를 사용했더라면 훨씬 짧은 기간에 마무리 할 수 있었을 것이라고 생각합니다.

프론트엔드는 테스트 코드를 작성하지 못했고 코드 퀄리티도 원하는 수준으로 끌어올리지 못했지만, 다행히도 겉으로 보기에 기초적인 기능들은 대부분 작동하는 수준에서 마무리 할 수 있었습니다.

## 간편 설치 & 실행

    docker-compose up

혹은, 다음과 같이 수동으로 두 개의 컨테이너를 구동할 수도 있습니다.

    docker run -p 8080:8080 sumin/koob:latest
    docker run -p 3000:3000 sumin/koob-frontend:latest

위와 같이 필요한 컨테이너를 띄우고 `localhost:3000` 으로 접속하면 회의실 예약 시스템을 사용할 수 있습니다. 혹시 직접 코드를 실행시키고 싶다면 아래 섹션을 참고하십시오.

## 직접 설치 & 실행

### Backend

    git clone https://github.com/suminb/koob.git && cd koob

    pip install -r requirements.txt
    pip install -e .

    koob create-db  # 데이터베이스를 생성하고 샘플 데이터를 넣기 위해 최초 한 번만 실행

테스트 코드를 실행하려면 다음의 명령어로 관련 패키지를 설치해주어야 합니다.

    pip install -r tests/requirements.txt

웹 서버는 다음과 같이 실행할 수 있고,

    koob run

테스트 코드는 다음의 명령어로 실행할 수 있습니다.

    pytest -v tests

기본값으로는 `dev.db` 파일을 만들고 (SQLite) 데이터를 저장하도록 되어있지만, 다음의 환경변수를 오버라이드 해서 데이터가 저장될 곳을 변경할 수 있습니다. 예를 들어서, 파일 대신 메모리에 저장하기 위해서 다음과 같이 지정할 수 있습니다.

    export KOOB_DB_URL="sqlite://:memory:"

포트 번호는 `KOOB_PORT` 환경변수로 지정할 수 있습니다. 기본값은 `8080`입니다.

    export KOOB_PORT=8080

### Frontend

    npm install
    npm start

## 고민했던 디자인 결정들

### SQLite

SQLite를 사용하기로 한건 가장 후회하는 결정 중에 하나입니다. 이렇게까지 기능이 없을 것이라고는 생각하지 못했습니다.

요구사항 중에 "가능하면 in-memory DB 사용" 이라는 조건이 있어서 큰 고민 없이 SQLite를 택했습니다. DB URL만 바꿔주면 파일에 저장할 수도 있고, 메모리에 저장할 수도 있습니다.

다음번에 비슷한 결정을 내려야 할 일이 있다면 SQLite 대신 H2를 사용해보고 싶습니다. SQLite에 비해서는 훨씬 풍부한 기능들을 제공하는 것으로 [보여집니다](http://www.h2database.com/html/functions.html). 다만, `SQLAlchemy` 라이브러리에서 H2를 공식적으로 지원하지 않기 때문에 직접 어댑터를 만들거나 다른 ORM을 사용해야 할 것 같습니다.

### Synthesizing Recurring Events vs. Recurrence Index

반복되는 이벤트를 표현하는 다양한 방법이 있겠지만, 크게 두 가지 후보를 고려했습니다.

1. 등록 시점에 반복 주기와 회수만큼 중복된 이벤트 생성하기
2. 반복되는 이벤트를 표현하는 자료 구조를 만들기

첫 번째 방법은 구현하기는 쉽지만, 무한하게 반복되는 이벤트를 표현할 수 없다는 점과 일괄 수정과 삭제가 어렵다는 문제가 있습니다. 두 번째 방법은 (첫 번째 방법과 비교하여) 구현이 조금 더 까다로울 수도 있지만, 정규화가 잘 되어있고 일괄 수정, 삭제에 유연하게 대응할 수 있다는 장점이 있습니다. 그래서 두 번째 방법으로 구현하기로 결정했습니다. (`models.py`의 `Recurrence` 클래스 참고)

### Scalability

규모확장성(scalability) 관점에서 봤을 때 몇가지 예상되는 병목 지점들이 있습니다.

#### 엔티티 ID 발급

지금은 정수형 필드에 `AUTO INCREMENT` 옵션을 걸어놔서 순차적으로 증가하는 아이디를 발급하도록 만들어놓았습니다. 하지만 이런 방식은 멀티 마스터 데이터베이스 구조에 적합하지 않습니다. 두 개 이상의 노드가 동시에 하나의 카운터를 증가시킬 때 정합성을 보장하는 것은 매우 어려운 일이기 때문입니다. 물론 [이러한 문제를 해결하기 위한 자료구조](https://en.wikipedia.org/wiki/Conflict-free_replicated_data_type#G-Counter_(Grow-only_Counter))가 있긴 하지만, RDBMS의 `AUTO INCREMENT`가 그런식으로 구현되어있을 것 같지는 않습니다.

이러한 문제를 해결하기 위해서는 유일함이 보장되는 아이디를 발급하는 별도의 서비스를 두거나, 타임스탬프와 머신 아이디를 이용해서 그러한 별도의 서비스 없이 유일한 아이디를 발급할 수 있도록 해야 합니다. ([UUID2](https://en.wikipedia.org/wiki/Universally_unique_identifier#Version_2_(date-time_and_MAC_address,_DCE_security_version))와 같은 방식)

#### Primary Keys

현재는 각 엔티티가 정수형 필드 하나를 PK로 가지고 있는 형태인데, 아쉽게도(?) API 디자인 상 예약(`Reservation`) 아이디를 가지고 조회를 하는 경우는 없습니다. 다만, 자주 사용되는 형태로 멀티 컬럼 인덱스들이 빌드 되어있기는 합니다.

- 특정 시간 범위에 겹치는 예약 찾기
- 특정 시간 범위에 반복되는 예약 찾기
- 특정 날짜(weekday)에 반복되는 예약 찾기

만약 이 중에서 월등하게 많이 사용되는 패턴이 있다면 그것을 secondary index가 아닌 primary key로 만들어서 성능 향상을 도모할 수 있습니다. (물론 그렇게 해서 실제로 디스크 엑세스 회수를 줄일 수 있는지 연구해봐야 합니다.)

#### Secondary Indexes

Secondary index 존재 유무는 무시할 수 없는 수준의 성능 차이를 가져오지만, 이것도 어느정도 규모 이상이 되면 곧 한계에 부딪치고 맙니다. 시중에 나와있는 분산 데이터베이스들 중에서는 월등한 key-value 엑세스 성능을 보여주는 것들도 있기 때문에 만약 우리의 쿼리를 key-value 엑세스만을 이용해서 표현할 수 있다면 규모확장성이 보장되는 서비스를 만들 수 있을 것이라고 기대됩니다.

### Frontend

- 달력 UI를 직접 만들다보니 너무 못생기게 나와서 [오픈소스 라이브러리](https://github.com/intljusticemission/react-big-calendar)를 가져다 쓰기로 결정했습니다.
- https://semantic-ui.com

## Known Issues

- 타임존이 지원 되지 않습니다. 로컬 타임존에 의존하기 때문에 서버 시간과 클라이언트 시간이 다르면 매우 해괴한 문제들이 생길 수 있습니다. GMT+9에서만 테스트 했습니다. 시간이 조금 더 있다면 이 문제부터 고치고 싶습니다.
- SQLite의 한계로 인해 반복되는 이벤트 날짜 계산을 데이터베이스에서 하지 못하고 애플리케이션 코드에서 수행합니다. (`models.py` 코드 내 주석 참고)
- 프론트엔드에서의 서버 URL 하드코딩
- 일정이 자정을 넘어가는 경우 다음날 슬롯에 일정이 표시되지 않고 당일 자정에서 끝나는 문제 (시간은 제대로 표시됨). 이건 `react-big-calendar`의 버그이고, 이미 제보 되어있지 않다면 이슈 티켓을 하나 만드는 것이 좋을 것 같습니다.

## 이번에 이루지 못한 꿈들

요구사항은 아니지만, 신경써서 만들고 싶었던 부분들:

- 예약을 수정하거나 취소할 수 있는 UI가 제공되지 않음
- 무한히 반복되는 이벤트 지원
    - 지금은 예약이 몇번 반복되는지 유한한 값으로 명시하도록 API가 디자인 되어있지만, 내부에서는 특정 시간 구간(time window)에 예약이 있는지 질의하는 방식으로 작동하기 때문에 이론적으로는 무한히 반복되는 예약도 표현 가능합니다.
- 스트레스 테스트
- OAuth 사용자 인증 기능
- 여러 기관(organizations) 지원; 각각의 기관은 다른 회의실 목록을 보유
- 국제화
- AWS API Gateway + Lambda 를 이용해서 서버리스(serverless) 백엔드를 만들기
- 시맨틱 버전 관리, 도커 태그 정책 정하기
- 프론트엔드 저장소 분리

## 앞으로의 계획

- 스프링으로 구현하는걸 재도전 해보고 싶습니다.
- 소규모 그룹에서 (가족, 친구, 동아리 등) 사용하는 공유 자원 관리 도구로 발전시켜보아도 좋을 것 같습니다.
    - 가족 구성원들이 한 대의 차를 공유하는 경우 "언제부터 언제까지 누가 차를 사용하겠다"는 약속을 명시
    - 그 '공유 자원'이 사람이라면 친구들끼리 약속을 잡을 때 "너 언제 시간 돼?"에 대한 답을 제공
