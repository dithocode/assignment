package io.ditho.assignment.presenter.editcontact;

import io.ditho.assignment.model.repository.RepositoryProvider;
import io.ditho.assignment.model.repository.entity.ContactEntity;
import io.ditho.assignment.model.rest.ApiProvider;
import io.ditho.assignment.presenter.BaseListPresenter;
import io.ditho.assignment.view.editcontact.EditContactListView;

public interface EditContactListPresenter extends BaseListPresenter<EditContactListView, ApiProvider, RepositoryProvider> {

    void saveData(ContactEntity contactEntity);
}

