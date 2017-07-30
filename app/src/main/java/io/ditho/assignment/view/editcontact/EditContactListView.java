package io.ditho.assignment.view.editcontact;

import java.util.List;

import io.ditho.assignment.model.repository.entity.ContactEntity;
import io.ditho.assignment.view.BaseListView;

public interface EditContactListView extends BaseListView<List<ContactEntity>> {
    String getRootId();
    String getParentId();

    void updateRootModel(ContactEntity rootModel);
    void updateParentModel(ContactEntity parentModel);

}
