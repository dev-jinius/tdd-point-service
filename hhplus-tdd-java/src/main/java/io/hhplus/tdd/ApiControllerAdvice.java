package io.hhplus.tdd;

import io.hhplus.tdd.exception.TddErrorCode;
import io.hhplus.tdd.exception.TddException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
class ApiControllerAdvice extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        return ResponseEntity.status(500).body(new ErrorResponse("500", "에러가 발생했습니다."));
    }

    /**
     * 포인트 서비스 예외 처리
     * @param e
     * @return
     */
    @ExceptionHandler(value = TddException.class)
    public ResponseEntity<TddErrorCode> handlePointServiceException(TddException e) {
        return ResponseEntity.status(400).body(e.getTddErrorCode());
    }
}
