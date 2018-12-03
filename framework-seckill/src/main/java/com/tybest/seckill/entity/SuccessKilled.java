package com.tybest.seckill.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name = "success_killed")
@Getter
@Setter
public class SuccessKilled implements Serializable{
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "seckill_id", nullable = false)
	private long seckillId;
	@Id
	private long userId;
	private short state;
	private Timestamp createTime;
}
