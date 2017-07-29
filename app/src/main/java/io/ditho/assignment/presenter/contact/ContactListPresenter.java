package io.ditho.assignment.presenter.contact;

import io.ditho.assignment.model.repository.RepositoryProvider;
import io.ditho.assignment.model.rest.ApiProvider;
import io.ditho.assignment.presenter.BaseListPresenter;
import io.ditho.assignment.view.contact.ContactListView;

public interface ContactListPresenter extends BaseListPresenter<ContactListView, ApiProvider, RepositoryProvider> {
}
