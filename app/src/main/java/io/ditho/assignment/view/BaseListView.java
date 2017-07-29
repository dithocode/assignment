package io.ditho.assignment.view;

public interface BaseListView<T> extends BaseView<T> {

    void startLoadMore();
    void finishLoadMore();

    void appendModel(T model);

}
