package com.bailbots.fb.quartersbot.repository;

import java.util.List;

public interface KeyboardObjectRepository {
    List<Object> getObjectListByEntity(Class clazz, Integer page, int pageSize);
    Long getPageNumbersOfObjectListByEntity(Class clazz);
}
