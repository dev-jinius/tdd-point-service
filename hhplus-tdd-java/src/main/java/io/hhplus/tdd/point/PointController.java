package io.hhplus.tdd.point;

import io.hhplus.tdd.point.dto.PointHistoryDto;
import io.hhplus.tdd.point.dto.RequestDto;
import io.hhplus.tdd.point.dto.UserPointDto;
import io.hhplus.tdd.point.service.PointService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/point")
public class PointController {

    private static final Logger log = LoggerFactory.getLogger(PointController.class);

    private final PointService pointService;

    /**
     * TODO - 특정 유저의 포인트를 조회하는 기능을 작성해주세요.
     */
    @GetMapping("{id}")
    public ResponseEntity<UserPointDto> point(
            @PathVariable(name = "id") long id
    ) {
        UserPointDto response = pointService.userPoint(id);

        return ResponseEntity.ok().body(response);
    }

    /**
     * TODO - 특정 유저의 포인트 충전/이용 내역을 조회하는 기능을 작성해주세요.
     */
    @GetMapping("{id}/histories")
    public ResponseEntity<List<PointHistoryDto>> history(
            @PathVariable(name = "id") long id
    ) {
        List<PointHistoryDto> historyList = pointService.pointHistory(id);
        return ResponseEntity.ok().body(historyList);
    }

    /**
     * TODO - 특정 유저의 포인트를 충전하는 기능을 작성해주세요.
     */
    @PatchMapping(value = "{id}/charge")
    public ResponseEntity<UserPointDto> charge(
//            @PathVariable(name = "id") long id, long amount
            @PathVariable(name = "id") long id, @RequestBody RequestDto request
    ) {
        UserPointDto response = pointService.chargePoint(id, request.getAmount());
        return ResponseEntity.ok().body(response);
    }

    /**
     * TODO - 특정 유저의 포인트를 사용하는 기능을 작성해주세요.
     */
    @PatchMapping(value = "{id}/use")
    public ResponseEntity<UserPointDto> use(
            @PathVariable(name = "id") long id, @RequestBody RequestDto request
    ) {
        UserPointDto usedUserPoint = pointService.usePoint(id, request.getAmount());
        return ResponseEntity.ok().body(usedUserPoint);
    }
}
