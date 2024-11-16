# 💻 Git And Run (8조) 프로젝트
: AI 검증 비즈니스 배달 플랫폼
|분류|내용|
|---|---|
|WBS|[LINK](http://hexagonal-taleggio-d78.notion.site/Git-and-Run-1363121c7bfd80f1839ccd358e83756d?pvs=73)|
|ERD|[LINK](https://www.erdcloud.com/d/zwwsd7gtExGqthiYH)|
|API|[LINK](https://www.erdcloud.com/d/zwwsd7gtExGqthiYH](https://hexagonal-taleggio-d78.notion.site/API-1363121c7bfd80e2bce1ee1295028abc))|

## 🤔 프로젝트 개요
가게 관리 및 사용자 중심 서비스를 제공하는 배달 플랫폼입니다. 이 프로젝트는 가게 소유자, 관리자, 고객을 대상으로 각각의 역할에 적합한 기능을 제공하며, 특히 생성형 인공지능 서비스(API)와 연동하여 메뉴 설명을 도와 사용자 친화적인 플랫폼을 목표로 합니다.

### 기획 배경
- 효율적이고 자동화된 온라인 주문 관리 시스템 요구

### 프로젝트 목표
- 추후 확장성을 고려한 유연한 데이터 설계
- 효율적인 가게 관리 시스템 구축
- 사용자 친화적인 메뉴 관리
- 검색을 통한 고객 맞춤형 서비스 제공
- 역할 기반의 권한 제어
- 지역 기반 서비스

### ● 기대 효과
AI를 통해 AI가 생성한 자연스러운 메뉴 설명으로 고객에게 메뉴의 매력을 효과적으로 전달.

## ⏳ 개발 기간
24.11.06 ~ 24.11.18

## 🧑‍🤝‍🧑 멤버 구성
팀장 : 정광호 - 메뉴 도메인, AI 연동

부팀장 : 채수원 - 가게 도메인, 카테고리 도메인, 지역 도메인

팀원 1 : 오세창 - 주문 도메인, 결제 도메인

팀원 2 : 이현민 - 유저 도메인, 인증 / 인가

팀원 3 : 홍유진 - 리뷰 도메인

## 💻 개발 환경
### ● 데이터베이스
PostgreSQL, Amazon RDS, Redis

### ● 백엔드
JAVA, Spring Boot 3.3.5, Spring Data JPA, Spring Security

### ● API
구글 Gemini API, JWT (JSON Web Token)

### ● Tools
Github, Notion, Postman, IntelliJ IDEA, ZEP


## 📚주요 기능

● 회원 관리
- 회원 가입, 조회, 수정, 삭제(Soft-Delete) 기능
- 고객, 소유자(Owner), 관리자(Admin, Manager) 등 역할 기반 접근 제어
- Spring Security와 JWT를 활용하여 보안 인증 및 권한 관리

● 가게 관리
- 가게 생성, 조회, 수정, 삭제(Soft-Delete) 기능
- 역할에 따른 정보 제한:
  -> 관리자는 모든 가게를 조회 및 관리 가능
  -> 소유자는 자신의 가게만 수정 및 삭제 가능
- 카테고리별 검색 및 키워드 검색 기능
- 지역 정보(Region)를 계층적으로 관리
  
● 메뉴 관리
- 가계별 메뉴 생성, 조회, 수정, 삭제(Soft-Delete) 기능
- 메뉴 정보와 가게 정보 간의 연관 관계 관리
- 키워드로 가게명 검색 후 메뉴 반환

● AI
- 메뉴 설명 생성 요청
  -> 입력 : 메뉴 이름, 주요 재료 기입
  -> 출력 : 생성형 AI가 작성한 매력적인 메뉴 설명
- 설명 자동 등록
- 다국어 지원

● 주문 관리

● 리뷰 관리

---
인프라 설계도
---
<img width="698" alt="image" src="https://github.com/user-attachments/assets/514c3044-4ced-4786-a094-ca8c1c6f55c2">
