package com.example.ccuda;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {

    private FragmentManager fragmentManager = getSupportFragmentManager();
    private ChatFragment fragmentChat = new ChatFragment();
    private CupponFragment fragmentCuppon = new CupponFragment();
    private CartFragment fragmentCart = new CartFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //FragmentTransaction transaction = fragmentManager.beginTransaction();
        //transaction.replace(R.id.frameLayout, fragmentSearch).commitAllowingStateLoss();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(new ItemSelectedListener());
        //bottomNavigationView.setOnNavigationItemSelectedListener(new ItemSelectedListener());





    }


    class ItemSelectedListener implements BottomNavigationView.OnNavigationItemSelectedListener{
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            switch(menuItem.getItemId())
            {
                case R.id.cuppon:
                    transaction.replace(R.id.frameLayout, fragmentCuppon).commitAllowingStateLoss();

                    break;
                case R.id.cart:
                    transaction.replace(R.id.frameLayout, fragmentCart).commitAllowingStateLoss();
                    break;
                case R.id.chat:
                    transaction.replace(R.id.frameLayout, fragmentChat).commitAllowingStateLoss();
                    break;
            }
            return true;
        }
    }


}
