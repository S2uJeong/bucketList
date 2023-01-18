package com.team9.bucket_list.domain.entity;

import com.team9.bucket_list.domain.enumerate.Membership;
import com.team9.bucket_list.domain.enumerate.PaymentStatus;
import jakarta.persistence.*;

@Entity
public class Payment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long id;

    @Enumerated(value = EnumType.STRING)
    private Membership membership;

    @Enumerated(value = EnumType.STRING)
    private PaymentStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
}
