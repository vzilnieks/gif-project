package com.example.vadim.gif_project;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.giphy.sdk.core.models.Media;
import com.giphy.sdk.core.models.enums.MediaType;
import com.giphy.sdk.core.network.api.CompletionHandler;
import com.giphy.sdk.core.network.api.GPHApi;
import com.giphy.sdk.core.network.api.GPHApiClient;
import com.giphy.sdk.core.network.response.ListMediaResponse;

import java.util.List;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

/**
 * Created by vadim on 07.05.2018
 */
public class ImageAdapterGif extends BaseAdapter {

  private Context mContext;
  private GPHApi mClient;
  private List<Media> mData;

  public ImageAdapterGif(Context c) {
    mContext = c;
    mClient = new GPHApiClient("UX9CCDUOQHrkLIA3bJnIpkbp2UgWFTqB");
    getTrendingGifs();

  }

  private void getTrendingGifs() {
    mClient.trending(MediaType.gif, null, null, null, new CompletionHandler<ListMediaResponse>() {
      @Override
      public void onComplete(ListMediaResponse result, Throwable e) {
        if (result == null) {
          // Do what you want to do with the error
        } else {
          mData = result.getData();

          ((MainActivity)mContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
              notifyDataSetChanged();
            }
          });

          if (result.getData() != null) {
            for (Media gif : result.getData()) {
              Log.v("giphy", gif.getId());
            }
          } else {
            Log.e("giphy error", "No results found");
          }
        }
      }
    });
  }

  @Override
  public int getCount() {
    return mData == null ? 0 : mData.size();
  }

  @Override
  public Object getItem(int position) {
    return null;
  }

  @Override
  public long getItemId(int position) {
    return 0;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    ImageView imageView;
    if (convertView == null) {
      // if not recycled, init some attr
      imageView = new ImageView(mContext);
      imageView.setLayoutParams(new ViewGroup.LayoutParams(200,200));
      imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
      imageView.setPadding(8,8,8,8);
    } else {
      imageView = (ImageView) convertView;
    }
//    imageView.setImageResource(mThumbIds[position]);
    Media media = mData.get(position);

    Glide.with(mContext)
            .asGif()
            .transition(withCrossFade(500))
            .load(media.getImages().getDownsized().getGifUrl())
            .apply(
                    new RequestOptions()
                            .placeholder(R.drawable.white_notification_icon)
                            .diskCacheStrategy(DiskCacheStrategy.ALL))
            .into(imageView);
    return imageView;
  }

  public String getFullPosition(int position) {
    Media media = mData.get(position);
    return media.getImages().getOriginal().getGifUrl();
  }
}
