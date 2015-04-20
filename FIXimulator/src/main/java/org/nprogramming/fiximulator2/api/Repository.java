package org.nprogramming.fiximulator2.api;

import org.nprogramming.fiximulator2.domain.ItemWithId;

import java.util.List;

public interface Repository<TItem extends ItemWithId> {

    void save(TItem item);

    TItem get(String id);

    List<TItem> getAll();
}

