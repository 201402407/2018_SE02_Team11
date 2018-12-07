CREATE TABLE Account (
	accountID varchar(20) NOT NULL PRIMARY KEY,
    pwd varchar(20) NOT NULL,
    accountName varchar(5) NOT NULL, -- name은 SQL의 예약어이기 때문에 accountName으로 명명.
    birth date NOT NULL -- int가 아닌 date입니다
);

CREATE TABLE StudentIDRequest (
	reqSIDnum integer(8) NOT NULL AUTO_INCREMENT PRIMARY KEY,
    reqSIDdate date NOT NULL,
    accountID varchar(20) NOT NULL,
    CONSTRAINT StudentIDRequest_FK
    FOREIGN KEY (accountID) REFERENCES Account(AccountID)
);

CREATE TABLE Department (
	departmentCode integer(5) NOT NULL AUTO_INCREMENT PRIMARY KEY,
    departmentName varchar(15) NOT NULL,
    tuition int
);

CREATE TABLE Student (
	studentID integer(9) NOT NULL PRIMARY KEY,  -- AUTO_INCREMENT가 아님에 주의 (즉 Student데이터를 INSERT해줄 때 학번을 정해줘야 한다는 뜻이다.)
    studentName varchar(5) NOT NULL,
    studentYear integer(1) NOT NULL,
    semester integer(2) NOT NULL,
    isTimeOff bool NOT NULL,
    isGraduate bool NOT NULL,
    accountID varchar(20) NOT NULL,
    departmentCode integer(5) NOT NULL,
    CONSTRAINT Student_accountID_FK
    FOREIGN KEY (accountID) REFERENCES Account(AccountID),
    CONSTRAINT Student_departmentCode_FK
    FOREIGN KEY (departmentCode) REFERENCES Department(departmentCode)
);

CREATE TABLE Subject (
	subjectCode integer(5) NOT NULL AUTO_INCREMENT PRIMARY KEY,
    subjectName varchar(20) NOT NULL,
    score decimal(5, 2) NOT NULL
);

CREATE TABLE Professor (
	professorCode integer(5) NOT NULL AUTO_INCREMENT PRIMARY KEY,
    profName varchar(5) NOT NULL
);

CREATE TABLE Syllabus (
	syllabusCode integer(5) NOT NULL AUTO_INCREMENT PRIMARY KEY,
    syllabusText varchar(255) NOT NULL
);

CREATE TABLE Lecture (
	lectureCode integer(7) NOT NULL AUTO_INCREMENT PRIMARY KEY,
    registerTerm integer(2) NOT NULL,
    applyNum integer(4) NOT NULL,
    allNum integer(4) NOT NULL,
    dayOfWeek int NOT NULL,  --(1=월, 2=화, 3=수, 4=목, 5=금)
    startTime time NOT NULL,
    endTime time NOT NULL,
    subjectCode integer(5) NOT NULL,
    departmentCode integer(5) NOT NULL,
    profCode integer(5) NOT NULL,
    syllabusCode integer(5) NOT NULL,
    CONSTRAINT Lecture_subjectCode_FK
    FOREIGN KEY (subjectCode) REFERENCES Subject(subjectCode),
    CONSTRAINT Lecture_departmentCode_FK
    FOREIGN KEY (departmentCode) REFERENCES Department(departmentCode),
    CONSTRAINT Lecture_profCode_FK
    FOREIGN KEY (profCode) REFERENCES Professor(professorCode),
    CONSTRAINT Lecture_syllabusCode_FK
    FOREIGN KEY (syllabusCode) REFERENCES Syllabus(syllabusCode)
);

CREATE TABLE Attendance (
	attendanceNum integer(5) NOT NULL AUTO_INCREMENT PRIMARY KEY,
    isRetake bool NOT NULL,
    studentID integer(9) NOT NULL,
    lectureCode integer(7) NOT NULL,
    CONSTRAINT Attendance_studentID_FK
    FOREIGN KEY (studentID) REFERENCES Student(studentID),
    CONSTRAINT Attendance_lectureCode_FK
    FOREIGN KEY (lectureCode) REFERENCES Lecture(lectureCode)
);

CREATE TABLE LectureEvaluation (
	attendanceNum integer(5) NOT NULL, -- 강의평가 엔티티, 성적 엔티티는 PK가 없음.
    LectureEvaluationText varchar(255) NOT NULL,
    CONSTRAINT LectureEvaluation_attendanceNum_FK 
    FOREIGN KEY (attendanceNum) REFERENCES Attendance(attendanceNum)
);

CREATE TABLE GradeInfo ( 
	attendanceNum integer(5) NOT NULL,
    grade decimal(5, 2) NOT NULL,
    CONSTRAINT GradeInfo_attendanceNum_FK 
    FOREIGN KEY (attendanceNum) REFERENCES Attendance(attendanceNum)
);    

CREATE TABLE ChangeRecord (
	changeRecordNum integer(5) NOT NULL AUTO_INCREMENT PRIMARY KEY, -- changeRecordNum으로 이름 변경
    changeDate Date NOT NULL,
    changeType tinyint(1) NOT NULL, -- 0=TAKEOFF, 1=RESUME
    -- changeType을 자바에서 Enum으로 불러들여야 할 때도, resultSet에서 getInt로 받고 시작할 것.
    startSemester integer(2) NOT NULL,
    endSemester integer(2) NOT NULL,
    reason varchar(100),
    studentID integer(9) NOT NULL,
    CONSTRAINT ChangeRecord_studentID_FK 
    FOREIGN KEY (studentID) REFERENCES Student(studentID)
);

CREATE TABLE TimeoffRequest (
	reqNum integer(5) NOT NULL AUTO_INCREMENT PRIMARY KEY,
    reqDate Date NOT NULL,
    changeType tinyint(1) NOT NULL,  -- 0=TAKEOFF, 1=RESUME
    reason varchar(100),
    startSemester integer(5) NOT NULL,
    endSemester integer(5) NOT NULL,
    studentID integer(9) NOT NULL,
    CONSTRAINT TimeoffRequest_studentID_FK 
    FOREIGN KEY (studentID) REFERENCES Student(studentID)
);

CREATE TABLE Scholarship (
	scholarshipNum integer(5) AUTO_INCREMENT NOT NULL PRIMARY KEY,
    scholarshipName varchar(20) NOT NULL
);

CREATE TABLE ScholarshipAward (
	awardNum integer(5) AUTO_INCREMENT NOT NULL PRIMARY KEY,
    awardMoney integer(10) NOT NULL default 0,
    awardDate Date NOT NULL,
    scholarshipNum integer(5) NOT NULL,
    studentID integer(9) NOT NULL,  -- 지금까지 studentID varchar(8)라고 되어있는 것들을 고쳤습니다.
    CONSTRAINT scholarshipAward_scholarshipNum_FK 
    FOREIGN KEY (scholarshipNum) REFERENCES Scholarship(scholarshipNum),
    CONSTRAINT ScholarshipAward_studentID_FK 
    FOREIGN KEY (studentID) REFERENCES Student(studentID)
    
);