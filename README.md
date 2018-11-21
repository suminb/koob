# Koob

Koob is a meeting room reservation system. If you read it backward, it's pretty much self-explanatory ;-)

## 내 발에 총 쏘기

꼭 성공시켜야 하는 프로젝트가 있다면 일반적으로 이미 경험했던 기술을 사용하는 것이 좋습니다. 익숙한 기술을 사용해도 실패 (e.g., 일정 지연 등) 리스크를 온전히 배제할 수 없는 상황에서 새로운 기술을 사용하는 것은 아주 큰 모험이기 때문입니다. 그럼에도 불구하고 꼭 새로운 기술을 도입해보고 싶다면 딱 하나의 부분에서만 새로운 기술을 도입하는 것이 좋습니다. 그래야 혹시라도 일이 계획대로 진행되지 않았을 때 실패의 원인을 신기술로 돌릴 수 있기 때문입니다.[^1] 과학 실험을 할 때 통제변인(controlled variables)을 하나씩만 바꾸어 보는 이유와 일맥상통합니다.

이러한 상황에서 저는 무모한 도전을 감행하기로 했습니다. 백엔드와 프론트엔드 모두 처음 써보는 기술들을 이용해서 작성하기로 한 것입니다. 백엔드는 [Spring Boot](https://spring.io/projects/spring-boot), 프론트엔드는 [React](https://reactjs.org/)를 이용해서 구성하기로 했습니다. 익숙한 [Python Flask](http://flask.pocoo.org/)와 (프론트엔드 기술에는 익숙하지 않지만, 그나마 조금 더 친숙한) [jQuery](https://jquery.com/), [Vue.js](https://vuejs.org/)를 사용했더라면 훨씬 짧은 기간에 마무리 할 수 있었을 것이라고 생각합니다.

## 고민했던 디자인 결정들

- Recurrence index vs. synthesizing recurring events

## Known Issues

- Validation 을 어느 계층에서 할 것인지 명확하지 않습니다.
- Timezone 지원이 되지 않습니다.
- DAO에서 raw query를 사용하는 것이 좋은 관례가 아닐 수도 있습니다.
- Raw SQL이 아닌 엔티티 클래스로부터 테이블을 자동으로 생성하는 방법이 분명히 있을텐데, 스프링 지식의 한계로 해결하지 못했습니다.
- Travis CI에서 @Autowired data source 문제

- Integration tests

## 이번에 이루지 못한 꿈들

- 무한히 반복되는 이벤트 지원
- OAuth 사용자 인증 기능
- 유닛테스트 픽스처(fixture)를 우아하게 관리하고 싶었는데, JUnit에 익숙하지 않아서 
- AWS Gateway + Lambda 를 이용해서 서버리스(serverless) 백엔드를 만들고 싶었습니다.

## 만약 이 프로젝트를 계속 진행한다면 해보고 싶은 것들

- 데이터베이스 엔티티의 아이디를 발급해주는 별도의 서비스를 만들거나, 
- 회의실 예약 수정, 삭제 기능
- Terraform?


[^1]: 농담입니다.