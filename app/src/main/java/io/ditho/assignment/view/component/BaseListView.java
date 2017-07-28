package io.ditho.assignment.view.component;

public interface BaseListView<T> extends BaseView<T> {

    void startLoadMore();
    void finishLoadMore();

    void appendModel(T model);

}
