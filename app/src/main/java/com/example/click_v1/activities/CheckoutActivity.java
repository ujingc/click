package com.example.click_v1.activities;


import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.click_v1.R;
import com.example.click_v1.databinding.ActiviesCheckoutBinding;
import com.example.click_v1.models.User;
import com.example.click_v1.utilities.Constants;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.paymentsheet.PaymentSheet;
import com.stripe.android.paymentsheet.PaymentSheetResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class CheckoutActivity extends AppCompatActivity {

    private ActiviesCheckoutBinding binding;
    private final String publishableKey = "pk_test_51NhqMgD1UGjBrGqgc2Q7wRA4P7aKhnTb1R4Lq2gFqdfYS5F1OsSDnAXtMiCq1Kkhz58B7hQ7oz6SAIAYOERM4jho00VsKmg1PC";
    private final String secretKey = "sk_test_51NhqMgD1UGjBrGqgbU7WMQjcBUpSbeYDBeaF3a5dd98rPX1ekslJNQC8dyr4nY9VHPXOri3d9gjHI166LAGNY7vb00MeT8gI8L";
    private String ephemeralKeys;
    private String clientSecret;
    private String customerId;
    private PaymentSheet paymentSheet;
    private User receiverUser;
    private User senderUser;
    private int stars;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActiviesCheckoutBinding.inflate(getLayoutInflater());
        PaymentConfiguration.init(this, publishableKey);
        paymentSheet = new PaymentSheet(this, this::onPaymentSheetResult);
        setContentView(binding.getRoot());
        startPaymentProcess();
        loadCheckoutDetails();
        setListeners();
    }

    private void loadCheckoutDetails() {
        senderUser = (User) getIntent().getSerializableExtra(Constants.KEY_USER);
        receiverUser = (User) getIntent().getSerializableExtra(Constants.KEY_RECEIVER);
        stars = (int) getIntent().getSerializableExtra(Constants.KEY_STARS);
        switch (stars) {
            case 50:
                binding.productTitleText.setText(stars+"颗星");
                binding.productItemText.setText(stars+"颗星");
                binding.productPriceText.setText("USD$" + 39.9);
                binding.productSubtotalPriceText.setText("USD$" + 39.9);
                binding.productTotalPriceText.setText("USD$" + 39.9);
                binding.payNowPriceText.setText("USD$" + 39.9);
                break;
            case 100:
                binding.productTitleText.setText(stars+"颗星");
                binding.productItemText.setText(stars+"颗星");
                binding.productPriceText.setText("USD$" + 89.99);
                binding.productSubtotalPriceText.setText("USD$" + 89.99);
                binding.productTotalPriceText.setText("USD$" + 89.99);
                binding.payNowPriceText.setText("USD$" + 89.99);
                break;
            case 200:
                binding.productTitleText.setText(stars+"颗星");
                binding.productItemText.setText(stars+"颗星");
                binding.productPriceText.setText("USD$" + 189.99);
                binding.productSubtotalPriceText.setText("USD$" + 189.99);
                binding.productTotalPriceText.setText("USD$" + 189.99);
                binding.payNowPriceText.setText("USD$" + 189.99);
                break;
            default:
                binding.productTitleText.setText(stars+"颗星");
                binding.productItemText.setText(stars+"颗星");
                binding.productPriceText.setText("USD$" + stars);
                binding.productSubtotalPriceText.setText("USD$" + stars);
                binding.productTotalPriceText.setText("USD$" + stars);
                binding.payNowPriceText.setText("USD$" + stars);
                break;
        }
    }

    private void startPaymentProcess() {
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                getResources().getString(R.string.stripe_customer_api),
                response -> {
                    try {
                        JSONObject object = new JSONObject(response);
                        customerId = object.getString("id");
                        getEmhericalKey(customerId);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {

                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> header = new HashMap<>();
                header.put("Authorization", "Bearer " + secretKey);
                return header;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(CheckoutActivity.this);
        requestQueue.add(stringRequest);
    }


    private void getEmhericalKey(String customerId) {
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                getResources().getString(R.string.stripe_ephemeral_api),
                response -> {
                    try {
                        Toast.makeText(getApplicationContext(), "getEmhericalKey"+customerId, Toast.LENGTH_SHORT).show();
                        JSONObject object = new JSONObject(response);
                        ephemeralKeys = object.getString("id");
                        System.out.println("my ephemeralKeys: "+ephemeralKeys);
                        getClientSecret(customerId);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                volleyError -> Toast.makeText(getApplicationContext(), volleyError.getMessage(), Toast.LENGTH_SHORT).show()
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> header = new HashMap<>();
                header.put("Authorization", "Bearer " + secretKey);
                header.put("Stripe-Version", "2023-08-16");
                return header;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("customer", customerId);
                return params;

            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(CheckoutActivity.this);
        requestQueue.add(stringRequest);
    }

    private void getClientSecret(String customerId) {
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                getResources().getString(R.string.stripe_payment_intent_api),
                response -> {
                    try {
                        JSONObject object = new JSONObject(response);
                        clientSecret = object.getString("client_secret");
                        Toast.makeText(CheckoutActivity.this, "launch payment sheet", Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> header = new HashMap<>();
                header.put("Authorization", "Bearer " + secretKey);
                return header;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("customer", customerId);
                params.put("currency", "usd");
                switch (stars) {
                    case 50:
                        params.put("amount", "03999");
                        break;
                    case 100:
                        params.put("amount", "08999");
                        break;
                    case 200:
                        params.put("amount", "18999");
                        break;
                    default:
                        params.put("amount", stars+"00");
                        break;
                }
                params.put("automatic_payment_methods[enabled]", "true");
                return params;

            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(CheckoutActivity.this);
        requestQueue.add(stringRequest);
    }

    private void onPaymentSheetResult(
            final PaymentSheetResult paymentSheetResult
    ) {
        if (paymentSheetResult instanceof PaymentSheetResult.Canceled) {
            Toast.makeText(this, "Canceled", Toast.LENGTH_SHORT).show();
        } else if (paymentSheetResult instanceof PaymentSheetResult.Failed) {
            Toast.makeText(this, "Got error: " + ((PaymentSheetResult.Failed) paymentSheetResult).getError(), Toast.LENGTH_SHORT).show();
        } else if (paymentSheetResult instanceof PaymentSheetResult.Completed) {
            onBackPressed();
            Toast.makeText(this, "Completed", Toast.LENGTH_SHORT).show();
            // Display for example, an order confirmation screen
        }
    }


    private void setListeners() {
        binding.payNowBtn.setOnClickListener(view -> {
            paymentFlow();
            Toast.makeText(CheckoutActivity.this, "pay", Toast.LENGTH_SHORT).show();
        });
        binding.cancelBtn.setOnClickListener(v->onBackPressed());
    }

    private void paymentFlow() {
//        PaymentSheet.Address address =
//                new PaymentSheet.Address.Builder()
//                        .country("AU")
//                        .build();
//        PaymentSheet.BillingDetails billingDetails =
//                new PaymentSheet.BillingDetails.Builder()
//                        .address(address)
//                        .build();

//        PaymentSheet.Configuration paymentSheetConfiguration = new PaymentSheet.Configuration.Builder(
//                "social coverage")
//                .customer(new PaymentSheet.CustomerConfiguration(
//                        customerId, ephemeralKeys))
//                .defaultBillingDetails(billingDetails)
//                .build();


        paymentSheet.presentWithPaymentIntent(clientSecret, new PaymentSheet.Configuration(
                "social coverage", new PaymentSheet.CustomerConfiguration(
                customerId, ephemeralKeys
        )));
    }
}
