package io.ditho.assignment.view.contact;

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
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.jcodecraeer.xrecyclerview.ArrowRefreshHeader;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.ditho.assignment.R;
import io.ditho.assignment.model.repository.RepositoryProvider;
import io.ditho.assignment.model.repository.entity.ContactEntity;
import io.ditho.assignment.model.rest.ApiProvider;
import io.ditho.assignment.presenter.contact.ContactListPresenterImpl;
import io.ditho.assignment.view.adapter.ContactListAdapter;
import io.ditho.assignment.view.main.MainActivity;

public class ContactListFragment extends Fragment implements ContactListView {

    private final ContactListPresenterImpl presenter;
    private ContactListAdapter listAdapter;

    @Nullable
    @Bind(R.id.listview_contact)
    XRecyclerView listView;

    public ContactListFragment() { presenter = new ContactListPresenterImpl(); }

    public static ContactListFragment newInstance() {
        ContactListFragment fragment = new ContactListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
        setHasOptionsMenu(false);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact, container, false);
        ButterKnife.bind(this, view);

        setupRecyvleView();

        presenter.init(
                this,
                ApiProvider.getInstance(getActivity()),
                RepositoryProvider.getInstance(getActivity()));

        return view;
    }

    private void setupRecyvleView() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        listAdapter = new ContactListAdapter(getActivity());
        listView.setLayoutManager(linearLayoutManager);
        listView.setAdapter(listAdapter);
        listView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        listView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);

        listView.setPullRefreshEnabled(true);
        listView.setLoadingMoreEnabled(false);
        listView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        presenter.fetchData();
                    }
                }, 1000);
            }

            @Override
            public void onLoadMore() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        presenter.fetchMoreData();
                    }
                }, 1000);

            }
        });

        final GestureDetector.OnGestureListener gastureDetectorListener =
            new GestureDetector.SimpleOnGestureListener() {

                @Override
                public void onLongPress(MotionEvent e) {
                    /// IMPORTANT NOTE
                    // detect if long press is actually pull to refresh
                    // since the RecycleView using 3rd party compiled library,
                    // then just hack or work around it to detect if its a pull to refresh
                    View headerView = listView.findChildViewUnder(e.getX(), listView.getY() + 10);
                    int headerPos = -1;
                    if (headerView != null) {
                        headerPos = listView.getChildAdapterPosition(headerView);
                    }

                    if (headerPos != 0) {
                        View child = listView.findChildViewUnder(e.getX(), e.getY());
                        if (child != null) {
                            int position = listView.getChildAdapterPosition(child) - 1;
                            setupSelectionMode(position);
                        }
                    }
                }
            };

        final GestureDetector gestureDetector = new GestureDetector(
                getActivity(),
                gastureDetectorListener);

        listView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                gestureDetector.onTouchEvent(e);
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {}

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {}
        });

    }

    private void setupSelectionMode(int startPosition) {
        if (!listAdapter.getSelectionMode()) {
            listAdapter.setSelectionMode(true);
            listAdapter.addSelection(startPosition);
            listView.setPullRefreshEnabled(false);
            setupSelectionToolbar();
        } else {
            updateSelectionMode(startPosition);
        }
    }

    private void updateSelectionMode(int startPosition) {
        if (listAdapter.getSelectionMode()) {
            if (!listAdapter.isSelected(startPosition)) {
                listAdapter.addSelection(startPosition);
            } else {
                listAdapter.removeSelection(startPosition);
            }
            updateSelectionTitleToolbar();
        }

    }

    private void finishSelectionMode() {
        MainActivity theActivity = (MainActivity) getActivity();
        theActivity.setupToolbar();

        listAdapter.setSelectionMode(false);
        listView.setPullRefreshEnabled(true);
    }

    private void setupSelectionToolbar() {
        MainActivity theActivity = (MainActivity) getActivity();
        if (theActivity != null) {
            Toolbar toolbar = theActivity.getMainToolbar();
            ActionBar theActionBar = theActivity.getSupportActionBar();
            if (theActionBar != null) {
                theActionBar.setDisplayHomeAsUpEnabled(true);
            }
            if (toolbar != null) {
                toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finishSelectionMode();
                    }
                });
                updateSelectionTitleToolbar(toolbar);
            }
        }
    }

    private void updateSelectionTitleToolbar() {
        MainActivity theActivity = (MainActivity) getActivity();
        Toolbar toolbar = theActivity.getMainToolbar();
        updateSelectionTitleToolbar(toolbar);
    }

    private void updateSelectionTitleToolbar(Toolbar toolbar) {
        int selectionCount = listAdapter.getSelection().size();
        if (toolbar != null) {
            toolbar.setTitle(
                    String.format("%d selected", selectionCount)
            );
        }
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
        listView.setRefreshing(true);
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
    public void appendModel(List<ContactEntity> model) {
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
    public void goBack() {

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


}
