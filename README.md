# 프로젝트 개요 및 기여한 점
# 🌱 DailyGrowth

## ⚙️ Tech Stack

**Server**: Java ver.17, SpringBoot, JPA, queryDSL

**Database**: PostgreSQL

## 🧱 Archetecture & Development Environment

`IDE` Intellij

`JDK` >= 17

`OS` mac

## 🔥 해결한 문제
### 1. 아키텍처 설계 사례: Refresh Token 재발급 보안 강화를 위한 사용자 정보기반 인증 아키텍쳐 설계
#### 문제인식 및 진단
- JWT AccessToken 단일 구조  탈취 시 무제한 서버 접근 가능
- 짧은 유효기간으로 피해 감소 가능
- Stateless로 RefreshToken을 구현한다면, 탈취 시 AccessToken 무한 재발급 위험
- 쿠키 저장(HttpOnly, Secure, SameSite=Strict)
  - XSS/CSRF 방어 가능, 그러나 MITM(중간자 공격)으로 쿠키 전체 탈취 위험

#### 판단 및 해결 <br>
- 인증 구조 모식도
  <img width="751" height="618" alt="image" src="https://github.com/user-attachments/assets/839e0a7c-2fc0-407c-b739-101322b9e2e3" />

- Refesh Token 상태 관리
  - Redis에 토큰 + IP + User-Agent 저장 → Stateful 전환
- 재발급 요청 시 검증 절차 강화
  - Redis 저장 정보와 요청 환경 비교 → 불일치 시 재발급 거부 + 기존 토큰 무효화
- 토큰 도난 방지 아키텍처
  - Stateless JWT 한계 보완

#### 성과
✅ 보안 강화: IP/User-Agent 기반 검증으로 비정상 재발급 차단<br>
✅ 탈취 피해 1회로 제한<br>
✅ 안전한 Refresh Token 구조 완성<br>
