package io.hhplus.tdd.point.integration;

import io.hhplus.tdd.point.dto.UserPointDto;
import io.hhplus.tdd.point.service.PointService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

import static org.assertj.core.api.Assertions.*;

/**
 * 통합테스트, 동시성 테스트
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class PointServiceTest {

    @Autowired
    private PointService pointService;

    @Test
    public void chargeAndUseSynchronizedTest() throws InterruptedException {
        long userId = 1L;
        int core = 10;
        ExecutorService executor = Executors.newFixedThreadPool(core);
        CountDownLatch latch = new CountDownLatch(core);
        List<String> startThreadList = new ArrayList<>();
        List<String> finishedThreadList = new ArrayList<>();

        for (int i = 0; i < core; i++) {
            long chargePoint = (long)(Math.random()*5+1)*1000;
            executor.submit(() -> {
                synchronized (this) {
                    long randomNum = (long)(Math.random()*2+1);
                    startThreadList.add(Thread.currentThread().getName());
                    try {
                        if (randomNum == 1) {
                            pointService.usePoint(userId, chargePoint);
                        } else {
                            pointService.chargePoint(userId, chargePoint);
                        }
                    } catch (Exception e) {
                    } finally {
//                        logger.info("완료한 스레드 : [{}]", Thread.currentThread().getName());
                        finishedThreadList.add(Thread.currentThread().getName());
                        latch.countDown();
                    }
                }
            });
        }
        latch.await();
        executor.shutdown();

        assertThat(startThreadList).isEqualTo(finishedThreadList);
    }

    @Test
    void ConcurrenyChargeTest() throws ExecutionException, InterruptedException {
        Long userId = 1L;
        Long amount = 2000L;

        List<CompletableFuture<UserPointDto>> futures = Arrays.asList(
                CompletableFuture.supplyAsync(() -> pointService.chargePoint(userId, amount)),
                CompletableFuture.supplyAsync(() -> pointService.chargePoint(userId, amount)),
                CompletableFuture.supplyAsync(() -> pointService.chargePoint(userId, amount)),
                CompletableFuture.supplyAsync(() -> pointService.chargePoint(userId, amount)),
                CompletableFuture.supplyAsync(() -> pointService.chargePoint(userId, amount))
        );

        CompletableFuture<Void> allDoneFuture = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
        allDoneFuture.thenRunAsync(() -> {
            UserPointDto resultDto = pointService.userPoint(userId);
            System.out.println(resultDto.getPoint());
            assertThat(resultDto.getPoint()).isEqualTo(2000 * 5);
        });
    }
}
