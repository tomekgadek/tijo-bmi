package com.example.bmimanager.weight.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

class InMemoryWeightRecordRepository implements WeightRecordRepository {

    private final ConcurrentHashMap<Long, WeightRecord> map = new ConcurrentHashMap<>();
    private long idSequence = 1L;

    @Override
    public List<WeightRecord> findByUserIdOrderByRecordDateAsc(Long userId) {
        return map.values().stream()
                .filter(r -> r.getUserId().equals(userId))
                .sorted(Comparator.comparing(WeightRecord::getRecordDate))
                .collect(Collectors.toList());
    }

    @Override
    public Page<WeightRecord> findByUserIdOrderByRecordDateAsc(Long userId, Pageable pageable) {
        List<WeightRecord> allRecords = findByUserIdOrderByRecordDateAsc(userId);

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), allRecords.size());

        List<WeightRecord> pageContent;
        if (start >= allRecords.size()) {
            pageContent = new ArrayList<>();
        } else {
            pageContent = allRecords.subList(start, end);
        }

        return new PageImpl<>(pageContent, pageable, allRecords.size());
    }

    @Override
    public Optional<WeightRecord> findTopByUserIdOrderByRecordDateDesc(Long userId) {
        return map.values().stream()
                .filter(r -> r.getUserId().equals(userId))
                .max(Comparator.comparing(WeightRecord::getRecordDate));
    }

    @Override
    public WeightRecord save(WeightRecord record) {
        if (record.getId() == null) {
            record.setId(idSequence++);
            if (record.getCreatedAt() == null) {
                record.onCreate(); // mimic @PrePersist
            }
        }
        map.put(record.getId(), record);
        return record;
    }

    @Override
    public Optional<WeightRecord> findById(Long recordId) {
        return Optional.ofNullable(map.get(recordId));
    }

    @Override
    public void deleteById(Long recordId) {
        map.remove(recordId);
    }
}
