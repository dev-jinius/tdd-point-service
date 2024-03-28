package io.hhplus.tdd.point;

import io.hhplus.tdd.exception.TddException;
import org.apache.catalina.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.hhplus.tdd.exception.TddErrorCode.NOT_ENOUGH_POINT;
import static org.mockito.Mockito.when;

class UserPointTest {

    /**
     * 유저가 없을 때 새로 생성하는 기능
     */
    @Test
    void empty() {
        //given
        Long userId = 1L;
        UserPoint userPoint = null;

        //when
        UserPoint newUser = userPoint.empty(userId);

        //then
        assert newUser.getId() == userId;
        assert newUser != null;
    }

    /**
     * 포인트 충전 기능
     */
    @Test
    @DisplayName("원래 있던 포인트에 충전한 포인트를 추가한다.")
    void charge() {
        //given
        Long userId = 1L;
        Long originPoint = 5000L;
        Long requestCharge = 7000L;

        //when
        UserPoint dbUser = UserPoint.builder()
                                        .id(userId)
                                        .point(originPoint)
                                        .updateMillis(System.currentTimeMillis())
                                        .build();
        dbUser.charge(requestCharge);

        //then
        assert dbUser.getPoint() == originPoint + requestCharge;
    }

    /**
     * 포인트 사용 기능 - 실패 (포인트 부족)
     */
    @Test
    @DisplayName("기존 포인트보다 사용 금액이 더 크면, TddException(NOT_ENOUGH_POINT) 예외를 반환한다.")
    void NOT_ENOUGH_POINT() {
        //given
        Long userId = 1L;
        Long amount = 10000L;
        UserPoint originUserPoint = UserPoint.builder()
                                                .id(userId)
                                                .point(3000L)
                                                .updateMillis(System.currentTimeMillis())
                                                .build();

        Exception exception = null;
        //when
        try {
            originUserPoint.use(amount);
        } catch (TddException e) {
            exception = e;
        }

        assert exception != null;
        assert exception instanceof TddException;
        assert ((TddException) exception).getTddErrorCode() == NOT_ENOUGH_POINT;
    }

    /**
     * 포인트 사용 기능 - 성공
     */
    @Test
    @DisplayName("원래 있던 포인트에서 사용한 금액만큼 차감한다.")
    void use() {
        //given
        Long userId = 1L;
        Long amount = 10000L;
        UserPoint originUserPoint = UserPoint.builder()
                .id(userId)
                .point(30000L)
                .updateMillis(System.currentTimeMillis())
                .build();

        //when
        originUserPoint.use(amount);

        assert originUserPoint.getId() == userId;
        assert originUserPoint.getPoint() == 30000 - 10000;
    }
}