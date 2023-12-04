package com.hitices.route.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

/**
 * @author: wangteng
 * @e-mail: Willtynn@outlook.com
 * @date: 2023/8/29 14:31
 */
@Getter
@Setter
@AllArgsConstructor
@Entity
@Table(name = "trace")
public class TraceEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "service", nullable = false)
    private String service;

    @Column(name = "api", nullable = false)
    private String api;

    @Column(name = "traceId", nullable = false)
    private String traceId;

    @Column(name = "time", nullable = false)
    private Date time;

    @Column(name = "data", nullable = false)
    private String data;

    public TraceEntity() {

    }
}
