package com.bailbots.fb.quartersbot.dao;

import com.bailbots.fb.quartersbot.domain.HouseFilter;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "facebook_id")
    private String facebookId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column
    private boolean notifications;

    @EqualsAndHashCode.Exclude
    @ManyToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    @JoinTable(name = "reserve_house",
            joinColumns = { @JoinColumn(name = "facebook_id") },
            inverseJoinColumns = { @JoinColumn(name = "house_id") })
    private List<ReserveHouse> reserveHouses;

    @Transient
    @Builder.Default
    private HouseFilter houseFilter = HouseFilter.builder()
            .minSeatsNumber(0)
            .minPrice(0)
            .maxPrice(100000)
            .bath(true)
            .swimmingPool(true)
            .build();
}
