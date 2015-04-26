package com.wordpress.nprogramming.oms.api;

import java.util.List;

public interface Repository<TItem extends ItemWithId> {

    void save(TItem item);

    TItem queryById(String id);

    List<TItem> getAll();
}

