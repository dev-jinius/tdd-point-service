package io.hhplus.tdd.point.service;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;
import io.hhplus.tdd.exception.TddErrorCode;
import io.hhplus.tdd.exception.TddException;
import io.hhplus.tdd.point.PointHistory;
import io.hhplus.tdd.point.TransactionType;
import io.hhplus.tdd.point.UserPoint;
import io.hhplus.tdd.point.dto.PointHistoryDto;
import io.hhplus.tdd.point.dto.UserPointDto;
import org.apache.catalina.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.stubbing.BaseStubbing;
import org.mockito.internal.stubbing.ConsecutiveStubbing;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.ObjectUtils;

import javax.lang.model.type.ErrorType;

import java.util.List;

import static io.hhplus.tdd.exception.TddErrorCode.NOT_ENOUGH_POINT;
import static io.hhplus.tdd.point.TransactionType.CHARGE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.will;
import static org.mockito.Mockito.when;

/**
 * 유닛테스트 (비즈니스)
 */
@ExtendWith(MockitoExtension.class)
class PointServiceTest {

    @InjectMocks
    private PointService sut;
    @Mock
    private UserPointTable userPointRepository;
    @Mock
    private PointHistoryTable pointHistoryRepository;

    /**
     * 포인트 조회 - 실패 (유저 조회 실패)
     */
    @Test
    @DisplayName("userPoint가 null이면, NOT_FOUND_USER 예외를 반환한다.")
    void NOT_FOUND_USER() {
        Long userId = 1L;

        when(userPointRepository.selectById(userId)).thenReturn(null);

        Exception e = null;
        try {
            UserPointDto userPointDto = sut.userPoint(userId);
            if (ObjectUtils.isEmpty(userPointDto)) throw new TddException(TddErrorCode.NOT_FOUND_USER);
        } catch (TddException exception) {
            e = exception;
        }

        assert e != null;
        assert e instanceof TddException;
        assert ((TddException) e).getTddErrorCode() == TddErrorCode.NOT_FOUND_USER;
        assert (((TddException) e).getTddErrorCode().getMessage()).equals("존재하지 않는 유저입니다.");
    }

    /**
     * 포인트 조회 - 성공
     */
    @Test
    @DisplayName("userPoint가 정상적으로 반환되면, UserPointDto로 반환한다.")
    void userPoint() {
        //given
        Long userId = 1L;

        //when
        when(userPointRepository.selectById(userId)).thenReturn(new UserPoint(userId, 0L, System.currentTimeMillis()));
        UserPointDto dbUser = sut.userPoint(userId);

        //then
        assert dbUser.getId() == userId;
    }

    /**
     * 포인트 충전 - 성공
     */
    @Test
    @DisplayName("충전을 한 후 정상적으로 UserPoint가 반환되면, UserPointDto로 반환한다.")
    void charge() {
        //given
        Long userId = 1L;
        Long originPoint = 1000L;
        Long charge = 5000L;

        given(userPointRepository.selectById(userId)).willReturn(new UserPoint(userId, originPoint, System.currentTimeMillis()));

        //when
        Long amount = originPoint + charge;
        Long current = 0L;
        PointHistory pointHistory = new PointHistory(1L, userId, amount, CHARGE, current);
        when(userPointRepository.insertOrUpdate(userId, amount)).thenReturn(new UserPoint(userId, amount, System.currentTimeMillis()));

        UserPointDto chargedUserPoint = sut.chargePoint(userId, charge);

        //then
        assert chargedUserPoint.getPoint() == amount;
        assert chargedUserPoint.getId() == userId;
        assert pointHistory.type() == CHARGE;
        assert pointHistory.userId() == userId;
    }

    /**
     * 포인트 사용 - 실패 (포인트 부족)
     */
    @Test
    @DisplayName("기존 포인트보다 사용할 포인트가 더 크면, TddException(NOT_ENOUGH_POINT) 예외를 반환한다.")
    void NOT_ENOUGH_POINT() {
        //given
        Long userId = 1L;
        Long amount = 10000L;
        Long originPoint = 5000L;
        given(userPointRepository.selectById(userId)).willReturn(new UserPoint(userId, originPoint, System.currentTimeMillis()));

        //when
        Exception exception = null;
        try {
            sut.usePoint(userId, amount);
        } catch (TddException e) {
            exception = e;
        }

        //then
        assert exception != null;
        assert exception instanceof TddException;
        assert ((TddException) exception).getTddErrorCode() == NOT_ENOUGH_POINT;
    }

    /**
     * 포인트 사용 - 성공
     */
    @Test
    @DisplayName("성공적으로 포인트 사용을 하면, 그대로 반환한다.")
    void usePoint() {
        //given
        Long userId = 1L;
        Long amount = 10000L;
        Long originPoint = 20000L;
        given(userPointRepository.selectById(userId)).willReturn(new UserPoint(userId, originPoint, System.currentTimeMillis()));

        //when
        amount = originPoint - amount;
        when(userPointRepository.insertOrUpdate(userId, amount)).thenReturn(new UserPoint(userId, amount, System.currentTimeMillis()));
        UserPointDto usedUserPoint = sut.usePoint(userId, amount);

        //then
        assert usedUserPoint != null;
        assert usedUserPoint.getId() == userId;
        assert usedUserPoint.getPoint() == amount;
    }

}