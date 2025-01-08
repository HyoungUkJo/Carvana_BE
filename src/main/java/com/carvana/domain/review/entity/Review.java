package com.carvana.domain.review.entity;

import com.carvana.domain.customer.member.entity.CustomerMember;
import com.carvana.domain.reservation.entity.Reservation;
import com.carvana.domain.store.carwash.entity.CarWash;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id")    // 어떤 예약인지
    private Reservation reservation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_member_id")    // 누가 리뷰를 남겼는지
    private CustomerMember customerMember;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "carwash_id")        // 어떤 세차장을 갔는지
    private CarWash carWash;

    // 평점
    private int rating;

    // 리뷰 내용
    private String content;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @ElementCollection
    @CollectionTable(
        name = "review_imges",
        joinColumns = @JoinColumn(name = "review_id")
    )
    private List<String> imageUrls = new ArrayList<>(); // 리뷰 이미지를 저장할 리스트
                                                        // 추후 리뷰를 삭제시켰을때 이 이미지도 삭제시키는 로직 필요. (S3 기준인 array임)


    @Builder
    public Review(Long id, Reservation reservation, CustomerMember customerMember, CarWash carWash, int rating, String content, List<String> imageUrls) {
        this.id = id;
        this.reservation = reservation;
        this.customerMember = customerMember;
        this.carWash = carWash;
        this.rating = rating;
        this.content = content;
        this.imageUrls = imageUrls;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

}
