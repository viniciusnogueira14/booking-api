CREATE TABLE PROPERTY (
    ID_PROPERTY NUMBER NOT NULL,
    UUID_PROPERTY UUID NOT NULL,
    NAME_PROPERTY VARCHAR(100) NOT NULL,
    DESC_PROPERTY VARCHAR(1024),

    CONSTRAINT PK_PROPERTY PRIMARY KEY(ID_PROPERTY)
);
CREATE SEQUENCE SEQ_PROPERTY START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;

CREATE TABLE GUEST (
    ID_GUEST NUMBER NOT NULL,
    UUID_GUEST UUID NOT NULL,
    NAME_GUEST VARCHAR(100) NOT NULL,
    AGE_GUEST INTEGER NOT NULL,
    EMAIL_GUEST VARCHAR(100),
    DOC_TYPE_GUEST VARCHAR(20),
    DOC_NMB_GUEST VARCHAR(100),

    CONSTRAINT PK_GUEST PRIMARY KEY(ID_GUEST)
);
CREATE SEQUENCE SEQ_GUEST START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;

CREATE TABLE BOOKING (
    ID_BOOKING NUMBER NOT NULL,
    UUID_BOOKING UUID NOT NULL,
    ID_PROPERTY NUMBER NOT NULL,
    ST_BOOKING VARCHAR(20) NOT NULL,
    DT_START DATE NOT NULL,
    DT_END DATE NOT NULL,

    CONSTRAINT PK_BOOKING PRIMARY KEY(ID_BOOKING),
    CONSTRAINT FK_PROPERTY FOREIGN KEY(ID_PROPERTY) REFERENCES PROPERTY(ID_PROPERTY)
);
CREATE SEQUENCE SEQ_BOOKING START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;

CREATE TABLE BOOKING_GUEST (
    ID_BOOKING NUMBER NOT NULL,
    ID_GUEST NUMBER NOT NULL,

    CONSTRAINT PK_BOOKING_GUEST PRIMARY KEY(ID_BOOKING, ID_GUEST),
    CONSTRAINT FK_BOOKING FOREIGN KEY(ID_BOOKING) REFERENCES BOOKING(ID_BOOKING),
    CONSTRAINT FK_GUEST FOREIGN KEY(ID_GUEST) REFERENCES GUEST(ID_GUEST)
);