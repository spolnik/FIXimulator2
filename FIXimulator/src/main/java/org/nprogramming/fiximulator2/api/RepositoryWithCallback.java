package org.nprogramming.fiximulator2.api;

import org.nprogramming.fiximulator2.domain.ItemWithId;

import java.util.List;

public interface RepositoryWithCallback<TItem extends ItemWithId> {

    void save(TItem item);

    void addCallback(NotifyApi callback);

    TItem get(String id);

    void update(String id);

    List<TItem> getAll();
}

