package io.ditho.assignment.view.merge;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.ditho.assignment.R;
import io.ditho.assignment.common.MouseEventUtils;
import io.ditho.assignment.presenter.merge.LinkedContactListPresenter;
import io.ditho.assignment.presenter.merge.LinkedContactListPresenterImpl;
import io.ditho.assignment.model.repository.RepositoryProvider;
import io.ditho.assignment.model.repository.entity.ContactEntity;
import io.ditho.assignment.model.rest.ApiProvider;
import io.ditho.assignment.view.adapter.ContactListAdapter;
import io.ditho.assignment.view.contact.ContactListFragment;
import io.ditho.assignment.view.main.MainActivity;

public class LinkedContactListFragment extends Fragment implements LinkedContactListView {

    public static final String ARG_ROOT_ID = "ARG_ROOT_ID";
    public static final String ARG_PARENT_ID = "ARG_PARENT_ID";

    private LinkedContactListPresenter presenter;
    private ContactListAdapter listAdapter;
    private String parentId;
    private String rootId;

    @Nullable
    @Bind(R.id.listview_contact)
    XRecyclerView listView;

    public LinkedContactListFragment() {
        presenter = new LinkedContactListPresenterImpl();
    }

    public static LinkedContactListFragment newInstance(String rootId, String parentId) {
        LinkedContactListFragment fragment = new LinkedContactListFragment();
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
        setHasOptionsMenu(false);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_marge, container, false);

        ButterKnife.bind(this, view);

        setupRecycleView();
        setupToolbar();
        presenter.init(
                this,
                ApiProvider.getInstance(getActivity()),
                RepositoryProvider.getInstance(getActivity()));

        return view;
    }


    private void setupRecycleView() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        listAdapter = new ContactListAdapter(getActivity(), null);
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
                toolbar.setTitle("Linked Contact");
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
                    listAdapter.clear();
                    listAdapter.addAll(lModel);
                    listAdapter.notifyDataSetChanged();
                }
            });
        }
    }

    @Override
    public void appendModel(final List<ContactEntity> model) {
        if (listAdapter != null) {
            final List<ContactEntity> lModel = model;
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    listAdapter.addAll(lModel);
                    listAdapter.notifyDataSetChanged();
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

    public void showContactList() {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                ContactListFragment fragment = ContactListFragment.newInstance();
                MainActivity theActivity = (MainActivity) getActivity();
                theActivity.showFragment(fragment);
            }
        }, 500);
    }
}
