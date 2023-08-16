package com.example.click_v1.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.click_v1.databinding.ActiviesEditHobbiesBinding;
import com.example.click_v1.utilities.Constants;
import com.example.click_v1.utilities.PreferenceManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class EditHobbiesActivity extends AppCompatActivity {
    private ActiviesEditHobbiesBinding binding;
    private PreferenceManager preferenceManager;
    private FirebaseFirestore database;

    private ArrayList<String> hobbies;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferenceManager = new PreferenceManager(getApplicationContext());
        binding = ActiviesEditHobbiesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
        setListeners();
        database = FirebaseFirestore.getInstance();
    }

    private void init() {
        if (preferenceManager.getString(Constants.KEY_HOBBIES) != null) {
            hobbies = new ArrayList<>(Arrays.asList(preferenceManager.getString(Constants.KEY_HOBBIES).split("\\s*,\\s*")));
            for (String hobby : hobbies) {
                switch (hobby) {
                    case "music":
                        binding.checkbox1.setChecked(true);
                        break;
                    case "shopping":
                        binding.checkbox2.setChecked(true);
                        break;
                    case "traveling":
                        binding.checkbox3.setChecked(true);
                        break;
                    case "enjoyFood":
                        binding.checkbox4.setChecked(true);
                        break;
                    case "singing":
                        binding.checkbox5.setChecked(true);
                        break;
                    case "gym":
                        binding.checkbox6.setChecked(true);
                        break;
                    case "movies":
                        binding.checkbox7.setChecked(true);
                        break;
                    case "photographing":
                        binding.checkbox8.setChecked(true);
                        break;
                    case "cooking":
                        binding.checkbox9.setChecked(true);
                        break;
                    case "meditation":
                        binding.checkbox10.setChecked(true);
                        break;
                    case "gardening":
                        binding.checkbox11.setChecked(true);
                        break;
                    case "languageExchange":
                        binding.checkbox12.setChecked(true);
                        break;
                    case "dancing":
                        binding.checkbox13.setChecked(true);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    private void setListeners() {
        binding.saveBtn.setOnClickListener(v -> onSavePressed());
        binding.imageBack.setOnClickListener(v -> onBackPressed());
        binding.checkbox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    hobbies.add("music");
                } else {
                    hobbies.remove("music");
                }
            }
        });
        binding.checkbox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    hobbies.add("shopping");
                } else {
                    hobbies.remove("shopping");
                }
            }
        });
        binding.checkbox3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    hobbies.add("traveling");
                } else {
                    hobbies.remove("traveling");
                }
            }
        });
        binding.checkbox4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    hobbies.add("enjoyFood");
                } else {
                    hobbies.remove("enjoyFood");
                }
            }
        });
        binding.checkbox5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    hobbies.add("singing");
                } else {
                    hobbies.remove("singing");
                }
            }
        });
        binding.checkbox6.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    hobbies.add("gym");
                } else {
                    hobbies.remove("gym");
                }
            }
        });
        binding.checkbox7.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    hobbies.add("movies");
                } else {
                    hobbies.remove("movies");
                }
            }
        });
        binding.checkbox8.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    hobbies.add("photographing");
                } else {
                    hobbies.remove("photographing");
                }
            }
        });
        binding.checkbox9.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    hobbies.add("cooking");
                } else {
                    hobbies.remove("cooking");
                }
            }
        });
        binding.checkbox10.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    hobbies.add("meditation");
                } else {
                    hobbies.remove("meditation");
                }
            }
        });
        binding.checkbox11.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    hobbies.add("gardening");
                    Toast.makeText(getApplicationContext(), hobbies.toString(), Toast.LENGTH_SHORT).show();

                } else {
                    hobbies.remove("gardening");
                }
            }
        });
        binding.checkbox12.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    hobbies.add("languageExchange");
                    Toast.makeText(getApplicationContext(), hobbies.toString(), Toast.LENGTH_SHORT).show();

                } else {
                    hobbies.remove("languageExchange");
                }
            }
        });
        binding.checkbox13.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    hobbies.add("dancing");
                    Toast.makeText(getApplicationContext(), hobbies.toString(), Toast.LENGTH_SHORT).show();

                } else {
                    hobbies.remove("dancing");
                }
            }
        });
    }

    private void onSavePressed() {
        loading(true);
        String hobbiesAsS = String.join(",", hobbies);
        updateHobbies(hobbiesAsS);
        Toast.makeText(getApplicationContext(), hobbiesAsS, Toast.LENGTH_SHORT).show();
    }

    private void updateHobbies(String hobbies) {
        DocumentReference documentReference = database.collection(Constants.KEY_COLLECTION_USERS).document(preferenceManager.getString(Constants.KEY_USER_ID));
        Map<String, Object> data = new HashMap<>();
        data.put(Constants.KEY_HOBBIES, hobbies);
        documentReference.set(data, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        preferenceManager.putString(Constants.KEY_HOBBIES, hobbies);
                        loading(false);
                        Toast.makeText(EditHobbiesActivity.this, "ok", Toast.LENGTH_SHORT).show();
                        new CountDownTimer(500, 500) {
                            public void onTick(long millisUntilFinished) {
                            }

                            public void onFinish() {
                                Intent intent = new Intent(getApplicationContext(), EditProfileActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                        }.start();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        loading(false);
                        Toast.makeText(EditHobbiesActivity.this, "failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void loading(Boolean isLoading) {
        if (isLoading) {
            binding.progressBar.setVisibility(View.VISIBLE);
        } else {
            binding.progressBar.setVisibility(View.INVISIBLE);
        }
    }

}
