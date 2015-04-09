package ru.chulakov.akimovtest2;

public final class Constants {

	public static int MAX_PHOTO_IN_CACHE = 1000;

    public static int MAX_WIDTH_PHOTO_IN_CACHE = 800;
    public static int MAX_HEIGHT_PHOTO_IN_CACHE = 800;


    private Constants() {
	}

	public static class Config {
		public static final boolean DEVELOPER_MODE = false;
	}
	
	public static class Extra {
		public static final String FRAGMENT_INDEX = "com.nostra13.example.universalimageloader.FRAGMENT_INDEX";
		public static final String IMAGE_POSITION = "com.nostra13.example.universalimageloader.IMAGE_POSITION";
	}
}