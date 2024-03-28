//package io.hhplus.tdd.point;
//
//import lombok.Builder;
//import lombok.Data;
//
//@Data
//public class PointHistory {
//
//        private Long id;
//        private long userId;
//        private Long amount;
//        private TransactionType type;
//        private Long updateMillis;
//
//        @Builder
//        public PointHistory(Long id, Long userId, Long amount, TransactionType type, Long updateMillis) {
//            this.id = id;
//            this.userId = userId;
//            this.amount = amount;
//            this.type = type;
//            this.updateMillis = updateMillis;
//        }
//}

package io.hhplus.tdd.point;

public record PointHistory(
        long id,
        long userId,
        long amount,
        TransactionType type,
        long updateMillis
) {
}
