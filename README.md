# ☃️Snowman Village

## 📖 목차
- [프로젝트 소개](#-프로젝트-소개)
- [개발 기간](#-개발-기간)
- [개발 환경](#-개발-환경-백엔드)
- [팀 소개](#-팀-소개)
- [컨벤션](#-컨벤션)

## 📣 프로젝트 소개
<b>내가 만든 눈사람을 찍어 공유하고 자랑할 수 있는 플랫폼</b>

💡 2023 동국대학교 해커톤 동국톤 1th 7팀 

자랑하기 👉 [☃️snowman village](www.snowmanvillage.site)
<br/>
Instagram 👉 [☃️@snowman_village](https://www.instagram.com/snowman_village/)
<br/>
<br/>
Frontend Repository : [💻snowman village - Frontend](https://github.com/donggukthonN/donggukthon_2023_7_F)

## 🕰️ 개발 기간
- 2023.12.18 ~ 2023.12.20

## 🛠️ 개발 환경 (백엔드)
- Java 17
- Spring Boot 3.2.0
- Spring JPA
- MySQL
- AWS - EC2, S3, Code Deploy, ALB, Route 53
- Open API
    - Google Cloud Vision API
    - Google GeoCoding API

### 시스템 구성도
<img width="800" alt="donggukthon_7_architecture_design" src="https://github.com/donggukthonN/donggukthon_2023_7_B/assets/91234288/febbb6b4-0121-4a38-b63a-309f722459d6">

## 🧑‍💻 팀 소개

| 이름                                    | 학번   | 역할    |
|---------------------------------------| ------ |-------|
| [박준혁](https://github.com/kilito0118)  | 2023112374 | 기획    |
| [이도윤](https://github.com/Tapirus03)   | 2022111944 | 디자인   |
| [박세호](https://github.com/sayyyho)     | 2019112127 | 프론트엔드 |
| [이수진](https://github.com/sujinee01)   | 2020111864 | 프론트엔드 |
| [김민정](https://github.com/minjeong073) | 2019111791 | 백엔드   |
| [김세훈](https://github.com/khoon9)      | 2019112551 | 백엔드   |


## ✏️ 컨벤션

### Commit Message Convention

|    Type     | Description                                       |
| :---------: | ------------------------------------------------- |
|   `Feat:`   | 새로운 기능 추가                                  |
|   `Fix:`    | 버그 수정                                         |
|   `Docs:`   | 문서 수정                                         |
|  `Style:`   | 코드 포맷팅, 세미콜론 누락, 코드 변경이 없는 경우 |
| `Refactor:` | 코드 리팩토링                                     |
|   `Test:`   | 테스트 코드 및 리펙토링 테스트 코드 추가          |
|  `Chore:`   | 기타 변경사항 (빌드 스크립트 수정 등)             |

### Branch Naming Convention

Git-flow 전략 기반으로 main, develop, feature 브랜치를 사용합니다.

|   Type    | Description                                     |
|:---------:|-------------------------------------------------|
|  `main`   | 메인 브랜치, 배포용                                     |
| `develop` | 개발 브랜치, 배포가 가능한 상태가 되면 main 브랜치로 merge |
| `feature` | 기능 추가 브랜치, 새로운 기능 개발이 필요한 경우 develop 브랜치에서 분기   |