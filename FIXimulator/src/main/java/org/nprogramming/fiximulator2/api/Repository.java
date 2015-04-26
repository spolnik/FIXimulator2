package org.nprogramming.fiximulator2.api;

import com.wordpress.nprogramming.oms.api.ItemWithId;

import java.util.List;

public interface Repository<TItem extends ItemWithId> {

    void save(TItem item);

    TItem get(String id);

    List<TItem> getAll();
}

