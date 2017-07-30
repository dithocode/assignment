package io.ditho.assignment.presenter.contact;

import java.util.List;

import io.ditho.assignment.model.repository.RepositoryProvider;
import io.ditho.assignment.model.repository.entity.ContactEntity;
import io.ditho.assignment.model.rest.ApiProvider;
import io.ditho.assignment.presenter.BaseListPresenter;
import io.ditho.assignment.view.contact.ContactListView;

public interface ContactListPresenter extends BaseListPresenter<ContactListView, ApiProvider, RepositoryProvider> {

    public void mergeContact(List<ContactEntity> contactList);
}
