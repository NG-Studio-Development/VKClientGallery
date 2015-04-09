package ru.chulakov.akimovtest2.ui.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.List;

import ru.chulakov.akimovtest2.R;

public class GalleryAdapter extends ArrayAdapter {

    private LayoutInflater inflater;

    private int resource;

    private DisplayImageOptions options;



    private Initializer initializer;

    public GalleryAdapter(Context context, int resource, List list, Initializer initializer) {
        super(context, resource, list);
        this.resource = resource;
        this.initializer = initializer;

        inflater = LayoutInflater.from(context);

        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_stub)
                .showImageForEmptyUri(R.drawable.ic_empty)
                .showImageOnFail(R.drawable.ic_error)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final GalleryHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(resource, parent, false);
            holder = createGalleryHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (GalleryHolder) convertView.getTag();
        }


        holder.tvName.setText(initializer.getName(this,position));

        ImageLoader.getInstance()
                .displayImage(initializer.getImageUri(this,position)/*album.photo.get(0).src*/, holder.ivIcon, options, new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                        holder.progressBar.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                        holder.progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        holder.progressBar.setVisibility(View.GONE);
                    }
                }, new ImageLoadingProgressListener() {
                    @Override
                    public void onProgressUpdate(String imageUri, View view, int current, int total) {
                    }
                });

        //VKApiPhotoAlbum album = getItem(position);
        //initializer.initView(this,position);

        return convertView;
    }

    @Override
    public long getItemId(int position) {
        //return getItem(position).getId();
        return initializer.getId(this,position);
    }


    public interface Initializer {
        //void initView(GalleryAdapter adapter, int position);
        String getImageUri(GalleryAdapter adapter, int position);
        String getName(GalleryAdapter adapter, int position);
        long getId(GalleryAdapter adapter, int position);
    }

    private GalleryHolder createGalleryHolder(View view) {

        GalleryHolder holder = new GalleryHolder();
        holder.ivIcon = (ImageView) view.findViewById(R.id.ivIcon);
        holder.tvName = (TextView) view.findViewById(R.id.tvName);
        holder.progressBar = (ProgressBar) view.findViewById(R.id.progress);
        return holder;
    }

    static class GalleryHolder {
        ImageView ivIcon;
        TextView tvName;
        ProgressBar progressBar;
    }
}
