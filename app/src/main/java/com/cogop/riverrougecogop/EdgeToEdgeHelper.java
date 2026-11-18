package com.cogop.riverrougecogop;

import android.app.Activity;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

/**
 * Enables edge-to-edge on all API levels and pads content away from system bars
 * without using the deprecated statusBarColor / navigationBarColor APIs.
 */
public final class EdgeToEdgeHelper {

    private EdgeToEdgeHelper() {
    }

    public static void enable(@NonNull Activity activity) {
        WindowCompat.enableEdgeToEdge(activity.getWindow());
    }

    public static void applyContentInsets(@NonNull Activity activity) {
        View content = activity.findViewById(android.R.id.content);
        if (!(content instanceof ViewGroup)) {
            return;
        }
        ViewGroup contentGroup = (ViewGroup) content;
        if (contentGroup.getChildCount() == 0) {
            return;
        }
        View root = contentGroup.getChildAt(0);
        // Skip layouts that already opt into the legacy fitsSystemWindows path
        // (DrawerLayout / NavigationView handle their own insets).
        if (root.getFitsSystemWindows()) {
            return;
        }
        ViewCompat.setOnApplyWindowInsetsListener(root, (v, windowInsets) -> {
            Insets bars = windowInsets.getInsets(
                    WindowInsetsCompat.Type.systemBars() | WindowInsetsCompat.Type.displayCutout());
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom);
            return windowInsets;
        });
        ViewCompat.requestApplyInsets(root);
    }

    public static boolean shouldEnableInOnCreate() {
        // onActivityPreCreated is only delivered on API 29+.
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.Q;
    }
}
