package io.ditho.assignment.presenter.contact;

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
import io.ditho.assignment.model.repository.dao.ContactDao;
import io.ditho.assignment.model.repository.dao.MergeContactDao;
import io.ditho.assignment.model.repository.dao.MergeDao;
import io.ditho.assignment.model.repository.entity.ContactEntity;
import io.ditho.assignment.model.repository.entity.MergeContactEntity;
import io.ditho.assignment.model.repository.entity.MergeEntity;
import io.ditho.assignment.model.rest.ApiProvider;
import io.ditho.assignment.model.rest.ContactApi;
import io.ditho.assignment.model.rest.model.Contact;
import io.ditho.assignment.model.rest.model.ContactResponse;
import io.ditho.assignment.view.contact.ContactListView;

public class ContactListPresenterImpl implements ContactListPresenter {

    private ContactListView view;
    private ContactApi contactApi;

    private RepositoryProvider repositoryProvider;
    private DefaultRepository defaultRepository;
    private ContactDao contactDao;
    private MergeDao mergeDao;
    private MergeContactDao mergeContactDao;

    private Response.Listener<ContactResponse> successListener = new Response.Listener<ContactResponse>() {
        @Override
        public void onResponse(ContactResponse response) {
            ContactResponse.Data data = response.getData();
            if (data != null) {
                List<Contact> result = data.getResults();
                if (result.size() > 0) {
                    Executors.newSingleThreadExecutor().submit(
                        new UpdateContactEntityWorker(result));
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
    public void init(ContactListView view, ApiProvider apiProvider, RepositoryProvider repositoryProvider) {
        this.view = view;
        this.contactApi = apiProvider.getContactApi();
        this.repositoryProvider = repositoryProvider;

        defaultRepository = repositoryProvider.getDefaultRepository();
        contactDao = defaultRepository.getContactDao();
        mergeDao = defaultRepository.getMergedDao();
        mergeContactDao = defaultRepository.getMergeContactDao();
    }

    @Override
    public void deinit() {
        contactDao = null;
        mergeDao = null;
        mergeContactDao = null;
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
    public void stop() {  }

    @Override
    public void pause() {
        cancelAnyFetchData();
    }

    @Override
    public void resume() {
        fetchData();

    }

    @Override
    public void fetchData() {
        view.startLoad();
        contactApi.getContactList(
                getClass().getName(),
                successListener,
                errorListener);
    }

    @Override
    public void fetchMoreData() {

    }

    @Override
    public void cancelAnyFetchData() {
        contactApi.cancelAll(getClass().getName());
    }

    @Override
    public void mergeContact(List<ContactEntity> contactList) {
        view.startLoad();
        Executors.newSingleThreadExecutor().submit(
                new MergeContactWorker(contactList));
    }

    void updateContactEntity(List<Contact> listData) {
        int listSize = listData.size();
        List<ContactEntity> newRecordList = new ArrayList<>();

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


    void mergeContactEntity(List<ContactEntity> listData) {
        int listSize = listData.size();
        if (listData.size() > 0) {
            ContactEntity contact = null;

            // check if there any merged entity already, and get the very first occurrence.
            // since currently it no support link merge contact with merged contact
            // the rest merged contact (if any) will be ignored
            for (int counter = 0; counter < listSize; counter++) {
                ContactEntity temp = listData.get(counter);
                if (temp.getLinkCount() > 0) {
                    contact = temp;
                    break;
                }
            }

            // if no merged entity found
            if (contact == null) {
                contact = listData.get(0);
            }

            MergeEntity newMerge = new MergeEntity(contact);

            if (newMerge.getLinkCount() == 0) {
                newMerge.setId(String.valueOf(System.currentTimeMillis()));
            }

            for (int counter = 0; counter < listSize; counter++) {
                contact = listData.get(counter);
                if (TextUtils.isEmpty(newMerge.getFirstName()) &&
                        !TextUtils.isEmpty(contact.getFirstName())) {
                    newMerge.setFirstName(contact.getFirstName());
                }
                if (TextUtils.isEmpty(newMerge.getMiddleName()) &&
                        !TextUtils.isEmpty(contact.getMiddleName())) {
                    newMerge.setMiddleName(contact.getMiddleName());
                }
                if (TextUtils.isEmpty(newMerge.getLastName()) &&
                        !TextUtils.isEmpty(contact.getLastName())) {
                    newMerge.setLastName(contact.getLastName());
                }
                if (TextUtils.isEmpty(newMerge.getFullName()) &&
                        !TextUtils.isEmpty(contact.getFullName())) {
                    newMerge.setFullName(contact.getFullName());
                }
                if (TextUtils.isEmpty(newMerge.getEmail()) &&
                        !TextUtils.isEmpty(contact.getEmail())) {
                    newMerge.setEmail(contact.getEmail());
                }
                if (TextUtils.isEmpty(newMerge.getPhone()) &&
                        !TextUtils.isEmpty(contact.getPhone())) {
                    newMerge.setPhone(contact.getPhone());
                }
                if (TextUtils.isEmpty(newMerge.getMobile()) &&
                        !TextUtils.isEmpty(contact.getMobile())) {
                    newMerge.setMobile(contact.getMobile());
                }
                if (TextUtils.isEmpty(newMerge.getBusinessEmail()) &&
                        !TextUtils.isEmpty(contact.getBusinessEmail())) {
                    newMerge.setBusinessEmail(contact.getBusinessEmail());
                }
                if (TextUtils.isEmpty(newMerge.getBusinessPhone()) &&
                        !TextUtils.isEmpty(contact.getBusinessPhone())) {
                    newMerge.setBusinessPhone(contact.getBusinessPhone());
                }
                if (TextUtils.isEmpty(newMerge.getBusinessMobile()) &&
                        !TextUtils.isEmpty(contact.getBusinessMobile())) {
                    newMerge.setBusinessMobile(contact.getBusinessMobile());
                }
                if (TextUtils.isEmpty(newMerge.getJobTitleDescription()) &&
                        !TextUtils.isEmpty(contact.getJobTitleDescription())) {
                    newMerge.setJobTitleDescription(contact.getJobTitleDescription());
                }
                if (TextUtils.isEmpty(newMerge.getNotes()) &&
                        !TextUtils.isEmpty(contact.getNotes())) {
                    newMerge.setNotes(contact.getNotes());
                }
                if (TextUtils.isEmpty(newMerge.getPictureThumbnailUrl()) &&
                        !TextUtils.isEmpty(contact.getPictureThumbnailUrl())) {
                    newMerge.setPictureThumbnailUrl(contact.getPictureThumbnailUrl());
                }
            }

            // insert newly/updated merge entity
            mergeDao.insertAll(newMerge);

            // insert link relation between merge entity and contact entity
            ArrayList<MergeContactEntity> mergeContactList = new ArrayList<>();
            for (int counter = 0; counter < listSize; counter++) {
                contact = listData.get(counter);
                MergeContactEntity newMergeContact = new MergeContactEntity();
                newMergeContact.setContactId(contact.getId());
                newMergeContact.setMergeId(newMerge.getId());
                // do not insert/ignore merge entity
                // since currently it no support link merge entity with merged entity
                if (contact.getLinkCount() == 0) {
                    mergeContactList.add(newMergeContact);
                }
            }

            mergeContactDao.insertAll(mergeContactList);
        }
    }

    void updateViewModel() {
        List<MergeEntity> queryResult = mergeDao.getMergeContact();
        List<ContactEntity> results = new ArrayList<>();
        int queryResultSize = queryResult.size();

        for (int counter = 0; counter < queryResultSize; counter++) {
            MergeEntity mergeEntity = queryResult.get(counter);
            int linkCount = mergeContactDao.countLinked(mergeEntity.getId());
            mergeEntity.setLinkCount(linkCount);
            results.add(mergeEntity);
        }

        view.updateModel(results);
        view.finishLoad();
    }

    private class MergeContactWorker implements Runnable {

        private List<ContactEntity> listData = new ArrayList<>();

        public MergeContactWorker(List<ContactEntity> listData) {
            this.listData = listData;
        }

        @Override
        public void run() {
            mergeContactEntity(listData);
            updateViewModel();
        }

    }

    private class UpdateContactEntityWorker implements Runnable {

        private List<Contact> listData = new ArrayList<>();

        public UpdateContactEntityWorker(List<Contact> listData) {
            this.listData = listData;
        }

        @Override
        public void run() {
            updateContactEntity(listData);
            updateViewModel();
        }
    }
}
