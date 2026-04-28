# TIL

공부한 내용을 주제별 폴더로 정리하는 저장소입니다.

## 폴더 구조

```text
TIL/
├── README.md
└── MSA/
    ├── README.md
    ├── topics/
    │   └── _template/
    │       ├── README.md
    │       └── notes.md
    └── archive/
```

## 작성 규칙

- 큰 주제 단위로 최상위 폴더를 만든다.
- 세부 공부 주제마다 `topics/` 아래에 폴더를 만든다.
- 각 주제 폴더 안에는 필요에 따라 여러 `md` 파일을 작성한다.
- 시작이 어려우면 `_template` 폴더를 복사해서 새 주제를 만든다.

예시:

```text
MSA/topics/service-discovery/
MSA/topics/api-gateway/
MSA/topics/distributed-transaction/
```
