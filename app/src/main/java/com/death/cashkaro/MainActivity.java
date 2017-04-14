package com.death.cashkaro;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;

    private ImageView imageView;
    private TextView textView;
    private Button btnLogout;
    private View navHeader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_container);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            VectorDrawableCompat indicator =
                    VectorDrawableCompat.create(getResources(), R.drawable.ic_menu, getTheme());
            indicator.setTint(ResourcesCompat.getColor(getResources(), android.R.color.white, getTheme()));
            supportActionBar.setHomeAsUpIndicator(indicator);
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    // This method will trigger on item Click of navigation menu
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // Set item in checked state
                        menuItem.setChecked(true);

                        // TODO: handle navigation

                        // Closing drawer on item click
                        mDrawerLayout.closeDrawers();

                        return true;
                    }
                });

        navHeader = navigationView.getHeaderView(0);
        imageView = (ImageView) navHeader.findViewById(R.id.userImage);
        textView = (TextView) navHeader.findViewById(R.id.userName);
        btnLogout = (Button) navHeader.findViewById(R.id.logout);

        if(userLoggedIn())
        {
            SharedPreferences preferences = getApplicationContext().getSharedPreferences("USER", MODE_PRIVATE);
            Glide.with(this).load(preferences.getString("IMAGE", null)).into(imageView);
            textView.setText(preferences.getString("NAME", "Please Login"));
            btnLogout.setVisibility(View.VISIBLE);

        }
        else
        {
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_name));
            textView.setText("Log In");
        }

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();
                SharedPreferences preferences = getApplicationContext().getSharedPreferences("USER",MODE_APPEND);
                SharedPreferences.Editor ed = preferences.edit();
                ed.clear();
                ed.commit();
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_name));
                textView.setText("Log In");
                btnLogout.setVisibility(View.INVISIBLE);
            }
        });

    }

    private boolean userLoggedIn() {
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("USER", MODE_PRIVATE);
        if(preferences.contains("NAME")) {
            return true;
        }
        return  false;
    }


    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getSupportFragmentManager());

        adapter.addFragment(new HomeFragment(), "Home");
        adapter.addFragment(new ElectronicsFragment(), "Electronics");

        viewPager.setAdapter(adapter);
    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public Adapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }


    public boolean askPermission(String permission, int reqCode) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                ActivityCompat.requestPermissions(this, new String[]{permission}, reqCode);
                return false;
            }
        } else {
            return true;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.cameraPermission:
                askPermission(Manifest.permission.CAMERA, 1);
                break;
            case R.id.locationPermission:
                askPermission(Manifest.permission.ACCESS_FINE_LOCATION, 2);
                break;
            case R.id.contactPermission:
                askPermission(Manifest.permission.READ_CONTACTS, 3);
                break;
            case R.id.userLogin:
                if(userLoggedIn()){
                    mDrawerLayout.openDrawer(Gravity.START);
                }else{
                startActivity(new Intent(this, LoginManager.class));
                this.finish();
                }
                break;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}
