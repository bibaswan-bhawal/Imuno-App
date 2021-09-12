package com.boss.imuno.UI.details;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.boss.imuno.R;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class DetailsFragment extends Fragment {

    OnButtonClickListener mCallback;

    private int pagePosition;

    private boolean showLevel;

    private TextInputLayout ageTextField;
    private TextInputLayout weightTextField;
    private TextInputLayout levelTextField;
    private TextInputLayout heightTextField;
    private TextInputLayout minutesTextField;

    private ProgressBar progressbar;
    private RadioGroup radioGroup;

    public interface OnButtonClickListener {
        void onButtonClicked(int position, String age, String weight,String height, boolean levelProvided, String level, int race, String hours);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mCallback = (OnButtonClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnItemClickListener");
        }
    }

    public DetailsFragment() {
        // Required empty public constructor
    }

    public static DetailsFragment newInstance(int position) {
        DetailsFragment detailsFragment = new DetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(String.valueOf(R.string.register_pager_position_key), position);
        detailsFragment.setArguments(bundle);
        return detailsFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onPause() {
        if (ageTextField != null && !Objects.requireNonNull(ageTextField.getEditText()).getText().toString().isEmpty()) {
            ageTextField.setErrorEnabled(false);
        }
        if (weightTextField != null && !Objects.requireNonNull(weightTextField.getEditText()).getText().toString().isEmpty()) {
            weightTextField.setErrorEnabled(false);
        }
        if (levelTextField != null && !Objects.requireNonNull(levelTextField.getEditText()).getText().toString().isEmpty()) {
            levelTextField.setErrorEnabled(false);
        }
        if (minutesTextField != null && !Objects.requireNonNull(minutesTextField.getEditText()).getText().toString().isEmpty()) {
            minutesTextField.setErrorEnabled(false);
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(pagePosition == 1){
            ageTextField.post(new Runnable() {
            @Override
            public void run() {
                ageTextField.requestFocus();
                InputMethodManager inputMethodManager = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.showSoftInput(ageTextField, InputMethodManager.SHOW_IMPLICIT);
            }
        });
    } else if(pagePosition == 2) {
            minutesTextField.post(new Runnable() {
                @Override
                public void run() {
                    minutesTextField.requestFocus();
                    InputMethodManager inputMethodManager = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.showSoftInput(minutesTextField, InputMethodManager.SHOW_IMPLICIT);
                }
            });
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        pagePosition = 0;
        if (getArguments() != null) {
            pagePosition = getArguments().getInt(String.valueOf(R.string.register_pager_position_key));
        }

        View root;
        final int finalPagePosition = pagePosition;

        switch (pagePosition) {
            case 0:
            default:
                root = inflater.inflate(R.layout.fragment_details_1, container, false);
                Button nextbutton = root.findViewById(R.id.nextButton);

                nextbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mCallback.onButtonClicked(finalPagePosition, null, null,null, false, null, 0, null);
                    }
                });

                break;

            case 1:
                root = inflater.inflate(R.layout.fragment_details_2, container, false);
                nextbutton = root.findViewById(R.id.nextButton);
                ageTextField = root.findViewById(R.id.detailAgeTextField);
                weightTextField = root.findViewById(R.id.detailWeightTextField);
                levelTextField = root.findViewById(R.id.detailLevelTextField);
                heightTextField = root.findViewById(R.id.detailHeightTextField);

                CheckBox showLevelCheckBox = root.findViewById(R.id.showLevelCheckBox);

                showLevelCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                        showLevel = isChecked;
                        if(isChecked){
                           levelTextField.setVisibility(View.VISIBLE);
                        }else{
                            levelTextField.setVisibility(View.INVISIBLE);
                        }
                    }
                });

                nextbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        boolean error = false;
                        String detailsAge = String.valueOf(Objects.requireNonNull(ageTextField.getEditText()).getText());
                        String detailsWeight = String.valueOf(Objects.requireNonNull(weightTextField.getEditText()).getText());
                        String detailsHeight = String.valueOf(Objects.requireNonNull(heightTextField.getEditText()).getText());

                        String detailsLevel = "";

                        if(detailsAge.isEmpty()){
                            ageTextField.setError(getString(R.string.age_error));
                            ageTextField.setErrorEnabled(true);

                            error = true;
                        } else {
                            try {
                                Integer.parseInt(detailsAge);
                            } catch (NumberFormatException e) {
                                ageTextField.setError(getString(R.string.age_error));
                                ageTextField.setErrorEnabled(true);
                            }

                        }

                        if(detailsWeight.isEmpty()){
                            weightTextField.setError(getString(R.string.weight_error));
                            weightTextField.setErrorEnabled(true);

                            error = true;
                        }else {
                            try {
                                Integer.parseInt(detailsWeight);
                            } catch (NumberFormatException e) {
                                weightTextField.setError(getString(R.string.weight_error));
                                weightTextField.setErrorEnabled(true);
                            }

                        }

                        if(detailsHeight.isEmpty()){
                            heightTextField.setError(getString(R.string.height_error));
                            heightTextField.setErrorEnabled(true);

                            error = true;
                        }else {
                            try {
                                Integer.parseInt(detailsWeight);
                            } catch (NumberFormatException e) {
                                heightTextField.setError(getString(R.string.height_error));
                                heightTextField.setErrorEnabled(true);
                            }

                        }

                        if(showLevel) {
                            detailsLevel = String.valueOf(Objects.requireNonNull(levelTextField.getEditText()).getText());

                            if(detailsLevel.isEmpty()){
                                levelTextField.setError(getString(R.string.level_error));
                                levelTextField.setErrorEnabled(true);

                                error = true;
                            }else {
                                try {
                                    Double.parseDouble(detailsLevel);
                                } catch (NumberFormatException e) {
                                    levelTextField.setError(getString(R.string.level_error));
                                    levelTextField.setErrorEnabled(true);
                                }

                            }
                        }

                        if(!error){
                            mCallback.onButtonClicked(finalPagePosition, detailsAge, detailsWeight, detailsHeight, showLevel, detailsLevel, 0, null);
                        }
                    }
                });

                break;

            case 2:
                root = inflater.inflate(R.layout.fragment_details_3, container, false);
                nextbutton = root.findViewById(R.id.nextButton);

                minutesTextField = root.findViewById(R.id.detailMinutesTextField);
                radioGroup = root.findViewById(R.id.radioGroup);

                progressbar = root.findViewById(R.id.detailsUserProgessBar);
                minutesTextField.setErrorEnabled(false);

                nextbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {



                        String detailsMinutes = String.valueOf(minutesTextField.getEditText().getText());
                        int detailsRace = radioGroup.indexOfChild(radioGroup.findViewById(radioGroup.getCheckedRadioButtonId())) + 1;

                        if(detailsMinutes.isEmpty()){
                            minutesTextField.setError(getString(R.string.hour_error));
                            minutesTextField.setErrorEnabled(true);
                        }else{
                            try {
                                Integer.parseInt(detailsMinutes);
                                mCallback.onButtonClicked(finalPagePosition, null, null, null,false, null, detailsRace, detailsMinutes);
                            } catch (NumberFormatException e) {
                                minutesTextField.setError(getString(R.string.hour_error));
                                minutesTextField.setErrorEnabled(true);
                            }

                        }
                    }
                });

                break;

        }

        return root;
    }

    public void showProgressBar(){
        progressbar.setVisibility(View.VISIBLE);
    }
    public void hideProgressBar(){
        progressbar.setVisibility(View.INVISIBLE);
    }

}
