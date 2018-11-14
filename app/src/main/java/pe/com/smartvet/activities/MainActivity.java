package pe.com.smartvet.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.androidnetworking.widget.ANImageView;

import pe.com.smartvet.R;
import pe.com.smartvet.SmartVetApp;
import pe.com.smartvet.fragments.OwnerFragment;
import pe.com.smartvet.fragments.PetFragment;
import pe.com.smartvet.fragments.ProductFragment;
import pe.com.smartvet.fragments.PromotionFragment;
import pe.com.smartvet.fragments.ReportFragment;
import pe.com.smartvet.fragments.UserFragment;
import pe.com.smartvet.models.Vet;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    ANImageView photoANImageView;
    TextView displayNameTextView;
    TextView emailTextView;
    Toolbar toolbar;

    Vet vet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);

        photoANImageView = headerView.findViewById(R.id.photoANImageView);
        displayNameTextView = headerView.findViewById(R.id.displayNameTextView);
        emailTextView = headerView.findViewById(R.id.emailTextView);

        vet = SmartVetApp.getInstance().getVet();

        photoANImageView.setErrorImageResId(R.mipmap.ic_launcher);
        photoANImageView.setDefaultImageResId(R.mipmap.ic_launcher);
        photoANImageView.setImageUrl(vet.getPhoto());
        displayNameTextView.setText(vet.getName());
        emailTextView.setText(vet.getEmail());

        navigateAccordingTo(R.id.nav_user);
    }

    private Fragment getFragmentFor(int id) {
        switch (id) {
            case R.id.nav_user:
                toolbar.setTitle(R.string.nav_option_user);
                return new UserFragment();
            case R.id.nav_pet:
                toolbar.setTitle(R.string.nav_option_pet);
                return new PetFragment();
            case R.id.nav_owner:
                toolbar.setTitle(R.string.nav_option_owner);
                return new OwnerFragment();
            case R.id.nav_product:
                toolbar.setTitle(R.string.nav_option_product);
                return new ProductFragment();
            case R.id.nav_report:
                toolbar.setTitle(R.string.nav_option_report);
                return new ReportFragment();
            case R.id.nav_promo:
                toolbar.setTitle(R.string.nav_option_promo);
                return new PromotionFragment();
        }
        return null;
    }

    private boolean navigateAccordingTo(int id) {
        try {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content, getFragmentFor(id))
                    .commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_user) {
            navigateAccordingTo(id);
        }  else if (id == R.id.nav_pet) {
            navigateAccordingTo(id);
        }  else if (id == R.id.nav_owner) {
            navigateAccordingTo(id);
        }  else if (id == R.id.nav_product) {
            navigateAccordingTo(id);
        }  else if (id == R.id.nav_report) {
            navigateAccordingTo(id);
        }  else if (id == R.id.nav_promo) {
            navigateAccordingTo(id);
        } else if (id == R.id.nav_session) {
            SplashActivity.changeData(MainActivity.this,"user" ,"");
            SplashActivity.changeData(MainActivity.this,"password" ,"");
            SplashActivity.changeData(MainActivity.this,"token" ,"");
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        photoANImageView.setErrorImageResId(R.mipmap.ic_launcher);
        photoANImageView.setDefaultImageResId(R.mipmap.ic_launcher);
        photoANImageView.setImageUrl(vet.getPhoto());
        displayNameTextView.setText(vet.getName());
        emailTextView.setText(vet.getEmail());
    }
}
