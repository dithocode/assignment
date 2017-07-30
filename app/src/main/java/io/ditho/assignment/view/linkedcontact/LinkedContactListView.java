package io.ditho.assignment.view.linkedcontact;

import java.util.List;

import io.ditho.assignment.model.repository.entity.ContactEntity;
import io.ditho.assignment.view.BaseListView;

public interface LinkedContactListView extends BaseListView<List<ContactEntity>> {
    String getRootId();
    String getParentId();
}
