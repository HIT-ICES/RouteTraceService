package com.hitices.route.repository;

import com.hitices.route.entity.TraceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @author: wangteng
 * @e-mail: Willtynn@outlook.com
 * @date: 2023/8/29 14:35
 */
@Repository
public interface TraceRepository extends JpaRepository<TraceEntity, InternalError> {
    List<TraceEntity> findAllByTimeBetweenAndServiceIsOrderByDataDesc(Date start, Date end,String service);
    List<TraceEntity> findAllByTimeBetweenAndServiceIsAndApiIsOrderByDataDesc(Date start, Date end,String service, String api);
    TraceEntity findById(Long id);
}
