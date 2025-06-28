package com.example.logistics.repository;

import com.example.logistics.entity.LogisticsTrace;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface LogisticsTraceRepository extends MongoRepository<LogisticsTrace, String> {
    
    Optional<LogisticsTrace> findByOrderId(Long orderId);
    
    @Query("{ 'traces.timestamp' : { $gte: ?0, $lte: ?1 } }")
    List<LogisticsTrace> findByTimeRange(LocalDateTime startTime, LocalDateTime endTime);
    
    @Query("{ 'traces.location' : ?0 }")
    List<LogisticsTrace> findByLocation(String location);
    
    @Query("{ 'traces.operator' : ?0 }")
    List<LogisticsTrace> findByOperator(String operator);
} 