package com.sb.projects.trader.repository;

import com.sb.projects.trader.entity.StrategyOrder;
import com.sb.projects.trader.enums.StrategyOrderStatus;
import com.sb.projects.trader.enums.StrategyType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface StrategyOrderRepository extends CrudRepository<StrategyOrder, Long> {
    List<StrategyOrder> findByStrategyType(StrategyType strategyType);

    void deleteBySecurityIdAndStrategyType(String securityId, StrategyType strategyType);

    List<StrategyOrder> findByStatus(StrategyOrderStatus strategyOrderStatus);

    StrategyOrder findBySecurityIdAndUserIdAndStatus(String securityId, String userId, StrategyOrderStatus strategyOrderStatus);

    StrategyOrder findBySecurityIdAndUserId(String securityId, String userId);

    @Query("SELECT so FROM StrategyOrder so WHERE so.securityId = ?1 and so.userId = ?2 order by so.created desc fetch first 1 rows only")
    StrategyOrder findLatestStrategyOrder(String securityId, String userId);
}
