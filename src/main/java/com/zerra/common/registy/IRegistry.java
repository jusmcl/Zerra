package com.zerra.common.registy;

import java.util.List;

public interface IRegistry<T> {

    List<IEntryHolder<T>> getEntryHolders();

    List<T> getEntries();

}
