package io.ditho.assignment.view.editcontact;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.ditho.assignment.R;
import io.ditho.assignment.common.MouseEventUtils;
import io.ditho.assignment.model.repository.RepositoryProvider;
import io.ditho.assignment.model.repository.entity.ContactEntity;
import io.ditho.assignment.model.rest.ApiProvider;
import io.ditho.assignment.presenter.editcontact.EditContactListPresenter;
import io.ditho.assignment.presenter.editcontact.EditContactListPresenterImpl;
import io.ditho.assignment.view.adapter.EditContactListAdapter;
import io.ditho.assignment.view.main.MainActivity;

public class EditContactListFragment extends Fragment implements EditContactListView {

    public static final String ARG_ROOT_ID = "ARG_ROOT_ID";
    public static final String ARG_PARENT_ID = "ARG_PARENT_ID";

    private EditContactListPresenter presenter;
    private EditContactListAdapter listAdapter;
    private String parentId;
    private String rootId;

    private ContactEntity rootEntity;
    private ContactEntity parentEntity;

    @Nullable
    @Bind(R.id.listview_contact_field)
    XRecyclerView listView;

    public EditContactListFragment() {
        presenter = new EditContactListPresenterImpl();
    }

    public static EditContactListFragment newInstance(String rootId, String parentId) {
        EditContactListFragment fragment = new EditContactListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ROOT_ID, rootId);
        args.putString(ARG_PARENT_ID, parentId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            rootId = getArguments().getString(ARG_ROOT_ID);
            parentId = getArguments().getString(ARG_PARENT_ID);
        }
        setHasOptionsMenu(true);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_contact, container, false);

        ButterKnife.bind(this, view);

        setupRecycleView();
        setupToolbar();

        presenter.init(
                this,
                ApiProvider.getInstance(getActivity()),
                RepositoryProvider.getInstance(getActivity()));

        return view;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.editcontact, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean retValue = false;
        if (item.getItemId() == R.id.action_save) {
            HashMap<String, String> selectedResult = listAdapter.getSelectedData();
            transformAndSave(selectedResult);
        } else {
            retValue = super.onOptionsItemSelected(item);
        }

        return retValue;
    }

    private void setupRecycleView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        listAdapter = new EditContactListAdapter(getActivity());

        listView.setLayoutManager(linearLayoutManager);
        listView.setAdapter(listAdapter);
        listView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        listView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);

        listView.setPullRefreshEnabled(false);
        listView.setLoadingMoreEnabled(false);

    }

    private void setupToolbar() {
        MainActivity theActivity = (MainActivity) getActivity();
        if (theActivity != null) {
            Toolbar toolbar = theActivity.getMainToolbar();
            ActionBar theActionBar = theActivity.getSupportActionBar();
            if (theActionBar != null) {
                theActionBar.setDisplayHomeAsUpEnabled(true);
            }
            if (toolbar != null) {
                toolbar.setTitle("Edit Contact");
                toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        exitFragment();
                    }
                });
            }
        }
    }

    private void revertToolbar() {
        MainActivity theActivity = (MainActivity) getActivity();
        ActionBar theActionBar = theActivity.getSupportActionBar();
        if (theActionBar != null) {
            theActionBar.setDisplayHomeAsUpEnabled(false);
        }
        theActivity.setupToolbar();
    }

    private void exitFragment() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                getActivity().onBackPressed();
            }
        });
    }

    @Override
    public void onDestroyView() {
        presenter.deinit();
        ButterKnife.unbind(this);
        super.onDestroyView();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        presenter.attached();
    }

    @Override
    public void onDetach() {
        presenter.detached();
        super.onDetach();
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.start();
    }

    @Override
    public void onStop() {
        presenter.stop();
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.resume();
    }

    @Override
    public void onPause() {
        presenter.pause();
        super.onPause();
    }

    @Override
    public void startLoad() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                listView.setRefreshing(true);

                // Force to show refresh indicator
                // need to research later on Fragment Replace on completely updating layout
                MouseEventUtils.generatePullTouch(listView);
            }
        });
    }

    @Override
    public void finishLoad() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                listView.refreshComplete();
            }
        });
    }

    @Override
    public void startLoadMore() {
    }

    @Override
    public void finishLoadMore() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                listView.loadMoreComplete();
            }
        });
    }

    @Override
    public void updateModel(List<ContactEntity> model) {
        if (listAdapter != null) {
            final List<ContactEntity> lModel = model;
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    transformAndApplyModel(lModel);
                }
            });
        }
    }

    @Override
    public void appendModel(List<ContactEntity> model) {
        if (listAdapter != null) {
            final List<ContactEntity> lModel = model;
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    transformAndApplyModel(lModel);
                }
            });
        }
    }

    @Override
    public boolean goBack() {
        revertToolbar();
        return false;
    }

    @Override
    public void displayMessage(String title, String message) {
        final String titleParam = title;
        final String msgParam = message;

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                final AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                        .setTitle(titleParam)
                        .setMessage(msgParam)
                        .setCancelable(false)
                        .create();
                alertDialog.setButton(
                        AlertDialog.BUTTON_POSITIVE,
                        "OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                alertDialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
        });
    }

    @Override
    public String getRootId() {
        return rootId;
    }

    @Override
    public String getParentId() {
        return parentId;
    }

    @Override
    public void updateRootModel(ContactEntity rootModel) {
        this.rootEntity = rootModel;
    }

    @Override
    public void updateParentModel(ContactEntity parentModel) {
        this.parentEntity = parentModel;
    }

    void transformAndSave(HashMap<String, String> selectedResult) {
        String value = "";

        value = selectedResult.get("First Name");
        if (!TextUtils.isEmpty(value)) parentEntity.setFirstName(value);

        value = selectedResult.get("Middle Name");
        if (!TextUtils.isEmpty(value)) parentEntity.setMiddleName(value);

        value = selectedResult.get("Last Name");
        if (!TextUtils.isEmpty(value)) parentEntity.setLastName(value);

        value = selectedResult.get("Full Name");
        if (!TextUtils.isEmpty(value)) parentEntity.setFullName(value);

        value = selectedResult.get("Gender");
        if (!TextUtils.isEmpty(value)) parentEntity.setGender(value);

        value = selectedResult.get("Email");
        if (!TextUtils.isEmpty(value)) parentEntity.setEmail(value);

        value = selectedResult.get("Phone");
        if (!TextUtils.isEmpty(value)) parentEntity.setPhone(value);

        value = selectedResult.get("Mobile");
        if (!TextUtils.isEmpty(value)) parentEntity.setMobile(value);

        value = selectedResult.get("Business Email");
        if (!TextUtils.isEmpty(value)) parentEntity.setBusinessEmail(value);

        value = selectedResult.get("Business Phone");
        if (!TextUtils.isEmpty(value)) parentEntity.setBusinessPhone(value);

        value = selectedResult.get("Business Mobile");
        if (!TextUtils.isEmpty(value)) parentEntity.setBusinessMobile(value);

        value = selectedResult.get("Job Title");
        if (!TextUtils.isEmpty(value)) parentEntity.setJobTitleDescription(value);

        value = selectedResult.get("Notes");
        if (!TextUtils.isEmpty(value)) parentEntity.setNotes(value);

        value = selectedResult.get("Picture Url");
        if (!TextUtils.isEmpty(value)) parentEntity.setPictureThumbnailUrl(value);

        presenter.saveData(parentEntity);
    }

    void transformAndApplyModel(List<ContactEntity> model) {
        ArrayList<String> listTitle = new ArrayList<>();

        listTitle.add("First Name");
        listTitle.add("Middle Name");
        listTitle.add("Last Name");
        listTitle.add("Full Name");
        listTitle.add("Gender");
        listTitle.add("Email");
        listTitle.add("Phone");
        listTitle.add("Mobile");
        listTitle.add("Business Email");
        listTitle.add("Business Phone");
        listTitle.add("Business Mobile");
        listTitle.add("Job Title");
        listTitle.add("Notes");
        listTitle.add("Picture Url");

        int modelSize = model.size();

        ArrayList<ArrayList<String>> fieldValueList = new ArrayList<>();
        ArrayList<String> fieldFirstNameValueList = new ArrayList<>();
        ArrayList<String> fieldMidleNameValueList = new ArrayList<>();
        ArrayList<String> fieldLastNameValueList = new ArrayList<>();
        ArrayList<String> fieldFullNameValueList = new ArrayList<>();
        ArrayList<String> fieldGenderValueList = new ArrayList<>();
        ArrayList<String> fieldEmailValueList = new ArrayList<>();
        ArrayList<String> fieldPhoneValueList = new ArrayList<>();
        ArrayList<String> fieldMobileValueList = new ArrayList<>();
        ArrayList<String> fieldBusinessEmailValueList = new ArrayList<>();
        ArrayList<String> fieldBusinessPhoneValueList = new ArrayList<>();
        ArrayList<String> fieldBusinessMobileValueList = new ArrayList<>();
        ArrayList<String> fieldJobTitleValueList = new ArrayList<>();
        ArrayList<String> fieldNotesValueList = new ArrayList<>();
        ArrayList<String> fieldPictureUrlValueList = new ArrayList<>();

        for (int counter = 0; counter < modelSize; counter++) {
            ContactEntity contactEntity = model.get(counter);
            fieldFirstNameValueList.add(contactEntity.getFirstName());
            fieldMidleNameValueList.add(contactEntity.getMiddleName());
            fieldLastNameValueList.add(contactEntity.getLastName());
            fieldFullNameValueList.add(contactEntity.getFullName());
            fieldGenderValueList.add(contactEntity.getGender());
            fieldEmailValueList.add(contactEntity.getEmail());
            fieldPhoneValueList.add(contactEntity.getPhone());
            fieldMobileValueList.add(contactEntity.getMobile());
            fieldBusinessEmailValueList.add(contactEntity.getBusinessEmail());
            fieldBusinessPhoneValueList.add(contactEntity.getBusinessPhone());
            fieldBusinessMobileValueList.add(contactEntity.getBusinessMobile());
            fieldJobTitleValueList.add(contactEntity.getJobTitleDescription());
            fieldNotesValueList.add(contactEntity.getNotes());
            fieldPictureUrlValueList.add(contactEntity.getPictureThumbnailUrl());
        }

        fieldValueList.add(fieldFirstNameValueList);
        fieldValueList.add(fieldMidleNameValueList);
        fieldValueList.add(fieldLastNameValueList);
        fieldValueList.add(fieldFullNameValueList);
        fieldValueList.add(fieldGenderValueList);
        fieldValueList.add(fieldEmailValueList);
        fieldValueList.add(fieldPhoneValueList);
        fieldValueList.add(fieldMobileValueList);
        fieldValueList.add(fieldBusinessEmailValueList);
        fieldValueList.add(fieldBusinessPhoneValueList);
        fieldValueList.add(fieldBusinessMobileValueList);
        fieldValueList.add(fieldJobTitleValueList);
        fieldValueList.add(fieldNotesValueList);
        fieldValueList.add(fieldPictureUrlValueList);

        listAdapter.setListData(listTitle, fieldValueList);
        listAdapter.notifyDataSetChanged();
    }
}
