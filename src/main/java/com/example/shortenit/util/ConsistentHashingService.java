package com.example.shortenit.util;

import java.util.SortedMap;
import java.util.TreeMap;

import org.springframework.stereotype.Service;

@Service
public class ConsistentHashingService {
    private final SortedMap<Integer, String> ring = new TreeMap<>();
    private final int virtualNodes = 100;

    public void addNode(String nodeId) {
        for (int i = 0; i < virtualNodes; i++) {
            ring.put(hash(nodeId + i), nodeId);
        }
    }

    public String getNode(String key) {
        int hash = hash(key);
        if (!ring.containsKey(hash)) {
            SortedMap<Integer, String> tail = ring.tailMap(hash);
            hash = tail.isEmpty() ? ring.firstKey() : tail.firstKey();
        }
        return ring.get(hash);
    }

    private int hash(String key) {
        return key.hashCode();
    }
}