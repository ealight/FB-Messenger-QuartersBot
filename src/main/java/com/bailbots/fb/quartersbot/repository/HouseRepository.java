package com.bailbots.fb.quartersbot.repository;

import com.bailbots.fb.quartersbot.dao.House;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HouseRepository extends JpaRepository<House, Long>, HouseRepositoryCustom{

    House getById(Long id);

    Long countByFilters(int minSeats, int minPrice, int maxPrice, boolean swimmingPol, boolean bath);

    List<House> getHousesListByFilters(int minSeats, int minPrice, int maxPrice, boolean swimmingPool, boolean bath, Integer page, int pageSize);

    Long countByFacebookId(String facebookId, String table);

    List<House> getHousesListByFacebookId(String facebookId, String table, Integer page, int pageSize);

}
