package com.example.click_v1.utilities;


import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.View;

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

}
