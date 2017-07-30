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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.ditho.assignment.R;
import io.ditho.assignment.common.MouseEventUtils;
import io.ditho.assignment.model.repository.RepositoryProvider;
import io.ditho.assignment.model.repository.entity.ContactEntity;
import io.ditho.assignment.model.rest.ApiProvider;
import io.ditho.assignment.presenter.contact.ContactListPresenterImpl;
import io.ditho.assignment.view.adapter.ContactListAdapter;
import io.ditho.assignment.view.editcontact.EditContactListFragment;
import io.ditho.assignment.view.main.MainActivity;
import io.ditho.assignment.view.linkedcontact.LinkedContactListFragment;

public class ContactListFragment extends Fragment implements ContactListView {

    private final ContactListPresenterImpl presenter;
    private ContactListAdapter listAdapter;
    private Menu menu;
    private boolean isRefreshing = false;

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
        setHasOptionsMenu(true);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact, container, false);
        ButterKnife.bind(this, view);

        MainActivity theActivity = (MainActivity) getActivity();
        theActivity.setupToolbar();

        setupRecycleView();
        presenter.init(
                this,
                ApiProvider.getInstance(getActivity()),
                RepositoryProvider.getInstance(getActivity()));

        return view;
    }

    private void setupRecycleView() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        listAdapter = new ContactListAdapter(getActivity(), new ContactListAdapter.EventListener() {
            @Override
            public void onClick(int position) {
                ContactEntity contactEntity = listAdapter.get(position);
                if (contactEntity != null &&
                    contactEntity.getLinkCount() > 0) {
                    showLinkedContactList(contactEntity);
                }
            }
        });

        listView.setLayoutManager(linearLayoutManager);
        listView.setAdapter(listAdapter);
        listView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        listView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);

        listView.setPullRefreshEnabled(true);
        listView.setLoadingMoreEnabled(false);
        listView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                if (!isRefreshing) {
                    isRefreshing = true;
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            presenter.fetchData();
                        }
                    }, 1000);
                }
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

//                @Override
//                public boolean onSingleTapConfirmed(MotionEvent e) {
//                    View child = listView.findChildViewUnder(e.getX(), e.getY());
//                    if (child != null) {
//                        int position = listView.getChildAdapterPosition(child) - 1;
//                        ContactEntity contactEntity = listAdapter.get(position);
//                        if (contactEntity.getLinkCount() > 0) {
//                            showLinkedContactList(contactEntity);
//                        }
//                    }
//
//                    return true;
//                }

                @Override
                public void onLongPress(MotionEvent e) {
                    // IMPORTANT NOTE
                    // detect if long press is actually pull to refresh
                    // since the RecycleView using 3rd party compiled library,
                    // just hack/work around it to detect if its a pull to refresh.
                    // Pull to refresh arrow position is placed at first position, which is 0
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
            setupSelectionMenu();
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
        listAdapter.setSelectionMode(false);
        listAdapter.clearSelection();
        listView.setPullRefreshEnabled(true);
        revertSelectionToolbar();
        setupMenu();
    }

    private void initMenu(Menu menu) {
        this.menu = menu;
        setupMenu();
    }

    private void setupMenu() {
        int listSize = menu.size();
        for (int counter = 0; counter < listSize; counter++) {
            MenuItem menuItem = menu.getItem(counter);
            switch (menuItem.getItemId()) {
                case R.id.action_merge:
                    menuItem.setVisible(false);
                    break;
            }
        }
    }

    private void setupSelectionMenu() {
        int listSize = menu.size();
        for (int counter = 0; counter < listSize; counter++) {
            MenuItem menuItem = menu.getItem(counter);
            switch (menuItem.getItemId()) {
                case R.id.action_merge:
                    menuItem.setVisible(true);
                    break;
            }
        }
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
        int selectionCount = listAdapter.getSelectionCount();
        if (toolbar != null) {
            toolbar.setTitle(
                    String.format("%d selected", selectionCount)
            );
        }
    }

    private void revertSelectionToolbar() {
        MainActivity theActivity = (MainActivity) getActivity();
        ActionBar theActionBar = theActivity.getSupportActionBar();
        if (theActionBar != null) {
            theActionBar.setDisplayHomeAsUpEnabled(false);
        }
        theActivity.setupToolbar();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.contact, menu);
        initMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean retVal = false;
        switch (item.getItemId()) {
            case R.id.action_merge:
                retVal = true;
                List<ContactEntity> selection = listAdapter.getSelection();
                finishSelectionMode();
                presenter.mergeContact(selection);
                break;
            default:
                retVal = super.onOptionsItemSelected(item);
                break;
        }

        return retVal;
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
                if (!isRefreshing) {
                    isRefreshing = true;
                    listView.setRefreshing(true);

                    // Force to show refresh indicator
                    // need to research later on Fragment Replace on completely updating layout
                    MouseEventUtils.generatePullTouch(listView);
                }
            }
        });
    }

    @Override
    public void finishLoad() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                listView.refreshComplete();
                isRefreshing = false;
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
    public boolean goBack() {
        boolean retVal = false;
        if (listAdapter.getSelectionMode()) {
            finishSelectionMode();
            retVal = true;
        }
        return retVal;
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

    public void showLinkedContactList(ContactEntity contactEntity) {
        final String rootId = contactEntity.getId();
        final String parentId = contactEntity.getId();

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                LinkedContactListFragment fragment = LinkedContactListFragment.newInstance(
                        rootId,
                        parentId);
                MainActivity theActivity = (MainActivity) getActivity();
                theActivity.showFragment(fragment);
            }
        }, 500);
    }
}
