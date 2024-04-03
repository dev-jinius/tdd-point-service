package io.hhplus.tdd.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@AllArgsConstructor
public enum TddErrorCode {

    NOT_FOUND_USER(HttpStatus.BAD_REQUEST.value(), "ERR-01", "존재하지 않는 유저입니다."),
    NOT_ENOUGH_POINT(HttpStatus.BAD_REQUEST.value(),"ERR-02", "포인트가 부족합니다.")
    ;

    private int status;
    private String code;
    private String message;
}
