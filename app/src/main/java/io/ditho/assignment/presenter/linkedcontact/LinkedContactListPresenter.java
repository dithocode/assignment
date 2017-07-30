package io.ditho.assignment.presenter.linkedcontact;

import io.ditho.assignment.model.repository.RepositoryProvider;
import io.ditho.assignment.model.rest.ApiProvider;
import io.ditho.assignment.presenter.BaseListPresenter;
import io.ditho.assignment.view.linkedcontact.LinkedContactListView;

public interface LinkedContactListPresenter extends BaseListPresenter<LinkedContactListView, ApiProvider, RepositoryProvider> {
}
