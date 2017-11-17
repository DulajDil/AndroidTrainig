package com.bit.app.repo;

import com.bit.app.entities.CheckEntity;

import java.util.Map;

public interface CheckedRepository {

    void saveCheck(CheckEntity check);

    void updateCheck(CheckEntity check);

    CheckEntity findCheck(String id);

    Map<Object, Object> findAllChecked();

    void deleteCheck(String id);
}
