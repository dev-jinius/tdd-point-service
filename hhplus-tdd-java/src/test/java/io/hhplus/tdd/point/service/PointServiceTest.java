package io.hhplus.tdd.point.service;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;
import io.hhplus.tdd.exception.TddErrorCode;
import io.hhplus.tdd.exception.TddException;
import io.hhplus.tdd.point.UserPoint;
import io.hhplus.tdd.point.dto.UserPointDto;
import org.apache.catalina.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.ObjectUtils;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

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
     * 포인트 충전
     */
    @Test
    @DisplayName("충전을 한 후 정상적으로 UserPoint가 반환되면, UserPointDto로 반환한다.")
    void charge() {
        //given
        long userId = 1;
        long charge = 5000L;
        UserPoint userPoint = new UserPoint(userId, charge, System.currentTimeMillis());

        //when
//        when(userPointRepository.selectById(userId)).thenReturn(userPoint);
        when(userPointRepository.insertOrUpdate(userPoint.getId(), 5000L)).thenReturn(userPoint);
        UserPointDto chargedUserPoint = sut.chargePoint(userPoint.getId(), charge);

        //then
        assert ;
//        assert chargedUserPoint.getPoint() == charge;
    }

    @Test
    void usePoint() {
    }

    @Test
    void pointHistory() {
    }
}