package io.ditho.assignment.presenter.merge;

import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import io.ditho.assignment.common.ModelConverter;
import io.ditho.assignment.model.repository.DefaultRepository;
import io.ditho.assignment.model.repository.RepositoryProvider;
import io.ditho.assignment.model.repository.dao.MergeDao;
import io.ditho.assignment.model.repository.dao.ContactDao;
import io.ditho.assignment.model.repository.entity.ContactEntity;
import io.ditho.assignment.model.repository.entity.MergeEntity;
import io.ditho.assignment.model.rest.ApiProvider;
import io.ditho.assignment.model.rest.ContactApi;
import io.ditho.assignment.model.rest.model.Contact;
import io.ditho.assignment.model.rest.model.ContactResponse;
import io.ditho.assignment.view.merge.MergedListView;

public class MergedListPresenterImpl implements MergedListPresenter {

    private MergedListView view;
    private ContactApi contactApi;
    private DefaultRepository defaultRepository;

    private List<MergeEntity> contactList = new ArrayList<>();
    private MergeDao mergedDao;
    private RepositoryProvider repositoryProvider;
    private ContactDao contactDao;

    private Response.Listener<ContactResponse> successListener = new Response.Listener<ContactResponse>() {
        @Override
        public void onResponse(ContactResponse response) {
            ContactResponse.Data data = response.getData();
            if (data != null) {
                List<Contact> result = data.getResults();
                if (result.size() > 0) {
                    Executors.newSingleThreadExecutor().submit(new UpdateContactEntityWorker(result));
                } else {
                    view.finishLoad();
                    view.displayMessage("Info", "No Data Available");
                }
            } else {
                view.finishLoad();
                view.displayMessage("Info", "No Data Available");
            }
        }
    };

    private Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            view.finishLoad();
            view.displayMessage("Load Fail", error.getCause().getMessage());
        }
    };


    @Override
    public void init(MergedListView view, ApiProvider apiProvider, RepositoryProvider repositoryProvider) {
        this.view = view;
        this.contactApi = apiProvider.getContactApi();
        this.repositoryProvider = repositoryProvider;

        defaultRepository = repositoryProvider.getDefaultRepository();
        mergedDao = defaultRepository.getMergedDao();
        contactDao = defaultRepository.getContactDao();
    }

    @Override
    public void deinit() {
        contactDao = null;
        mergedDao = null;
        defaultRepository = null;
    }

    @Override
    public void attached() { }

    @Override
    public void detached() { }

    @Override
    public void start() { fetchData(); }

    @Override
    public void stop() { cancelAnyFetchData(); }

    @Override
    public void pause() { }

    @Override
    public void resume() { }

    @Override
    public void fetchData() {
        view.startLoad();

        contactApi.getContactList(
                getClass().getName(),
                successListener,
                errorListener);
    }

    @Override
    public void fetchMoreData() { }

    @Override
    public void cancelAnyFetchData() {
        contactApi.cancelAll(getClass().getName());
    }

    private class UpdateContactEntityWorker implements Runnable {

        private List<Contact> listData = new ArrayList<>();

        public UpdateContactEntityWorker(List<Contact> listData) {
            this.listData = listData;
        }

        @Override
        public void run() {
        }

    }

}
