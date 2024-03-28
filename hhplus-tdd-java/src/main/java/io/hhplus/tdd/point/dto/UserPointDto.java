package io.hhplus.tdd.point.dto;

import io.hhplus.tdd.point.UserPoint;
import lombok.*;

@Getter
@AllArgsConstructor
@Builder
public class UserPointDto {

    private long id;
    private long point;
    private long updateMillis;

    public static UserPointDto of(UserPoint entity) {
        return UserPointDto.builder()
                .id(entity.getId())
                .point(entity.getPoint())
                .updateMillis(entity.getUpdateMillis())
                .build();
    }
}
