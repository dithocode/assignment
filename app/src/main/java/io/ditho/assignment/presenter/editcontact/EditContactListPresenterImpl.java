package io.ditho.assignment.presenter.editcontact;

import java.util.List;
import java.util.concurrent.Executors;

import io.ditho.assignment.model.repository.DefaultRepository;
import io.ditho.assignment.model.repository.RepositoryProvider;
import io.ditho.assignment.model.repository.dao.ContactDao;
import io.ditho.assignment.model.repository.dao.MergeDao;
import io.ditho.assignment.model.repository.entity.ContactEntity;
import io.ditho.assignment.model.repository.entity.MergeEntity;
import io.ditho.assignment.model.rest.ApiProvider;
import io.ditho.assignment.view.editcontact.EditContactListView;

public class EditContactListPresenterImpl implements EditContactListPresenter{

    private EditContactListView view;
    private DefaultRepository defaultRepository;
    private RepositoryProvider repositoryProvider;

    private ContactDao contactDao;
    private MergeDao mergeDao;
    @Override
    public void init(EditContactListView view, ApiProvider apiProvider, RepositoryProvider repositoryProvider) {
        this.view = view;
        this.repositoryProvider = repositoryProvider;

        defaultRepository = repositoryProvider.getDefaultRepository();
        contactDao = defaultRepository.getContactDao();
        mergeDao = defaultRepository.getMergedDao();

    }

    @Override
    public void deinit() {
        contactDao = null;
        defaultRepository = null;
    }

    @Override
    public void attached() {

    }

    @Override
    public void detached() {

    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void pause() { cancelAnyFetchData(); }

    @Override
    public void resume() { fetchData(); }

    @Override
    public void fetchData() {
        view.startLoad();
        Executors.newSingleThreadExecutor().submit(
                new LoadContactEntityWorker(view.getParentId())
        );
    }

    @Override
    public void cancelAnyFetchData() {

    }

    @Override
    public void fetchMoreData() {

    }

    @Override
    public void saveData(ContactEntity contactEntity) {
        view.startLoad();
        if (contactEntity != null) {
            MergeEntity mergeEntity = new MergeEntity(contactEntity);
            Executors.newSingleThreadExecutor().submit(new SaveContactEntityWorker(mergeEntity));
        }
    }

    void updateMergeEntity(MergeEntity mergeEntity) {
        mergeDao.update(mergeEntity);

        MergeEntity parentModel = mergeDao.getById(mergeEntity.getId());
        view.updateParentModel(parentModel);
        view.updateRootModel(parentModel);
        view.finishLoad();

        view.displayMessage("Info", "Contact Field Updated!");
    }

    void updateViewModel(String parentId) {
        List<ContactEntity> queryResult = contactDao.getLinkedContact(parentId);
        MergeEntity parentModel = mergeDao.getById(parentId);
        view.updateParentModel(parentModel);
        view.updateRootModel(parentModel);
        view.updateModel(queryResult);
        view.finishLoad();
    }

    private class SaveContactEntityWorker implements Runnable {

        private MergeEntity mergeEntity;

        public SaveContactEntityWorker(MergeEntity mergeEntity) {
            this.mergeEntity = mergeEntity;
        }

        @Override
        public void run() {
            updateMergeEntity(mergeEntity);
        }
    }

    private class LoadContactEntityWorker implements Runnable {

        private String parentId;

        public LoadContactEntityWorker(String parentId) {
            this.parentId = parentId;
        }

        @Override
        public void run() {
            updateViewModel(parentId);
        }
    }
}
