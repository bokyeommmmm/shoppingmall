# Hanaro Project

## 1. 프로젝트 개요

본 프로젝트는 Spring Boot를 기반으로 한 전자상거래(E-commerce) 백엔드 API 서버입니다. 사용자 관리, 상품 관리, 장바구니, 주문 등 온라인 쇼핑몰의 핵심 기능을 제공하며, Spring Security를 통해 역할 기반의 API 접근 제어를 구현하고 있습니다.

## 2. 주요 기능

- **사용자 관리**
    - 회원가입 및 로그인 (JWT 기반 인증)
    - 관리자의 사용자 정보 조회 및 삭제
- **상품 관리**
    - 상품 검색 및 상세 정보 조회
    - 관리자의 상품 등록, 수정, 삭제
- **장바구니**
    - 장바구니에 상품 추가, 수량 변경, 삭제
    - 사용자의 장바구니 정보 조회
- **주문 관리**
    - 상품 주문 기능
    - 사용자의 주문 내역 조회
    - 관리자의 전체 주문 내역 조회 (사용자, 날짜 기반 필터링)
- **모니터링**
    - Spring Boot Actuator를 이용한 애플리케이션 상태 모니터링
- **배치 작업**
    - 일일 매출 통계 집계

## 3. 사용 기술

- **언어**: Java 21
- **프레임워크**: Spring Boot 3.5.4
- **데이터베이스**: MySQL
- **ORM**: Spring Data JPA, QueryDSL
- **인증/인가**: Spring Security, JWT
- **빌드 도구**: Gradle
- **API 문서화**: SpringDoc (Swagger UI)
- **기타**: Lombok, Thumbnailator

## 4. API 엔드포인트

- **Swagger UI**: `http://localhost:8080/swagger.html`

### 4.1. 사용자 (Users)

- `POST /users/signUp`: 회원가입
- `POST /users/signIn`: 로그인
- `GET /users`: 모든 회원 정보 조회 (ADMIN)
- `GET /users/search`: 회원 검색 (ADMIN)
- `DELETE /users/{id}`: 회원 삭제 (ADMIN)

### 4.2. 상품 (Items)

- `GET /items/search`: 상품 검색
- `GET /items/detail/{id}`: 상품 상세 정보 조회
- `POST /items`: 상품 추가 (ADMIN)
- `PATCH /items/{id}`: 상품 정보 수정 (ADMIN)
- `DELETE /items/{id}`: 상품 삭제 (ADMIN)
- `PATCH /items/{id}/quantity`: 상품 수량 변경 (ADMIN)

### 4.3. 장바구니 (Carts)

- `GET /carts`: 내 장바구니 조회 (USER)
- `POST /carts`: 장바구니에 상품 추가 (USER)
- `PATCH /carts/items/{cartItemId}`: 장바구니 상품 수량 변경 (USER)
- `DELETE /carts/items/{cartItemId}`: 장바구니 상품 삭제 (USER)

### 4.4. 주문 (Orders)

- `GET /orders/me`: 내 주문 내역 조회 (USER)
- `POST /orders/users/{userId}/orders`: 상품 주문 (USER)
- `GET /orders/users/{userId}/orders`: 특정 사용자 주문 내역 조회 (ADMIN)
- `GET /orders/date`: 날짜로 주문 내역 조회 (ADMIN)
- `GET /orders/user`: 사용자로 주문 내역 조회 (ADMIN)

## 5. 데이터베이스 스키마 (주요 엔티티)

- `User`: 사용자 정보
- `Item`: 상품 정보
- `ItemImage`: 상품 이미지 정보
- `Cart`: 장바구니
- `CartItem`: 장바구니에 담긴 상품
- `Orders`: 주문 정보
- `OrderItem`: 주문된 상품
- `SaleItemStat`: 상품별 매출 통계
- `SaleStat`: 날짜별 매출 통계

## 6. 시작하기

1.  **데이터베이스 설정**
    - `src/main/resources/application.properties` 파일에서 `spring.datasource` 관련 정보를 실제 환경에 맞게 수정합니다.

2.  **애플리케이션 실행**
    ```bash
    ./gradlew bootRun
    ```

## 7. Actuator를 이용한 모니터링

Actuator 엔드포인트는 `9001` 포트로 설정되어 있습니다.

- **Health Check**: `http://localhost:9001/actuator/health`
- **Metrics**: `http://localhost:9001/actuator/metrics`
- **Environment**: `http://localhost:9001/actuator/env`
- **Beans**: `http://localhost:9001/actuator/beans`



## 8. 배치 작업

- **매출 통계 배치**
    - 매일 자정 직전에 실행되어 일일 매출 통계를 집계합니다.
    - 결과는 `SaleItemStat` (아이템별 통계) 및 `SaleStat` (날짜별 통계) 테이블에서 확인할 수 있습니다.
