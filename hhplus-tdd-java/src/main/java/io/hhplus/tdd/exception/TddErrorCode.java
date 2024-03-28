package io.hhplus.tdd.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum TddErrorCode {

    NOT_FOUND_USER(HttpStatus.BAD_REQUEST, "400", "존재하지 않는 유저입니다."),
    NOT_ENOUGH_POINT(HttpStatus.BAD_REQUEST, "400", "포인트가 부족합니다.")
    ;

    private HttpStatus httpStatus;
    private String code;
    private String message;
}
