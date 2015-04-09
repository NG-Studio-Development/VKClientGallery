package ru.chulakov.akimovtest2.ui.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.vk.sdk.api.model.VKList;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ru.chulakov.akimovtest2.R;
import ru.chulakov.akimovtest2.model.VKApiPhotoGEO;

public class ImageAdapter extends PagerAdapter {

		private LayoutInflater inflater;
        int resource;
        Context context;
        private VKList<VKApiPhotoGEO> list;
        private List<Bitmap> listBitmap;

        PlaceButtonClickListener placeButtonClickListener;

        public ImageAdapter(Context context, int resource, VKList<VKApiPhotoGEO> list) {
            inflater = LayoutInflater.from(context);
            this.resource = resource;
            this.context = context;
            this.list = list;
            listBitmap = new ArrayList<>();
        }

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object instantiateItem(ViewGroup view, final int position) {
			View viewItem = inflater.inflate(R.layout.item_pager_image, view, false);
			assert viewItem != null;

            final ImageAdapterHolder holder = createImageAdapterHolder(viewItem);

            VKApiPhotoGEO vkApiPhoto = list.get(position);
            holder.tvCountLike.setText(String.valueOf(vkApiPhoto.likes));
			holder.tvDate.setText("Date: "+getDateByTimestamp(vkApiPhoto.date));

            if(vkApiPhoto.hasLocation())
                holder.ibPlace.setVisibility(View.VISIBLE);
            else
                holder.ibPlace.setVisibility(View.GONE);

            holder.ibPlace.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(placeButtonClickListener != null)
                        placeButtonClickListener.onClick(list,position);
                }
            });

            ImageLoader.getInstance().displayImage(vkApiPhoto.getMaxImageURI(), holder.ivImage, createOptions(), new SimpleImageLoadingListener() {
				@Override
				public void onLoadingStarted(String imageUri, View view) {
                    holder.spinner.setVisibility(View.VISIBLE);
				}

				@Override
				public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
					String message = null;
					switch (failReason.getType()) {
						case IO_ERROR:
							message = "Input/Output error";
							break;
						case DECODING_ERROR:
							message = "Image can't be decoded";
							break;
						case NETWORK_DENIED:
							message = "Downloads are denied";
							break;
						case OUT_OF_MEMORY:
							message = "Out Of Memory error";
							break;
						case UNKNOWN:
							message = "Unknown error";
							break;
					}
					Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

                    holder.spinner.setVisibility(View.GONE);
				}

				@Override
				public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    holder.spinner.setVisibility(View.GONE);
                    listBitmap.add(loadedImage);
				}
			});

			view.addView(viewItem, 0);
			return viewItem;
		}

        private String getDateByTimestamp(long milliseconds) {
            return new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date(milliseconds*1000));
        }

        public Object getItem(int position) {
            return list.get(position);
        }

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view.equals(object);
		}

		@Override
		public void restoreState(Parcelable state, ClassLoader loader) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}

        private DisplayImageOptions createOptions() {
            return new DisplayImageOptions.Builder()
                    .showImageForEmptyUri(R.drawable.plus)
                    .showImageOnFail(R.drawable.plus)
                    .resetViewBeforeLoading(true)
                    .cacheOnDisk(true)
                    .imageScaleType(ImageScaleType.EXACTLY)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .considerExifParams(true)
                    .displayer(new FadeInBitmapDisplayer(300))
                    .build();
        }

    public Bitmap getBitmap(int position) {
        return listBitmap.get(position);
    }

    private ImageAdapterHolder createImageAdapterHolder(View view) {

        ImageAdapterHolder holder = new ImageAdapterHolder();
        holder.ivImage = (ImageView) view.findViewById(R.id.image);
        holder.tvCountLike = (TextView) view.findViewById(R.id.tvCountLike);
        holder.tvDate = (TextView) view.findViewById(R.id.tvDate);
        holder.spinner = (ProgressBar) view.findViewById(R.id.loading);
        holder.ibPlace = (ImageButton) view.findViewById(R.id.ibPlace);

        return holder;
    }

    public void setPlaceButtonClickListener(PlaceButtonClickListener placeButtonClickListener) {
        this.placeButtonClickListener = placeButtonClickListener;
    }

    public interface PlaceButtonClickListener {
        void onClick(VKList<VKApiPhotoGEO> list, int position);
    }

    static class ImageAdapterHolder {
        ImageView ivImage;
        TextView tvCountLike;
        //TextView tvUserName;
        TextView tvDate;
        ProgressBar spinner;
        ImageButton ibPlace;

    }
}