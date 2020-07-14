package com.bailbots.fb.quartersbot.dao;

import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "reserve_house")
public class ReserveHouse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "facebook_id")
    private String facebookId;

    @Column(name = "house_id")
    private Long houseId;

    @Column(name = "date_from")
    private Date dateFrom;

    @Column(name = "date_to")
    private Date dateTo;

    @ToString.Exclude
    @ManyToMany(mappedBy = "reserveHouses")
    private List<User> users;
}
