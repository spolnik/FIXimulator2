package org.nprogramming.fiximulator2.data;

import org.nprogramming.fiximulator2.api.Repository;
import org.nprogramming.fiximulator2.domain.ItemWithId;

import java.util.*;

public class InMemoryRepository<TItem extends ItemWithId>
        implements Repository<TItem> {

    protected final Map<String, TItem> items = new HashMap<>();

    @Override
    public void save(TItem item) {
        items.put(item.id(), item);
    }

    @Override
    public TItem get(String id) {

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

