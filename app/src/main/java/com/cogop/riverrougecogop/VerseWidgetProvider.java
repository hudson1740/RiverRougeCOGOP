package com.cogop.riverrougecogop;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;


import java.util.Random;

public class VerseWidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    private void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        // 1. Get the verse and reference
        String verse = getVerse(context);
        String reference = getReference(context);

        // 2. Create the RemoteViews
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.verse_widget_layout);
        views.setTextViewText(R.id.verse_text, verse);
        views.setTextViewText(R.id.verse_reference, reference);

        // 3. Create the Intent and PendingIntent (THIS WAS MISSING)
        Intent intent = new Intent(context, MainActivity.class); // Replace MainActivity with your actual activity class name if different
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

        // 4. Set the click listener (THIS WAS ALSO MISSING)
        views.setOnClickPendingIntent(R.id.HomeButton, pendingIntent); // Make the whole layout clickable

        // 5. Update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    private String getVerse(Context context) {
        String[] versesWithReferences = context.getResources().getStringArray(R.array.verses);
        Random random = new Random();
        int randomIndex = random.nextInt(versesWithReferences.length);
        String verseWithReference = versesWithReferences[randomIndex];

        String[] parts = verseWithReference.split("\\|");
        return parts[1].trim();
    }

    private String getReference(Context context) {
        String[] versesWithReferences = context.getResources().getStringArray(R.array.verses);
        Random random = new Random();
        int randomIndex = random.nextInt(versesWithReferences.length);
        String verseWithReference = versesWithReferences[randomIndex];

        String[] parts = verseWithReference.split("\\|");
        return parts[0].trim();
    }
}