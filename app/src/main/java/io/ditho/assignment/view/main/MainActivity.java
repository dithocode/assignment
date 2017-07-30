package io.ditho.assignment.view.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import io.ditho.assignment.R;
import io.ditho.assignment.view.BaseView;
import io.ditho.assignment.view.contact.ContactListFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String MAIN_FRAGMENT_TAG = "MAIN_FRAGMENT_TAG";
    Toolbar toolbar;
    DrawerLayout drawer;
    NavigationView navigationView;
    BaseView currentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        setupToolbar();
        showMainFragment();
    }

    public void setupToolbar() {
        toolbar.setTitle(getTitle());
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
    }

    public Toolbar getMainToolbar() {
        return toolbar;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            boolean isHandled = false;
            if (currentView != null) {
                isHandled = currentView.goBack();
            }
            if (!isHandled) {
                if (getFragmentManager().getBackStackEntryCount() > 0) {
                    getFragmentManager().popBackStack();
                } else {
                    super.onBackPressed();
                }
                Fragment fragment =
                    getSupportFragmentManager().findFragmentByTag(MAIN_FRAGMENT_TAG);
                if (fragment instanceof BaseView) {
                    currentView = (BaseView) fragment;
                    fragment.getView().invalidate();
                } else {
                    currentView = null;
                }
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case R.id.nav_contact:
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void showMainFragment() {
        Fragment fragment = ContactListFragment.newInstance();
        currentView = (BaseView) fragment;
        getSupportFragmentManager().popBackStack(
                null,
                FragmentManager.POP_BACK_STACK_INCLUSIVE);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction tr = fm.beginTransaction();
        tr.replace(R.id.layout_container, fragment, MAIN_FRAGMENT_TAG);
        tr.commit();
        fm.executePendingTransactions();
    }

    public void showFragment(@NonNull Fragment fragment) {
        if(fragment != null &&
                (fragment instanceof BaseView)) {
            try {
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction tr = fm.beginTransaction();
                tr.replace(R.id.layout_container, fragment, MAIN_FRAGMENT_TAG);
                tr.addToBackStack(null);
                tr.commit();
                fm.executePendingTransactions();
                currentView = (BaseView) fragment;
            } catch (Exception e) {
                Log.e(getClass().getName(), e.getMessage(), e);
            }
        }
    }
}
