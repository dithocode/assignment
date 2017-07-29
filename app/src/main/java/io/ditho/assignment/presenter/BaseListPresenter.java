package io.ditho.assignment.presenter;

import io.ditho.assignment.model.repository.RepositoryProvider;
import io.ditho.assignment.model.rest.ApiProvider;
import io.ditho.assignment.view.BaseView;

public interface BaseListPresenter<VW extends BaseView, RA extends ApiProvider, RS extends RepositoryProvider>
        extends BasePresenter<VW, RA, RS> {

    void fetchMoreData();

}
