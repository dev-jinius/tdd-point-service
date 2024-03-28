package io.hhplus.tdd.point;

import io.hhplus.tdd.exception.TddErrorCode;
import io.hhplus.tdd.exception.TddException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.util.ObjectUtils;

import static io.hhplus.tdd.exception.TddErrorCode.NOT_ENOUGH_POINT;

// 유저 포인트 정보
@Data
public class UserPoint {
    private Long id;
    private Long point;
    private Long updateMillis;

    // 새로운 유저 생성
    public static UserPoint empty(Long id) {
        return new UserPoint(id, 0L, System.currentTimeMillis());
    }

    // 포인트 충전
    public void charge(Long amount) {
        this.point += amount;
        this.updateMillis = System.currentTimeMillis();
    }

    // 포인트 사용
    public void use(Long amount) {
        if (this.point < amount) throw new TddException(NOT_ENOUGH_POINT);
        this.point -= amount;
    }

    @Builder
    public UserPoint(Long id, Long point, Long updateMillis) {
        this.id = id;
        this.point = point;
        this.updateMillis = updateMillis;
    }

}