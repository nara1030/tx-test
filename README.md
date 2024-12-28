### 이슈
회사에서 작업하던 중 서비스단에 `@Transactional`을 붙였는데도 롤백이 제대로 되지 않는 이슈가 있었다. 다른 사람들이 작성한 코드를 확인했는데, 해결책을 못 찾아서 집에서 좀 더 찾아본 내용을 기록한다. 기초적인 내용 같은데 뒤늦게야 공부하는 감이 있지만..

### 해결
현재 회사 코드는 다음과 같은 구조로 작성되었다.

* Controller → Facade → Service → Repository

Facade와 Service는 모두 `@Service`가 붙는 서비스단이지만, Facade에서는 전체적인 서비스 흐름을 이해하기 쉽기 위해 직접적인 Repository 호출을 하지 않고 Service를 호출한다.

이번에 문제가 된 코드의 경우 Facade의 메소드에서 Service의 메소드 2개를 호출한다. 하나는 상태 테이블 갱신, 하는 이력 테이블 입력이다. 이때 이력 테이블 입력시 PK 중복 에러가 발생했는데, 기존 상태 테이블 갱신되었던 부분이 롤백되지 않는다는 이슈를 전달받았다.

Facade 메소드 위와 Service 클래스 위에 `@Transactional`를 붙였음에도 롤백이 안 되어 조금 확인해보니 Checked Exception의 경우는 롤백을 트리거하지 않는다고 한다. 콘솔을 확인해보니 발생하는 예외는 `java.sql.SQLIntegrityConstraintViolationException`였는데 `org.springframework.dao.DuplicateKeyException`에 감싸져 뱉어지고 있었고 이는 Checked Exception이었다. 해서 Facade와 Service에 `@Transactional(rollbackFor = DuplicateKeyException.class)` 이렇게 속성을 추가했으나 동작하지 않았다.  해서 차선으로 예외가 발생하는 Service의 이력 등록 메소드에 try-catch문으로 `DuplicateKeyException` 예외 발생 시 `RuntimeException`을 호출해주도록 변경했다. 그리고 롤백이 제대로 되는지 테스트해보기 위해 테스트 메소드 `testDbWithNormalBehavior()`로 1차 테스트 후 혹시 몰라 API 테스트(`ControllerExample.testWithDb()`)도 진행해보았다.

사실 해결은 했으나 정확한 이해는 하지 못했다. 왜 블로그에 나온 것처럼 `@Transactional(rollbackFor = DuplicateKeyException.class)`로는 해결이 되지 않는지.. 그리고 회사 다른 분들은 `RuntimeException` 발생 안 시키신 거 같은데, 제대로 동작하고 있는건지... 다시 한 번 확인해볼 필요가 있겠다.

