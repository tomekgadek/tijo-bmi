package com.example.bmimanager.user.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

class InMemoryBmiUserRepository implements BmiUserRepository {

    private final ConcurrentHashMap<Long, BmiUser> map = new ConcurrentHashMap<>();
    private long idSequence = 1L;

    @Override
    public Optional<BmiUser> findByUsername(String username) {
        return map.values().stream()
                .filter(user -> username.equals(user.getUsername()))
                .findFirst();
    }

    @Override
    public List<BmiUser> findByIsPublicTrueAndIsBlockedFalse() {
        return map.values().stream()
                .filter(user -> Boolean.TRUE.equals(user.getIsPublic()) && Boolean.FALSE.equals(user.getIsBlocked()))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<BmiUser> findById(Long id) {
        return Optional.ofNullable(map.get(id));
    }

    @Override
    public BmiUser save(BmiUser user) {
        if (user.getId() == null) {
            user.setId(idSequence++);
            if (user.getCreatedAt() == null) {
                user.onCreate(); // mimic @PrePersist
            }
        }
        map.put(user.getId(), user);
        return user;
    }

    @Override
    public List<BmiUser> findAll() {
        return new ArrayList<>(map.values());
    }

    @Override
    public void deleteById(Long id) {
        map.remove(id);
    }
}
