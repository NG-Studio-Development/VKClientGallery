package ru.chulakov.akimovtest2.model.db;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.vk.sdk.api.model.VKList;

import java.util.List;

import ru.chulakov.akimovtest2.model.VKApiPhotoGEO;

@Table(name = "Items")
public class PhotoDB extends Model {

    @Column(name = "Photo_75")
    public String photo_75;

    @Column(name = "Photo_130")
    public String photo_130;

    @Column(name = "Photo_604")
    public String photo_604;

    @Column(name = "Photo_807")
    public String photo_807;

    @Column(name = "Photo_1280")
    public String photo_1280;

    @Column(name = "Photo_2560")
    public String photo_2560;

    @Column(name = "Date")
    private long date;

    @Column(name = "Likes")
    private int likes;

    @Column(name = "PhotoId", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    private int photoId;

    @Column(name = "AlbumId")
    private long albumId;

    @Column(name = "Latitude")
    private double latitude;

    @Column(name = "Longitude")
    private double longitude;

    public static void putFromVKList(VKList<VKApiPhotoGEO> vkList) {

        ActiveAndroid.beginTransaction();
        try {
                for (VKApiPhotoGEO vkApiPhotoGeo : vkList) {
                    PhotoDB photoDB = new PhotoDB();
                    photoDB.photoId = vkApiPhotoGeo.id;

                    photoDB.photo_2560 = vkApiPhotoGeo.photo_2560;
                    photoDB.photo_1280 = vkApiPhotoGeo.photo_1280;
                    photoDB.photo_807 = vkApiPhotoGeo.photo_807;
                    photoDB.photo_604 = vkApiPhotoGeo.photo_604;
                    photoDB.photo_130 = vkApiPhotoGeo.photo_130;
                    photoDB.photo_75 = vkApiPhotoGeo.photo_75;

                    photoDB.date = vkApiPhotoGeo.date;
                    photoDB.likes = vkApiPhotoGeo.likes;
                    photoDB.albumId = vkApiPhotoGeo.album_id;
                    photoDB.latitude = vkApiPhotoGeo.latitude;
                    photoDB.longitude = vkApiPhotoGeo.longitude;
                    photoDB.save();
                }

            ActiveAndroid.setTransactionSuccessful();
        }
        finally {
            ActiveAndroid.endTransaction();
        }
    }

    public static VKList<VKApiPhotoGEO> getVKApiPhotos(long albumId) {

        List<PhotoDB> listPhotoDB = new Select().from(PhotoDB.class)
                .where("AlbumId = ?", albumId)
                .execute();

        VKList<VKApiPhotoGEO> vkListPhoto = new VKList();

        for (PhotoDB photoDB : listPhotoDB) {
            VKApiPhotoGEO vkApiPhotoGEO = new VKApiPhotoGEO();

            vkApiPhotoGEO.date = photoDB.date;
            vkApiPhotoGEO.likes = photoDB.likes;
            vkApiPhotoGEO.latitude = photoDB.latitude;
            vkApiPhotoGEO.longitude = photoDB.longitude;

            vkApiPhotoGEO.photo_2560 = photoDB.photo_2560;
            vkApiPhotoGEO.photo_1280 = photoDB.photo_1280;
            vkApiPhotoGEO.photo_807 = photoDB.photo_807;
            vkApiPhotoGEO.photo_604 = photoDB.photo_604;
            vkApiPhotoGEO.photo_130 = photoDB.photo_130;
            vkApiPhotoGEO.photo_75 = photoDB.photo_75;

            vkListPhoto.add(vkApiPhotoGEO);
        }

        return vkListPhoto;
    }



}
