package com.carvana.global.notification.entity;

import com.carvana.domain.owner.member.entity.OwnerMember;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OwnerFcmToken extends BaseFcmToken {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_member_id")
    private OwnerMember ownerMember;

    @Builder
    public OwnerFcmToken(String token, OwnerMember ownerMember) {
        super(token);
        this.ownerMember = ownerMember;
    }
}
