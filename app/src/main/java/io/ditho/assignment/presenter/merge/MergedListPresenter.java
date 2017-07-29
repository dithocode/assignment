package io.ditho.assignment.presenter.merge;

import io.ditho.assignment.model.repository.RepositoryProvider;
import io.ditho.assignment.model.rest.ApiProvider;
import io.ditho.assignment.presenter.BaseListPresenter;
import io.ditho.assignment.view.merge.MergedListView;

public interface MergedListPresenter extends BaseListPresenter<MergedListView, ApiProvider, RepositoryProvider> {
}
