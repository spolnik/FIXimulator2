package org.nprogramming.fiximulator2.data;

import org.nprogramming.fiximulator2.api.ItemWithId;
import org.nprogramming.fiximulator2.api.NotifyApi;
import org.nprogramming.fiximulator2.api.RepositoryWithCallback;

import java.util.*;

public class InMemoryRepository<TItem extends ItemWithId> implements RepositoryWithCallback<TItem> {

    protected final Map<String, TItem> items = new HashMap<>();
    protected NotifyApi callback = null;

    @Override
    public void save(TItem item) {
        items.put(item.id(), item);

        if (callback != null)
            callback.added(item.id());
    }

    @Override
    public void addCallback(NotifyApi callback) {
        this.callback = callback;
    }

    @Override
    public TItem get(String id) {

        if (items.containsKey(id)) {
            return items.get(id);
        }

        return null;
    }

    @Override
    public void update(String id) {
        callback.update(id);
    }

    @Override
    public List<TItem> getAll() {
        return Collections.unmodifiableList(
                new ArrayList<>(items.values())
        );
    }
}
