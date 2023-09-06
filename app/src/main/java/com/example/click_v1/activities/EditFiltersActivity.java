package com.example.click_v1.activities;

import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.click_v1.databinding.ActivitiesEditFiltersBinding;
import com.example.click_v1.utilities.Constants;
import com.example.click_v1.utilities.PreferenceManager;

public class EditFiltersActivity extends AppCompatActivity {

    private ActivitiesEditFiltersBinding binding;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferenceManager = new PreferenceManager(getApplicationContext());
        binding = ActivitiesEditFiltersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListeners();
    }

    private void setListeners() {
        binding.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                binding.distanceText.setText(String.format("0-%s km", i));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Toast.makeText(EditFiltersActivity.this, ""+binding.seekBar.getProgress(), Toast.LENGTH_SHORT).show();
                preferenceManager.putString(Constants.KEY_DISTANCE, Integer.toString(binding.seekBar.getProgress()));
            }
        });
        binding.imageBack.setOnClickListener(v->onBackPressed());
    }
}
