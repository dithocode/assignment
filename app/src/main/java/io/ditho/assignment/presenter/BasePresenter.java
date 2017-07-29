package io.ditho.assignment.presenter;

import io.ditho.assignment.model.repository.RepositoryProvider;
import io.ditho.assignment.model.rest.ApiProvider;
import io.ditho.assignment.view.BaseView;

public interface BasePresenter<VW extends BaseView, RA extends ApiProvider, RS extends RepositoryProvider> {
    void init(VW view, RA apiProvider, RS repositoryProvider);
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
