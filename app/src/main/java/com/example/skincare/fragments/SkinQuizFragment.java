package com.example.skincare.fragments;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.skincare.R;
import com.google.android.material.snackbar.Snackbar;

public class SkinQuizFragment extends Fragment {

    private RadioGroup rg_question1, rg_question2, rg_question3, rg_question4,
            rg_question5, rg_question6, rg_question7;
    private Button btn_submit;

    private int dry = 0, oily = 0, normal = 0, sensitive = 0;

    public SkinQuizFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_skin_quiz, container, false);

        // Initialize RadioGroups
        rg_question1 = view.findViewById(R.id.rg_question1);
        rg_question2 = view.findViewById(R.id.rg_question2);
        rg_question3 = view.findViewById(R.id.rg_question3);
        rg_question4 = view.findViewById(R.id.rg_question4);
        rg_question5 = view.findViewById(R.id.rg_question5);
        rg_question6 = view.findViewById(R.id.rg_question6);
        rg_question7 = view.findViewById(R.id.rg_question7);

        // Initialize Submit button
        btn_submit = view.findViewById(R.id.btn_submit);

        btn_submit.setOnClickListener(v -> {
            if (allQuestionsAnswered()) {
                calculateSkinType(view);
            } else {
                showSnackbar(view, "Please answer all questions!");
            }
        });

        return view;
    }

    // Check if all questions are answered
    private boolean allQuestionsAnswered() {
        return rg_question1.getCheckedRadioButtonId() != -1 &&
                rg_question2.getCheckedRadioButtonId() != -1 &&
                rg_question3.getCheckedRadioButtonId() != -1 &&
                rg_question4.getCheckedRadioButtonId() != -1 &&
                rg_question5.getCheckedRadioButtonId() != -1 &&
                rg_question6.getCheckedRadioButtonId() != -1 &&
                rg_question7.getCheckedRadioButtonId() != -1;
    }

    // Calculate skin type and show result
    private void calculateSkinType(View view) {
        // Reset scores
        dry = 0; oily = 0; normal = 0; sensitive = 0;

        // Weighted scoring
        scoreQuestion(rg_question1, "Tight", "Normal", "Oily", "Sensitive", 3);
        scoreQuestion(rg_question2, "Often", "Sometimes", "Rarely", "Normal", 2);
        scoreQuestion(rg_question3, "Very", "Moderate", "Low", "Normal", 3);
        scoreQuestion(rg_question4, "Burns easily", "Tans easily", "Both", "Normal", 2);
        scoreQuestion(rg_question5, "Very shiny", "Slightly shiny", "Not shiny", "Normal", 3);
        scoreQuestion(rg_question6, "Often", "Sometimes", "Rarely", "Normal", 2);
        scoreQuestion(rg_question7, "Often", "Sometimes", "Rarely", "Normal", 2);

        // Determine max score
        int maxScore = Math.max(Math.max(dry, oily), Math.max(normal, sensitive));
        int countMax = 0;
        if(dry == maxScore) countMax++;
        if(oily == maxScore) countMax++;
        if(normal == maxScore) countMax++;
        if(sensitive == maxScore) countMax++;

        String skinType;
        int bgColor;

        if(countMax > 1){
            skinType = "Mixed Skin";
            bgColor = Color.parseColor("#9E9E9E"); // gray
        } else if(maxScore == dry){
            skinType = "Dry";
            bgColor = Color.parseColor("#FFA726"); // orange
        } else if(maxScore == oily){
            skinType = "Oily";
            bgColor = Color.parseColor("#29B6F6"); // blue
        } else if(maxScore == sensitive){
            skinType = "Sensitive";
            bgColor = Color.parseColor("#AB47BC"); // purple
        } else {
            skinType = "Normal";
            bgColor = Color.parseColor("#66BB6A"); // green
        }

        // Show Snackbar until user clicks OK
        Snackbar snackbar = Snackbar.make(view, "Your skin type: " + skinType, Snackbar.LENGTH_INDEFINITE);
        snackbar.setBackgroundTint(bgColor);
        snackbar.setTextColor(Color.WHITE);

        // Clear selections only after OK is clicked
        snackbar.setAction("OK", v -> {
            clearAllSelections();
            snackbar.dismiss();
        });

        snackbar.setActionTextColor(Color.YELLOW);
        snackbar.show();
    }

    // Score individual question
    private void scoreQuestion(RadioGroup rg, String dryOpt, String normalOpt,
                               String oilyOpt, String sensitiveOpt, int weight) {
        int selectedId = rg.getCheckedRadioButtonId();
        if(selectedId == -1) return;

        RadioButton selected = rg.findViewById(selectedId);
        String answer = selected.getText().toString();

        if(answer.equals(dryOpt)) dry += weight;
        else if(answer.equals(normalOpt)) normal += weight;
        else if(answer.equals(oilyOpt)) oily += weight;
        else if(answer.equals(sensitiveOpt)) sensitive += weight;
    }

    // Clear all selected answers
    private void clearAllSelections() {
        rg_question1.clearCheck();
        rg_question2.clearCheck();
        rg_question3.clearCheck();
        rg_question4.clearCheck();
        rg_question5.clearCheck();
        rg_question6.clearCheck();
        rg_question7.clearCheck();
    }

    // Show a custom snackbar for warnings
    private void showSnackbar(View view, String message) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_INDEFINITE);

        // Customize background and corners
        View snackbarView = snackbar.getView();
        GradientDrawable bgShape = new GradientDrawable();
        bgShape.setCornerRadius(24); // Rounded corners
        bgShape.setColor(Color.parseColor("#00796B")); // Teal color
        snackbarView.setBackground(bgShape);

        // Text color
        snackbar.setTextColor(Color.WHITE);

        // Action button
        snackbar.setAction("DISMISS", v -> snackbar.dismiss());
        snackbar.setActionTextColor(Color.YELLOW);

        snackbar.show();
    }
}
