package io.hhplus.tdd.point.dto;

import io.hhplus.tdd.point.PointHistory;
import io.hhplus.tdd.point.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class PointHistoryDto {
    private long id;
    private Long userId;
    private Long amount;
    private TransactionType type;
    private Long updateMillis;

    public static PointHistoryDto of(PointHistory entity) {
        return PointHistoryDto.builder()
                .id(entity.id())
                .userId(entity.userId())
                .amount(entity.amount())
                .type(entity.type())
                .updateMillis(entity.updateMillis())
                .build();
//                .id(entity.getId())
//                .userId(entity.getUserId())
//                .amount(entity.getAmount())
//                .type(entity.getType())
//                .updateMillis(entity.getUpdateMillis())
//                .build();
    }
}
