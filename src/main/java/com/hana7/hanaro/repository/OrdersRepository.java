package com.hana7.hanaro.repository;

import com.hana7.hanaro.entity.OrderItem;
import com.hana7.hanaro.entity.Orders;
import com.hana7.hanaro.entity.SaleStat;
import com.hana7.hanaro.entity.User;
import com.hana7.hanaro.enums.ORDERSTATUS;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

public interface OrdersRepository extends JpaRepository<Orders, Long>{
    Page<Orders> findByUser(User user, Pageable pageable);

    Page<Orders> findByCreatedAtBetween(LocalDateTime createdAtAfter, LocalDateTime createdAtBefore, Pageable pageable);

    @Modifying                   // payed -> preparing  (5초)
    @Transactional               //    20     25 - 5초 = 20, (24 - 5) => 19 (26 - 5) = 21
    @Query("update Orders o set o.orderStatus = :nextState, o.updatedAt = now()"
        + " where o.orderStatus = :state and o.createdAt <= :timeToUp")
    int updateStateBatch(@Param("state") ORDERSTATUS state,
        @Param("nextState") ORDERSTATUS nextState,
        @Param("timeToUp") LocalDateTime timeToUp);

    List<Orders> findByCreatedAtBetween(LocalDateTime createdAtAfter, LocalDateTime createdAtBefore);

    @Query(value = "select 'today' saledt, count(*) ordercnt, 0 totamt from Orders o"
        + " where o.createdAt between concat(:saledt, ' 00:00:00.00') and concat(:saledt, ' 23:59:59.99')", nativeQuery = true)
    SaleStat getTodayStat(@Param("saledt") String saledt);

    @Query(value =
        "select oi.item as id, max(oi.id) as orders, oi.item, sum(oi.amount) as amount, sum(oi.totalPrice) as totalPrice"
            + "  from Orders o inner join OrderItem oi on o.id = oi.orders"
            + " where o.createdAt between concat(:saledt, ' 00:00:00.00') and concat(:saledt, ' 23:59:59.99')"
            + " group by oi.item", nativeQuery = true)
    List<OrderItem> getTodayItemStat(@Param("saledt") String saledt);
}
