package com.bailbots.fb.quartersbot.service;

import com.bailbots.fb.quartersbot.dao.ReserveHouse;
import com.bailbots.fb.quartersbot.utils.callback.CallbackParser;

import java.util.Date;

public interface ReserveHouseService {
    boolean houseAlreadyReserved(String facebookId, CallbackParser parser, Date dateFrom, Date dateTo);
    boolean existByFacebookId(String facebookId, Long houseId);
    boolean dateFromMoreThenDateTo(String facebookId, Date dateFrom, Date dateTo);
    boolean selectedPastDate( String facebookId, CallbackParser parser, Date dateFrom, Date dateTo);
    ReserveHouse reserve(String facebookId, Long houseId, Date dateFrom, Date dateTo);
    void showReserveHouseDate(String facebookId, Long houseId);
}
