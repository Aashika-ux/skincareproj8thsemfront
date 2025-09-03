package com.example.skincare.views;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.skincare.databinding.ActivityHomePageBinding;
import com.example.skincare.R;
import com.example.skincare.fragments.NotifyFragment;
import com.example.skincare.fragments.HomePageFragment;
import com.example.skincare.fragments.ProfileFragment;
import com.example.skincare.fragments.SkinCareFragment;
import com.example.skincare.fragments.SkinQuizFragment;
import com.example.skincare.fragments.RoutinePageFragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomePageActivity extends AppCompatActivity {
    private ActivityHomePageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivityHomePageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        replaceFragment(new HomePageFragment());

        final BottomNavigationView navView = binding.bottomNavigationView;

        navView.setOnItemSelectedListener(item -> {

            // Fragment selection using if-else logic
            if (item.getItemId() == R.id.home) {
                replaceFragment(new HomePageFragment());
            } else if (item.getItemId() == R.id.care) {
                replaceFragment(new SkinCareFragment());
            } else if (item.getItemId() == R.id.routine) {
                replaceFragment(new RoutinePageFragment());
            } else if (item.getItemId() == R.id.person) {
                replaceFragment(new ProfileFragment());
            } else if (item.getItemId() == R.id.face) {
                replaceFragment(new SkinQuizFragment());
            }

            // Elevate the BottomNavigationView on click
//            navView.animate()
//                    .translationZ(20f)
//                    .setDuration(150)
//                    .withEndAction(() ->
//                            navView.animate()
//                                    .translationZ(0f)
//                                    .setDuration(150)
//                    );

            return true;
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (findViewById(R.id.frameFragmentLayout) == null) {
            throw new RuntimeException("frameFragmentLayout not found! Check XML.");
        }

        fragmentTransaction.replace(R.id.frameFragmentLayout, fragment);
        fragmentTransaction.commit();
    }

}
