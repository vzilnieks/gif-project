package com.example.vadim.gif_project;

import android.app.NotificationManager;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

  ImageAdapterGif imageAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    GridView gridView = findViewById(R.id.gridView);

    imageAdapter = new ImageAdapterGif(this);
    gridView.setAdapter(imageAdapter);

    gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String gifUrl = imageAdapter.getFullPosition(position);
        showNextActivity(gifUrl);
        Toast.makeText(MainActivity.this, "" + position, Toast.LENGTH_SHORT).show();
      }
    });

    gridView.setOnScrollListener(new AbsListView.OnScrollListener(){
      @Override
      public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
      {
        if(firstVisibleItem + visibleItemCount >= totalItemCount){
          imageAdapter.didReachScrollBottom(totalItemCount);
        }
      }

      @Override
      public void onScrollStateChanged(AbsListView view, int scrollState){

      }
    });

    this.showNotificationAfterDelay(this.getBasicNotification());
  }

  private void showNotificationAfterDelay(final NotificationCompat.Builder notification) {

    final NotificationManager mNotificationManager =
            (NotificationManager) getSystemService(this.NOTIFICATION_SERVICE);

    final Handler handler = new Handler();
    handler.postDelayed(new Runnable() {
      @Override
      public void run() {
        mNotificationManager.notify(0, notification.build());
      }
    }, 1000);
  }

  private NotificationCompat.Builder getBasicNotification() {

    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, "default")
            .setSmallIcon(R.drawable.white_notification_icon)
            .setContentTitle("Здарова братан")
            .setContentText("Пора смотреть гифки!")
//            .setStyle(new NotificationCompat.BigTextStyle()
//                    .bigText("Much longer text that cannot fit one line..."))
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setDefaults(NotificationCompat.DEFAULT_VIBRATE);

    return mBuilder;
  }

  private void showNextActivity(String url) {
    Intent intent = new Intent(getBaseContext(), ShareActivity.class);
    intent.putExtra(ShareActivity.SHARE_URL_KEY, url);
    startActivity(intent);
  }
}
