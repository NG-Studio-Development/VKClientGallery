package ru.chulakov.akimovtest2.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKParser;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiPhotoAlbum;
import com.vk.sdk.api.model.VKList;

import org.json.JSONObject;

import ru.chulakov.akimovtest2.R;
import ru.chulakov.akimovtest2.model.VKApiPhotoGEO;
import ru.chulakov.akimovtest2.model.db.PhotoDB;
import ru.chulakov.akimovtest2.ui.activities.ImageActivity;
import ru.chulakov.akimovtest2.ui.activities.MainActivity;
import ru.chulakov.akimovtest2.ui.adapters.GalleryAdapter;

public class GalleryFragment extends BaseFragment<MainActivity> {

    private static final String ARG_ID_ALBUM = "arg_id_album";

    private static final String ARG_TYPE_START = "arg_type_start";

    private static final int TYPE_START_ALBUMS = 0;

    private static final int TYPE_START_PHOTOS = 1;

    private int typeStart;

    public static GalleryFragment newGalleryFragmentAlbum() {
        GalleryFragment fragment = new GalleryFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_TYPE_START, TYPE_START_ALBUMS);
        fragment.setArguments(args);
        return fragment;
    }

    public static GalleryFragment newGalleryFragmentPhotoInAlbum(long idAlbum) {
        GalleryFragment fragment = new GalleryFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_ID_ALBUM, idAlbum);
        args.putInt(ARG_TYPE_START, TYPE_START_PHOTOS);
        fragment.setArguments(args);
        return fragment;
    }

    public GalleryFragment() { /* Required empty public constructor */ }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            typeStart = getArguments().getInt(ARG_TYPE_START);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_gallery, container, false);
        final GridView gvGallery = (GridView) view.findViewById(R.id.gvGallery);

        if(typeStart == TYPE_START_ALBUMS)
            createAlbumsView(gvGallery);
        else
            createPhotosView(gvGallery);

        return view;
    }

    void createAlbumsView(GridView gvGallery) {
        getAlbums(gvGallery);
        gvGallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                GalleryFragment fragment = newGalleryFragmentPhotoInAlbum(l);
                getHostActivity().switchFragment(fragment, true);
            }
        });
    }

    private void getAlbums(final GridView gvGallery) {

        VKRequest albums = new VKRequest(METHOD_GET_ALBUMS,
                VKParameters.from(VKApiConst.PHOTO_SIZES, 1),
                VKRequest.HttpMethod.GET);
        albums.setResponseParser(new VKParser() {
            @Override
            public Object createModel(JSONObject object) {
                return new VKList<>(object, VKApiPhotoAlbum.class);
            }
        });

        albums.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                VKList<VKApiPhotoAlbum> vkList = (VKList) response.parsedModel;
                GalleryAdapter adapter = new GalleryAdapter(getHostActivity(),R.layout.item_gallery, vkList, getAlbumInitializer());
                gvGallery.setAdapter(adapter);
            }
        });
    }

    void createPhotosView(GridView gvGallery) {
        final long idAlbum = getArguments().getLong(ARG_ID_ALBUM, Integer.MIN_VALUE);

        if (idAlbum != Integer.MIN_VALUE)
            if(getHostActivity().isConnectToInternet()) {
                getPhotoByAlbum(idAlbum, gvGallery);
            } else {
                VKList<VKApiPhotoGEO> vkList = PhotoDB.getVKApiPhotos(idAlbum);

                GalleryAdapter adapter = new GalleryAdapter(getHostActivity(), R.layout.item_gallery, vkList, getPhotoInitializer());
                gvGallery.setAdapter(adapter);
            }

        gvGallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ImageActivity.startImageActivity(getHostActivity(), idAlbum, i);
            }
        });
    }

    private void getPhotoByAlbum (long albumId, final GridView gvGallery) {

        VKRequest photo = new VKRequest(METHOD_GET_PHOTOS,
                VKParameters.from(VKApiConst.PHOTO_SIZES, 0, VKApiConst.ALBUM_ID, albumId,VKApiConst.EXTENDED,1,VKApiConst.FIELDS, "lat,long"),
                VKRequest.HttpMethod.GET);

        photo.setResponseParser(new VKParser() {
            @Override
            public VKList/*<VKApiPhoto>*/ createModel(JSONObject object) {
                return new VKList<>(object, VKApiPhotoGEO.class);
            }
        });

        photo.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                VKList<VKApiPhotoGEO> vkList = (VKList) response.parsedModel;
                GalleryAdapter adapter = new GalleryAdapter(getHostActivity(), R.layout.item_gallery, vkList, getPhotoInitializer());
                gvGallery.setAdapter(adapter);
                PhotoDB.putFromVKList(vkList);
            }
        });
    }

    GalleryAdapter.Initializer getAlbumInitializer() {
        return new GalleryAdapter.Initializer(){

            @Override
            public String getImageUri(GalleryAdapter adapter, int position) {
                VKApiPhotoAlbum album = (VKApiPhotoAlbum)adapter.getItem(position);
                return album.photo.get(0).src;
            }

            @Override
            public String getName(GalleryAdapter adapter, int position) {
                VKApiPhotoAlbum album = (VKApiPhotoAlbum)adapter.getItem(position);
                return album.title;
            }

            @Override
            public long getId(GalleryAdapter adapter, int position) {
                VKApiPhotoAlbum album = (VKApiPhotoAlbum)adapter.getItem(position);
                return album.getId();
            }
        };

    }

    GalleryAdapter.Initializer getPhotoInitializer() {
        return new GalleryAdapter.Initializer(){


            @Override
            public String getImageUri(GalleryAdapter adapter, int position) {
                VKApiPhotoGEO photo = (VKApiPhotoGEO)adapter.getItem(position);
                return photo.getMaxImageURI();
            }

            @Override
            public String getName(GalleryAdapter adapter, int position) {
                VKApiPhotoGEO photo = (VKApiPhotoGEO)adapter.getItem(position);
                return String.valueOf(photo.date);

            }

            @Override
            public long getId(GalleryAdapter adapter, int position) {
                VKApiPhotoGEO photo = (VKApiPhotoGEO)adapter.getItem(position);
                return photo.getId();
            }
        };
    }

    private static final String METHOD_GET_ALBUMS = "photos.getAlbums";
    private static final String METHOD_GET_PHOTOS = "photos.get";

    @Override
    public int getLayoutResID() {
        return R.layout.fragment_gallery;
    }


}
