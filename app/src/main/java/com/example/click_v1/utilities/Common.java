package com.example.click_v1.utilities;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.shape.TriangleEdgeTreatment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class Common {

    public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffInMillies = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }


    public static String getReadableDateTime(Date date) {
        return new SimpleDateFormat("MMMM dd, yyyy - hh:mm a", Locale.getDefault()).format(date);
    }

    public static Bitmap view2Bitmap(View view) {
        Bitmap bitmap = null;//  w w w . j a  v a  2s.c  om
        try {
            if (view != null) {
                view.setDrawingCacheEnabled(true);
                view.measure(View.MeasureSpec.makeMeasureSpec(0,
                        View.MeasureSpec.UNSPECIFIED), View.MeasureSpec
                        .makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                view.layout(0, 0, view.getMeasuredWidth(),
                        view.getMeasuredHeight());
                view.buildDrawingCache();
                bitmap = view.getDrawingCache();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static void toTriangleEdgeCardView(MaterialCardView cardView, Resources resources) {
        float size = resources.getDimension(com.intuit.sdp.R.dimen._5sdp); //16dp
        TriangleEdgeTreatment triangleEdgeTreatment = new TriangleEdgeTreatment(size, false);
        cardView.setShapeAppearanceModel(cardView.getShapeAppearanceModel()
                .toBuilder()
                .setBottomEdge(triangleEdgeTreatment)
                .build());
    }

    public static BitmapDescriptor getBitmapDescriptorFromView(View view) {
        Bitmap bitmap = view2Bitmap(view);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }


    public static Bitmap getBitmapFromEncodedString(String encodedImage) {
        if(encodedImage != null) {
            byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        } else {
            return null;
        }
    }

    public static double distance(double lat1, double lat2, double lon1,
                                  double lon2, double el1, double el2) {

        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        double height = el1 - el2;

        distance = Math.pow(distance, 2) + Math.pow(height, 2);

        return Math.sqrt(distance);
    }

    public static void loading(Context context, Boolean isLoading) {
        if (isLoading) {
            Toast.makeText(context, "loading activity", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "activity loaded", Toast.LENGTH_SHORT).show();
        }
    }

}
