package io.ditho.assignment.view.component;

public interface BasePresenter<T, RA, RS> {
    void init(T view, RA apiProvider, RS repositoryProvider);
    void deinit();

    void attached();
    void detached();

    void start();
    void stop();
    void pause();
    void resume();

    void fetchData();
    void cancelAnyFetchData();
}
