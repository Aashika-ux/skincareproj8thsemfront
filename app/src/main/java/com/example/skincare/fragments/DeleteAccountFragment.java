package com.example.skincare.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.skincare.views.LoginActivity;
import com.example.skincare.R;
import com.example.skincare.imp.PreferenceManager;

public class DeleteAccountFragment extends Fragment {

    private PreferenceManager preferenceManager;

    public DeleteAccountFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_delete_account, container, false);

        preferenceManager = new PreferenceManager(requireContext());

        Button btnDelete = view.findViewById(R.id.btnconfirmdelete);
        Button btnCancel = view.findViewById(R.id.btncanceldelete);

        btnDelete.setOnClickListener(v -> deleteAccount());
        btnCancel.setOnClickListener(v -> getParentFragmentManager().popBackStack());

        return view;
    }

    private void deleteAccount() {
        // Clear SharedPreferences
        preferenceManager.clearData();

        Toast.makeText(getContext(), "Account deleted successfully", Toast.LENGTH_SHORT).show();

        // Go to login
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        requireActivity().finish();
    }
}
