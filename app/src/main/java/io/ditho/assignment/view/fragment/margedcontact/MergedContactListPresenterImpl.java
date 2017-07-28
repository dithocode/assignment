package io.ditho.assignment.view.fragment.margedcontact;

import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import io.ditho.assignment.common.ModelConverter;
import io.ditho.assignment.repository.DefaultRepository;
import io.ditho.assignment.repository.RepositoryProvider;
import io.ditho.assignment.repository.dao.MergedContactDao;
import io.ditho.assignment.repository.dao.ContactDao;
import io.ditho.assignment.repository.entity.ContactEntity;
import io.ditho.assignment.repository.entity.MergedContactEntity;
import io.ditho.assignment.rest.ApiProvider;
import io.ditho.assignment.rest.ContactApi;
import io.ditho.assignment.rest.model.Contact;
import io.ditho.assignment.rest.model.ContactResponse;

public class MergedContactListPresenterImpl implements MergedContactListPresenter {

    private MergedContactListView view;
    private ContactApi contactApi;
    private DefaultRepository defaultRepository;

    private List<MergedContactEntity> contactList = new ArrayList<>();
    private MergedContactDao mergedContactDao;
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
    public void init(MergedContactListView view, ApiProvider apiProvider, RepositoryProvider repositoryProvider) {
        this.view = view;
        this.contactApi = apiProvider.getContactApi();
        this.repositoryProvider = repositoryProvider;

        defaultRepository = repositoryProvider.getDefaultRepository();
        mergedContactDao = defaultRepository.getMergedContactDao();
        contactDao = defaultRepository.getContactDao();
    }

    @Override
    public void deinit() {
        contactDao = null;
        mergedContactDao = null;
        defaultRepository = null;
    }

    @Override
    public void attached() { }

    @Override
    public void detached() { }

    @Override
    public void start() { fetchData(); }

    @Override
    public void stop() { }

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
            updateContactEntity();
            updateMergeContactEntity();
            updateViewModel();
        }

        void updateContactEntity() {
            int listSize = listData.size();
            List<ContactEntity> newRecordList = new ArrayList<>();

            contactDao.deleteAll();
            for (int counter = 0; counter < listSize; counter++) {
                ContactEntity newRecord = ModelConverter.from(listData.get(counter));
                if (TextUtils.isEmpty(newRecord.getId())) {
                    newRecord.setId(String.valueOf(System.currentTimeMillis()));
                }
                newRecordList.add(newRecord);
            }

            // insert / replace existing contact entity
            contactDao.insertAll(newRecordList);
        }

        void updateMergeContactEntity() {
            // clear merged record
            mergedContactDao.deleteAll();

            // find and group contact by account and first name
            List<ContactEntity> list = contactDao.getValidContact();
            int listSize = list.size();

            for (int counter = 0; counter < listSize; counter++) {
                ContactEntity contactEntity = list.get(counter);

                Log.i(getClass().getName(),
                        String.format("Checking Account=%s, FirstName=%s",
                                contactEntity.getAccount(),
                                contactEntity.getFirstName()));

                MergedContactEntity mergedContactEntity = new MergedContactEntity(contactEntity);

                // assign new contact id if existing contact doesn't have one
                if (TextUtils.isEmpty(mergedContactEntity.getId())) {
                    mergedContactEntity.setId(String.valueOf(System.currentTimeMillis()));
                }

                String[] arrCompleteName = contactEntity.getFirstName().split(" ");
                String onlyFirstNameCriteria = arrCompleteName[0] + "%";

                List<String> results;
                if (TextUtils.isEmpty(mergedContactEntity.getMiddleName())) {
                    // find and get fist valid middle name
                    results = contactDao.getMiddleName(
                            contactEntity.getAccount(),
                            onlyFirstNameCriteria,
                            contactEntity.getId());

                    if (results != null && results.size() > 0) {
                        mergedContactEntity.setMiddleName(results.get(0));
                    }
                }

                if (TextUtils.isEmpty(mergedContactEntity.getLastName())) {
                    // find and get fist valid last name
                    results = contactDao.getLastName(
                            contactEntity.getAccount(),
                            onlyFirstNameCriteria,
                            contactEntity.getId());

                    if (results != null && results.size() > 0) {
                        mergedContactEntity.setLastName(results.get(0));
                    }
                }

                if (TextUtils.isEmpty(mergedContactEntity.getFirstName())) {
                    // find and get fist valid full name
                    results = contactDao.getFullName(
                            contactEntity.getAccount(),
                            onlyFirstNameCriteria,
                            contactEntity.getId());

                    if (results != null && results.size() > 0) {
                        mergedContactEntity.setFullName(results.get(0));
                    }
                }

                if (TextUtils.isEmpty(mergedContactEntity.getEmail())) {
                    // find and get fist valid email
                    results = contactDao.getEmail(
                            contactEntity.getAccount(),
                            onlyFirstNameCriteria,
                            contactEntity.getId());

                    if (results != null && results.size() > 0) {
                        mergedContactEntity.setEmail(results.get(0));
                    }
                }

                if (TextUtils.isEmpty(mergedContactEntity.getMobile())) {
                    // find and get fist valid mobile
                    results = contactDao.getMobile(
                            contactEntity.getAccount(),
                            onlyFirstNameCriteria,
                            contactEntity.getId());

                    if (results != null && results.size() > 0) {
                        mergedContactEntity.setMobile(results.get(0));
                    }
                }

                if (TextUtils.isEmpty(mergedContactEntity.getPhone())) {
                    // find and get fist valid phone
                    results = contactDao.getPhone(
                            contactEntity.getAccount(),
                            onlyFirstNameCriteria,
                            contactEntity.getId());

                    if (results != null && results.size() > 0) {
                        mergedContactEntity.setPhone(results.get(0));
                    }
                }

                if (TextUtils.isEmpty(mergedContactEntity.getBusinessEmail())) {
                    // find and get fist valid business email
                    results = contactDao.getBusinessEmail(
                            contactEntity.getAccount(),
                            onlyFirstNameCriteria,
                            contactEntity.getId());

                    if (results != null && results.size() > 0) {
                        mergedContactEntity.setBusinessEmail(results.get(0));
                    }
                }

                if (TextUtils.isEmpty(mergedContactEntity.getBusinessMobile())) {
                    // find and get fist valid business mobile
                    results = contactDao.getBusinessMobile(
                            contactEntity.getAccount(),
                            onlyFirstNameCriteria,
                            contactEntity.getId());

                    if (results != null && results.size() > 0) {
                        mergedContactEntity.setBusinessMobile(results.get(0));
                    }
                }

                if (TextUtils.isEmpty(mergedContactEntity.getBusinessPhone())) {
                    // find and get fist valid business phone
                    results = contactDao.getBusinessPhone(
                            contactEntity.getAccount(),
                            onlyFirstNameCriteria,
                            contactEntity.getId());

                    if (results != null && results.size() > 0) {
                        mergedContactEntity.setBusinessPhone(results.get(0));
                    }
                }

                if (TextUtils.isEmpty(mergedContactEntity.getJobTitleDescription())) {
                    // find and get fist valid business phone
                    results = contactDao.getJobTitleDescription(
                            contactEntity.getAccount(),
                            onlyFirstNameCriteria,
                            contactEntity.getId());

                    if (results != null && results.size() > 0) {
                        mergedContactEntity.setJobTitleDescription(results.get(0));
                    }
                }

                if (TextUtils.isEmpty(mergedContactEntity.getNotes())) {
                    // find and get fist valid business phone
                    results = contactDao.getNotes(
                            contactEntity.getAccount(),
                            onlyFirstNameCriteria,
                            contactEntity.getId());

                    if (results != null && results.size() > 0) {
                        mergedContactEntity.setNotes(results.get(0));
                    }
                }

                // find and get fist valid picture url
                results = contactDao.getPictureThumbnailUrl(
                        contactEntity.getAccount(),
                        onlyFirstNameCriteria,
                        contactEntity.getId());

                if (results != null && results.size() > 0) {
                    mergedContactEntity.setPictureThumbnailUrl(results.get(0));
                }
                Log.i(getClass().getName(), mergedContactEntity.toString());

                // store merged data in local database
                mergedContactDao.insertAll(mergedContactEntity);
            }
        }

        void updateViewModel() {
            List<MergedContactEntity> queryResult = mergedContactDao.getAll();
            List<ContactEntity> results = new ArrayList<>();

            results.addAll(queryResult);

            view.updateModel(results);
            view.finishLoad();
        }

    }

}
