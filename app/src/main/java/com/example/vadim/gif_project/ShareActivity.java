package com.example.vadim.gif_project;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class ShareActivity extends AppCompatActivity {

  public static String SHARE_URL_KEY = "SHARE";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_share);

    String url = getIntent().getStringExtra(SHARE_URL_KEY);
    if (url == null) {
      return;
    }

    ImageView imageView = findViewById(R.id.img_view);

    Glide.with(this)
            .asGif()
            .transition(withCrossFade(500))
            .load(url)
            .apply(
                    new RequestOptions()
                            .placeholder(R.drawable.white_notification_icon)
                            .diskCacheStrategy(DiskCacheStrategy.ALL))
            .into(imageView);
  }

  public void onShare(View view) {
    ImageView imageView = findViewById(R.id.img_view);
    imageView.setDrawingCacheEnabled(true);
    imageView.buildDrawingCache();
    Bitmap bitmap = imageView.getDrawingCache();

    String path = MediaStore.Images.Media.insertImage(getContentResolver(),
            bitmap, "awesomeGIF", null);

    Uri uri = Uri.parse(path);

    Intent share = new Intent(Intent.ACTION_SEND);
    share.setType("image/gif");
    share.putExtra(Intent.EXTRA_STREAM, uri);
    share.putExtra(Intent.EXTRA_TEXT, "I found something cool!");
    this.startActivity(Intent.createChooser(share, "GIF!"));
  }

  public void onCancel(View view) {
    this.finish();
  }
}
