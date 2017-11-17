package com.bit.app.repo;

import com.bit.app.entities.CheckEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.Map;

@Slf4j
@Repository
public class CheckedRepositoryImpl implements CheckedRepository {

    private static final String KEY = "Check";

    private RedisTemplate<String, Object> redisTemplate;
    private HashOperations hashOperations;

    @Autowired
    public CheckedRepositoryImpl(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @PostConstruct
    private void init() {
        hashOperations = redisTemplate.opsForHash();
    }

    @Override
    public void saveCheck(CheckEntity check) {
        hashOperations.put(KEY, check.getId(), check);
        log.warn("Saving CheckEntity: " + check);
    }

    @Override
    public void updateCheck(CheckEntity check) {
        hashOperations.put(KEY, check.getId(), check);
        log.warn("Update CheckEntity: " + check);
    }

    @Override
    public CheckEntity findCheck(String id) {
        CheckEntity checkEntity = (CheckEntity) hashOperations.get(KEY, id);
        log.warn(String.format("Find CheckEntity: %s to id %s",  checkEntity, id));
        return checkEntity;
    }

    @Override
    public Map<Object, Object> findAllChecked() {
        return hashOperations.entries(KEY);
    }

    @Override
    public void deleteCheck(String id) {
        hashOperations.delete(KEY, id);
        log.warn("Delete CheckEntity to id " + id);
    }
}
