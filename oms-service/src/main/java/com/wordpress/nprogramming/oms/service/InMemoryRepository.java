package com.wordpress.nprogramming.oms.service;

import com.wordpress.nprogramming.oms.api.ItemWithId;
import com.wordpress.nprogramming.oms.api.Repository;

import java.util.*;

public class InMemoryRepository<TItem extends ItemWithId>
        implements Repository<TItem> {

    protected final Map<String, TItem> items = new HashMap<>();

    @Override
    public void save(TItem item) {
        items.put(item.id(), item);
    }

    @Override
    public TItem queryById(String id) {

        if (items.containsKey(id)) {
            return items.get(id);
        }

        return null;
    }

    @Override
    public List<TItem> getAll() {
        return Collections.unmodifiableList(
                new ArrayList<>(items.values())
        );
    }
}

