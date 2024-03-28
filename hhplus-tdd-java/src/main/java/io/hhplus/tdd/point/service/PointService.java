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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.stream.Collectors;

import static io.hhplus.tdd.exception.TddErrorCode.NOT_ENOUGH_POINT;
import static io.hhplus.tdd.exception.TddErrorCode.NOT_FOUND_USER;
import static io.hhplus.tdd.point.TransactionType.CHARGE;

@Service
@RequiredArgsConstructor
public class PointService {

    private final UserPointTable userPointRepository;
    private final PointHistoryTable pointHistoryRepository;

    /**
     * 포인트 조회
     * @param id
     * @return
     */
    public UserPointDto userPoint(Long id) {
        UserPoint dbUserPoint = userPointRepository.selectById(id);
        if (ObjectUtils.isEmpty(dbUserPoint)) throw new TddException(NOT_FOUND_USER);

        return UserPointDto.of(dbUserPoint);
    }

    /**
     * 포인트 충전
     * @param id
     * @param amount
     * @return
     */
    public UserPointDto chargePoint(Long id, Long amount) {
        UserPoint dbUserPoint = userPointRepository.selectById(id);
        if (ObjectUtils.isEmpty(dbUserPoint)) throw new TddException(NOT_FOUND_USER);

        dbUserPoint.charge(amount);

        UserPoint result = userPointRepository.insertOrUpdate(dbUserPoint.getId(), dbUserPoint.getPoint());
        pointHistoryRepository.insert(id, dbUserPoint.getPoint(), CHARGE, System.currentTimeMillis());

        return UserPointDto.of(result);
    }

    /**
     * 포인트 사용
     * @return
     */
    public UserPointDto usePoint(Long id, Long amount) {
        UserPoint dbUserPoint = userPointRepository.selectById(id);
        if (ObjectUtils.isEmpty(dbUserPoint)) throw new TddException(NOT_FOUND_USER);

        dbUserPoint.use(amount);

        UserPoint usedUserPoint = userPointRepository.insertOrUpdate(dbUserPoint.getId(), dbUserPoint.getPoint());
        pointHistoryRepository.insert(id, dbUserPoint.getPoint(), CHARGE, System.currentTimeMillis());

        return UserPointDto.of(usedUserPoint);
    }

    /**
     * 포인트 충전, 사용 내역 조회
     * @return
     */
    public List<PointHistoryDto> pointHistory(Long id) {
        UserPoint dbUserPoint = userPointRepository.selectById(id);
        if (ObjectUtils.isEmpty(dbUserPoint)) throw new TddException(NOT_FOUND_USER);

        List<PointHistory> dbHistoryList = pointHistoryRepository.selectAllByUserId(dbUserPoint.getId());

        return dbHistoryList.stream().map(history -> PointHistoryDto.of(history)).collect(Collectors.toList());
    }
}
