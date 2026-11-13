package com.bunon.repository;

import com.bunon.model.Loom;
import com.bunon.model.SubOrder;
import com.bunon.model.SubOrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubOrderRepository extends JpaRepository<SubOrder, Long> {
    List<SubOrder> findByLoomAndStatusIn(Loom loom, List<SubOrderStatus> statuses);
}
