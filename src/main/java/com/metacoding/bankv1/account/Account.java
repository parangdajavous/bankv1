package com.metacoding.bankv1.account;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@NoArgsConstructor
@Getter
@Table(name = "account_tb")
@Entity
public class Account {

    @Id
    private Integer number;  //계좌번호: PK
    private String password;
    private Integer balance; //잔액
    private Integer userId; //FK
    private Timestamp createdAt;  //생성날짜 (insert 된 시간)

}
