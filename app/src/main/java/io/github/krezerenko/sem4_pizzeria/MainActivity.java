package io.github.krezerenko.sem4_pizzeria;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import io.github.krezerenko.sem4_pizzeria.cart.CartFragment;
import io.github.krezerenko.sem4_pizzeria.menu.MenuFragment;
import io.github.krezerenko.sem4_pizzeria.profile.ProfileFragment;

public class MainActivity extends AppCompatActivity
{
    private int currentFragment = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) ->
        {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.statusBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });

        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav_main);
        bottomNav.setOnItemSelectedListener(item ->
        {
            FragmentManager fragmentManager = getSupportFragmentManager();
            int itemId = item.getItemId();
            if (itemId == R.id.item_bottom_menu && currentFragment != 0)
            {
                changeFragment(new MenuFragment(), fragmentManager);
                currentFragment = 0;
            }
            if (itemId == R.id.item_bottom_cart && currentFragment != 1)
            {
                changeFragment(new CartFragment(), fragmentManager);
                currentFragment = 1;
            }
            if (itemId == R.id.item_bottom_orders && currentFragment != 2)
            {
                changeFragment(new Fragment(), fragmentManager);
                currentFragment = 2;
            }
            if (itemId == R.id.item_bottom_profile && currentFragment != 3)
            {
                changeFragment(new ProfileFragment(), fragmentManager);
                currentFragment = 3;
            }
            return true;
        });

    }

    private void changeFragment(Fragment newFragment, FragmentManager manager)
    {
        manager.beginTransaction()
                .replace(R.id.fragment_container_menu, newFragment)
                .commit();
    }
}