# 📋 23-1 세종대학교 캡스톤디자인

## 1~2주차
### 팀 빌딩 및 주제 선정
* **주제 : 시설 작물 병해 검출 및 분류 플랫폼**    
* 팀원 및 역할
  * 오종석 : UI/UX 설계 및 Android 개발
  * 김민정 : Server 구축 및 ERD 설계
  * 현준희, 임채진 : 인공지능 모델 개발 및 학습
### 주제 상세 설명   
* 인공지능 모델을 활용한 전문가의 개입 없이 농작물의 병해충을 판별할 수 있는 서비스 개발
* 농작물의 병해 진단에서 나아가 플랫폼으로서의 추가적인 기능 구현
* 'AI Hub'의 시설 작물 질병 진단 이미지 데이터 활용, 작물은 딸기, 상추, 고추, 토마토 선정   
<img src="https://user-images.githubusercontent.com/98886487/227699209-9b3a2d70-d4d7-4d48-b6f8-00a54f96113b.png" width="500" height="300" /> 

## 3~4주차
### 제안서 작성 및 기획안 발표   
* [문서] 파일 첨부 - 과제 제안서, 발표자료
### 주요 기능 정의
#### 작물의 병해 검출 기능   
* - [x] 진단 전 접근 권한 확인, 카메라/갤러리에서 이미지 첨부(Crop 필요) 및 작물 종류 선택
* - [x] 진단 결과(%) PieChart 구성 및 한 줄 요약 제공
* - [ ] 진단 결과 저장 및 게시판 공유 기능
* - [ ] 진단 결과를 바탕으로 추가 Open API 활용

#### 사용자 커뮤니티 기능
* - [x] 게시판 탭 질문 게시판, 노하우 게시판 분류
* - [ ] 게시글 목록 확인, 게시글 작성, 수정, 삭제 기능
* - [ ] 게시글별 댓글 작성
* - [ ] 게시글 추천/비추천 기능

#### 국가농작물병해충관리시스템 API 활용 (https://ncpms.rda.go.kr/npms/OpenApiInfo.np)
* 상세 기능 미정

#### ~3주차 주요 구현화면
<img src="https://user-images.githubusercontent.com/98886487/230277439-3366642f-f2b7-4bae-a2fe-5408d2e599be.png" width="700" height="350" /> 

## 5~6주차
#### 유스케이스(Use Case) 작성
* [문서] 파일 첨부 - 유스케이스 명세서
#### 주요 기능 구체화
