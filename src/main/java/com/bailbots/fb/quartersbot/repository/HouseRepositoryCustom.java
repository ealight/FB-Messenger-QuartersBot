package com.bailbots.fb.quartersbot.repository;

import com.bailbots.fb.quartersbot.dao.House;

import java.util.List;

public interface HouseRepositoryCustom {
    Long countByFilters(int minSeats, int minPrice, int maxPrice, boolean swimmingPol, boolean bath);

    List<House> getHousesListByFilters(int minSeats, int minPrice, int maxPrice, boolean swimmingPool, boolean bath, Integer page, int pageSize);

    Long countByFacebookId(String facebookId, String table);

    List<House> getHousesListByFacebookId(String facebookId, String table,Integer page, int pageSize);
}
