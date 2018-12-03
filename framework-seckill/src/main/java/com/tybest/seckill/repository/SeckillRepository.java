package com.tybest.seckill.repository;

import com.tybest.seckill.entity.Seckill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeckillRepository extends JpaRepository<Seckill,Long> {
}
