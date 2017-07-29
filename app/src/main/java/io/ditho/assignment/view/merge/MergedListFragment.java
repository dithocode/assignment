package io.ditho.assignment.view.merge;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.ditho.assignment.R;
import io.ditho.assignment.presenter.merge.MergedListPresenter;
import io.ditho.assignment.presenter.merge.MergedListPresenterImpl;
import io.ditho.assignment.model.repository.RepositoryProvider;
import io.ditho.assignment.model.repository.entity.ContactEntity;
import io.ditho.assignment.model.rest.ApiProvider;
import io.ditho.assignment.view.adapter.ContactListAdapter;

public class MergedListFragment extends Fragment implements MergedListView {

    private MergedListPresenter presenter;

    private ContactListAdapter listAdapter;

    @Nullable
    @Bind(R.id.listview_marged_contact)
    XRecyclerView listView;

    public MergedListFragment() {
        presenter = new MergedListPresenterImpl();
    }

    /**
     * create a new instance of contact list fragment
     *
     * @return A new instance of fragment AccountContactListFragment.
     */
    public static MergedListFragment newInstance() {
        MergedListFragment fragment = new MergedListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_marge, container, false);

        ButterKnife.bind(this, view);

        listAdapter = new ContactListAdapter(getActivity());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

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

        presenter.init(
                this,
                ApiProvider.getInstance(getActivity()),
                RepositoryProvider.getInstance(getActivity()));

        return view;
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
    public void appendModel(final List<ContactEntity> model) {
        if (listAdapter != null) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    listAdapter.addAll(model);
                    listAdapter.notifyDataSetChanged();
                }
            });
        }
    }

    @Override
    public void updateModel(final List<ContactEntity> model) {
        if (listAdapter != null) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    listAdapter.clear();
                    listAdapter.addAll(model);

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
