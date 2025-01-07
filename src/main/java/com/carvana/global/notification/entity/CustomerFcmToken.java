package com.carvana.global.notification.entity;

import com.carvana.domain.customer.member.entity.CustomerMember;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CustomerFcmToken extends BaseFcmToken{

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_member_id")
    private CustomerMember customerMember;

    @Builder
    public CustomerFcmToken(String token, CustomerMember customerMember) {
        super(token);
        this.customerMember = customerMember;
    }
}
