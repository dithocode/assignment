package io.ditho.assignment.view.component;

public interface BaseListPresenter<T, RA, RS> extends BasePresenter<T, RA, RS> {

    void fetchMoreData();

}
