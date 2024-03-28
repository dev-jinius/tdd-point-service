package io.hhplus.tdd.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TddException extends RuntimeException {
    private TddErrorCode TddErrorCode;
}
