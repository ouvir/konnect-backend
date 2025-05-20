# Konnect Backend

Konnect 프로젝트의 백엔드 서버 저장소입니다.  
Java + Spring Boot 기반으로 구축되었으며, Gradle을 사용하여 빌드 및 의존성 관리를 수행합니다.  
[프론트 프로젝트 경로](https://github.com/sehee-xx/Konnect)

---

## 🧾 프로젝트 개요

Konnect는 한국 여행 중 촬영한 사진으로 SNS 형태의 사진첩을 만들고, 자신의 여행 기록을 작성하여 공유할 수 있는 웹 플랫폼입니다.
이 저장소는 Konnect의 백엔드 API 서버로, 사용자 인증, 관광지 검색, 게시글 및 사진첩 작성 등 핵심 기능을 제공합니다.

---
## 📁 주요 폴더 구조

```
konnect-backend/
├── src/
│   ├── main/
│   │   ├── java/com/konnect/       # 주요 백엔드 비즈니스 로직 (도메인, 서비스, 컨트롤러 등)
│   │   └── resources/              # 설정 파일 및 정적 자원 (application.yml 등)
│   └── test/                       # 테스트 코드 디렉토리
├── build.gradle                    # Gradle 빌드 설정 파일
├── settings.gradle                 # Gradle 프로젝트 설정 파일
├── README.md                       # 프로젝트 설명 파일
```

- `src/main/java`: 비즈니스 로직, API 컨트롤러, 서비스, 리포지토리 등
- `src/main/resources`: 설정 파일 (application.yml 등)
- `build.gradle`: 빌드 설정
- `settings.gradle`: 프로젝트 설정

---

## ⚙️ 기술 스택

<p align="left">
  <img src="https://img.shields.io/badge/Java-17+-red?logo=java&logoColor=white&style=for-the-badge" />
</p>
<p align="left">
  <img src="https://img.shields.io/badge/Spring Boot-3.x-6DB33F?logo=springboot&logoColor=white&style=for-the-badge" />
  <img src="https://img.shields.io/badge/Spring Security-JWT%20%2B%20OAuth2-6DB33F?logo=springsecurity&logoColor=white&style=for-the-badge" />
</p>
<p align="left">
  <img src="https://img.shields.io/badge/MySQL-8.x-4479A1?logo=mysql&logoColor=white&style=for-the-badge" />
  <img src="https://img.shields.io/badge/JPA-Hibernate-59666C?logo=hibernate&logoColor=white&style=for-the-badge" />
  <img src="https://img.shields.io/badge/QueryDSL-5.x-blue?style=for-the-badge" />
</p>
<p align="left">
  <img src="https://img.shields.io/badge/Swagger-Springdoc%20OpenAPI-85EA2D?logo=swagger&logoColor=black&style=for-the-badge" />
</p>
<p align="left">
  <img src="https://img.shields.io/badge/Gradle-7.x-02303A?logo=gradle&logoColor=white&style=for-the-badge" />
</p>

---

## 🔐 인증 및 보안

- **JWT 기반 인증**: Access Token 이용 구성
- **소셜 로그인**: Google, Naver, Kakao 지원 (OAuth2)
- **쿠키 기반 인증 유지**

---

## 📦 환경 설정

`.env` 와 `application-secret.properties` 파일에서 환경별 정보를 설정합니다.

---

## 🏁 실행 방법
1. 프로젝트 빌드
`./gradlew clean build`
2. 애플리케이션 실행
`./gradlew bootRun`
또는 IDE에서 KonnectApplication을 실행해도 됩니다.

* .env 에 환경변수가 정의되어있어야합니다.

---

## 🧪 API 문서
Swagger UI 경로:
http://localhost:8080/swagger-ui/index.html

---

## 🔍 주요 기능
✅ JWT + OAuth2 기반 로그인 및 회원가입

✅ 관광지 키워드 및 조건 검색 (시도, 구군, 콘텐츠 타입)

✅ 게시글 및 댓글 CRUD

✅ offset 기반 페이지네이션

✅ Swagger 기반 API 문서 자동화

---

## 📌 기타
Swagger 테스트 시 쿠키에 Authorization 토큰이 필요합니다.

운영/개발 환경 분리는 .env 파일 주입으로 설정합니다.

```
# 주입 방법
dos2unix .env
set -a
source .env
set +a 
```

.env 파일 예시
```
SPRING_PROFILES_ACTIVE=dev
# DB
DB_URL=""
DB_USER_NAME=
DB_PASSWORD=
DB_DDL_AUTO=none
# TODO oauth 성공 시, url 등록
# Client URL
CLIENT_URL=""
# JWT Redis
SPRING_DATA_REDIS_HOST=
SPRING_DATA_REDIS_PORT=
SPRING_DATA_REDIS_PASSWORD=
SPRING_DATA_REDIS_TIMEOUT=
# OAUTH2
SECURITY_OAUTH2_CLIENT_REGISTRATION_NAVER_REDIRECT_URI=""
SECURITY_OAUTH2_CLIENT_REGISTRATION_GOOGLE_REDIRECT_URI=""
SECURITY_OAUTH2_CLIENT_REGISTRATION_KAKAO_REDIRECT_URI=""
# Local Storage Path
STORAGE_IMAGE_DIRECTORY_PATH=""
```

production 환경에서는 반드시 HTTPS 설정과 함께 JWT 쿠키 보안을 강화하세요.

---
