package io.ditho.assignment.presenter.merge;

import java.util.List;
import java.util.concurrent.Executors;

import io.ditho.assignment.model.repository.DefaultRepository;
import io.ditho.assignment.model.repository.RepositoryProvider;
import io.ditho.assignment.model.repository.dao.ContactDao;
import io.ditho.assignment.model.repository.entity.ContactEntity;
import io.ditho.assignment.model.rest.ApiProvider;
import io.ditho.assignment.view.merge.LinkedContactListView;

public class LinkedContactListPresenterImpl implements LinkedContactListPresenter {

    private LinkedContactListView view;
    private DefaultRepository defaultRepository;
    private RepositoryProvider repositoryProvider;

    private ContactDao contactDao;

    @Override
    public void init(LinkedContactListView view, ApiProvider apiProvider, RepositoryProvider repositoryProvider) {
        this.view = view;
        this.repositoryProvider = repositoryProvider;

        defaultRepository = repositoryProvider.getDefaultRepository();
        contactDao = defaultRepository.getContactDao();
    }

    @Override
    public void deinit() {
        contactDao = null;
        defaultRepository = null;
    }

    @Override
    public void attached() { }

    @Override
    public void detached() { }

    @Override
    public void start() {  }

    @Override
    public void stop() {  }

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
    public void fetchMoreData() { }

    @Override
    public void cancelAnyFetchData() {
    }

    void updateViewModel(String parentId) {
        List<ContactEntity> queryResult = contactDao.getLinkedContact(parentId);
        view.updateModel(queryResult);
        view.finishLoad();
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
