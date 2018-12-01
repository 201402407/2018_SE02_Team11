# 2018_SE02_Team11
- charset은 EUC-KR
- 모든 UI페이지는 jQuery(자바스크립트 라이브러리)를 포함해야 한다. AJAX 기능을 쉽게 사용하기 위함.
- jQuery는 "/external_lib/jquery-3.3.1.min.js" 에 있다.
- Proc페이지 : UI페이지로부터 입력을 받아 DAO클래스 오퍼레이션을 실행하여 그 결과를 출력하는 페이지.
- UI페이지는 form데이터를 Proc페이지에 submit할 때 AJAX를 이용한다. (즉, submit을 누를 때 브라우저는 Proc페이지로 이동을 하지 않는다.)
- UI페이지의 파일명 규칙: 딱히 없음
- Proc페이지의 파일명 규칙: proc_엔티티타입_오퍼레이션이름.jsp (예: proc_account_signup.jsp)