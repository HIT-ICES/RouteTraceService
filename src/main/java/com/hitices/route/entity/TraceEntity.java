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

    @Column(name = "service", nullable = true)
    private String service;

    @Column(name = "api", nullable = true)
    private String api;

    @Column(name = "traceId", nullable = true)
    private String traceId;

    @Column(name = "time", nullable = true)
    private Date time;

    @Column(name = "data", nullable = true)
    private String data;

    @Column(name = "graph_data", nullable = true)
    private String graph;

    @Column(name = "request_size", nullable = true)
    private Long requestSize;

    @Column(name = "response_size", nullable = true)
    private Long responseSize;

    public TraceEntity() {

    }
}
