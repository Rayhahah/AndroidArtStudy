package com.rayhahah.androidartstudy.util;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;

/**
 * 图片功能
 * 初始化CameraUtil,调用showCamera,然后在onActivityForResult进行分类处理
 *
 * @author user
 */
public class CameraUtil {

    public static final int Capture = 1;
    public static final int Content = 2;
    private Context context;
    private String rootPath;
    private Uri uri;
    private File imgFile;
    private String imgFileName;
    private String imgFilePath;
    private int type;

    public CameraUtil(Context context, String path) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.rootPath = path;
    }

    public Uri getUri() {
        return uri;
    }

    public File getImgFile() {
        return imgFile;
    }

    public String getImgFileName() {
        return imgFileName;
    }

    public String getImgFilePath() {
        return imgFilePath;
    }

    public int getType() {
        return type;
    }

    /**
     * 对照相图片进行处理，旋转;
     *
     * @param file   图片文件
     * @param isZoom 是否进行图片缩小
     * @return 返回处理后的图片文件
     */
    public File dealImageCapture(File file, boolean isZoom) {

        int degree = readPictureDegree(file.getAbsolutePath());
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inSampleSize = 2;
        Bitmap cbitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), opts);
        Bitmap newbitmap = rotaingImageView(degree, cbitmap);
        if (isZoom) {
            Bitmap bitmap = getimage(getRealFilePath(uri));
            if (bitmap == null) {
                bitmap = zoomBitmap(newbitmap, newbitmap.getWidth() / 2, newbitmap.getHeight() / 2);

            }
            return saveBitmap(bitmap, file.toString());
        } else {

            return saveBitmap(newbitmap, file.toString());
        }
    }

    /**
     * 处理相册图片，裁剪方法startPhotoZoom后调用
     *
     * @param u      本地图片地址uri
     * @param isZoom 是否进行图片缩小
     */
    public void dealImageContentWithCrop(Intent intent, boolean isZoom) {
        Bundle extras = intent.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
//			final Drawable drawable = new BitmapDrawable(photo);
            try {
                if (photo != null) {
                    imgFileName = getFileName();
                    File photoFile = getOutputMediaFileUri(imgFileName);
                    Uri imageUri = Uri.fromFile(photoFile);
                    uri = imageUri;
                    imgFile = photoFile;
                    imgFilePath = photoFile.toString();
                    if (photoFile != null) {
                        // 如果需要，进行图片压缩
                        Bitmap smallBitmap;
                        if (isZoom) {

//							smallBitmap = comp(photo);
//							smallBitmap = getimage(imgFilePath);
                            smallBitmap = getimage(getRealFilePath(uri));
                            if (smallBitmap == null) {

                                smallBitmap = zoomBitmap(photo,
                                        photo.getWidth() / 2, photo.getHeight() / 2);
                            }
                        } else {
                            smallBitmap = photo;
                        }
                        FileOutputStream out = new FileOutputStream(
                                photoFile.toString());
                        smallBitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
                        out.flush();
                        out.close();
                    }
                }
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    /**
     * 处理相册图片，不裁剪
     *
     * @param u      本地图片地址uri
     * @param isZoom 是否进行图片缩小
     */
    public void dealImageContentWithoutCrop(Uri u, boolean isZoom) {
        ContentResolver resolver = context.getContentResolver();
        try {
            Bitmap photo = MediaStore.Images.Media.getBitmap(resolver, u);
            if (photo != null) {
                imgFileName = getFileName();
                File photoFile = getOutputMediaFileUri(imgFileName);
                Uri imageUri = Uri.fromFile(photoFile);
                uri = imageUri;
                imgFile = photoFile;
                imgFilePath = photoFile.toString();
                if (photoFile != null) {
                    // 如果需要，进行图片压缩
                    Bitmap smallBitmap;
                    if (isZoom) {

//						smallBitmap = comp(photo);
//						smallBitmap = getimage(imgFilePath);
                        smallBitmap = getimage(getRealFilePath(u));
                        if (smallBitmap == null) {
                            smallBitmap = zoomBitmap(photo,
                                    photo.getWidth() / 2, photo.getHeight() / 2);
                        }
                    } else {
                        smallBitmap = photo;
                    }
                    FileOutputStream out = new FileOutputStream(
                            photoFile.toString());
                    if (smallBitmap != null) {

                        smallBitmap.compress(Bitmap.CompressFormat.PNG, 50, out);
                    }
                    out.flush();
                    out.close();
                }
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 处理相册图片，不裁剪
     *
     * @param u      本地图片地址uri
     * @param isZoom 是否进行图片缩小
     */
    public void dealImageContentWithoutCropAndCopy(Uri u, boolean isZoom) {

        String res = getRealFilePath(u);
        if (res != null) {
            imgFileName = res;
            imgFile = new File(res);
            uri = u;
            imgFilePath = u.getPath();
        }
    }

    /**
     * 从Uri 获取真实路径
     *
     * @param uri
     * @return
     */
    public String getRealFilePath(final Uri u) {
        if (null == u) {
            return null;
        }
        final String scheme = u.getScheme();
        String data = null;
        if (scheme == null) {

            data = u.getPath();
        } else if (ContentResolver.SCHEME_FILE.equals(scheme)) {

            data = u.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(u, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    /**
     * 读取图片属性：旋转的角度
     *
     * @param path 图片绝对路径
     * @return degree旋转的角度
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 旋转图片
     *
     * @param angle
     * @param bitmap
     * @return Bitmap
     */
    public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
        // 旋转图片 动作
        Matrix matrix = new Matrix();
        ;
        matrix.postRotate(angle);
        System.out.println("angle2=" + angle);
        // 创建新的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizedBitmap;
    }

    /**
     * 保存图片
     *
     * @param bitmap
     * @param fileName
     * @return
     */
    public static File saveBitmap(Bitmap bitmap, String fileName) {
        // 获得文件的File对象
        final File imgFile = new File(fileName);
        if (imgFile.exists()) {
            imgFile.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(imgFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imgFile;
    }

    /**
     * 缩放Bitmap图片
     */
    public static Bitmap zoomBitmap(Bitmap bitmap, int width, int height) {

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Matrix matrix = new Matrix();
        float scaleWidth = ((float) width / w);
        float scaleHeight = ((float) height / h);
        matrix.postScale(scaleWidth, scaleHeight);// 利用矩阵进行缩放不会造成内存溢出
        Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);

        return newbmp;
    }

    /**
     * 图片按比例大小压缩方法（根据Bitmap图片压缩）
     *
     * @param image
     * @return
     */
    private Bitmap comp(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        if (baos.toByteArray().length / 1024 > 1024) {//判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, 50, baos);//这里压缩50%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 800f;//这里设置高度为800f
        float ww = 480f;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        isBm = new ByteArrayInputStream(baos.toByteArray());
        bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        return compressImage(bitmap);//压缩好比例大小后再进行质量压缩
    }

    /**
     * 质量压缩方法
     *
     * @param image
     * @return
     */
    private Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if (image != null) {

            image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        }
        int options = 100;
        while (baos.toByteArray().length / 1024 > (100 * 5)) {  //循环判断如果压缩后图片是否大于5*100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
            if (options <= 0) {
                break;
            }
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    /**
     * 图片按比例大小压缩方法（根据路径获取图片并压缩）
     *
     * @param srcPath
     * @return
     */
    private Bitmap getimage(String srcPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);//此时返回bm为空

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
//		//现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
//		float hh = 800f;//这里设置高度为800f
//		float ww = 480f;//这里设置宽度为480f
        float hh = 1280f;
        float ww = 720f;
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        return compressImage(bitmap);//压缩好比例大小后再进行质量压缩
    }

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    public void startPhotoZoom(Uri uri) {
        if (uri == null) {
            Log.i("tag", "The uri is not exist.");
            return;
        }

        Intent intent = new Intent("com.android.camera.action.CROP");
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            String url = getPath(context, uri);
            intent.setDataAndType(Uri.fromFile(new File(url)), "image/*");
        } else {
            intent.setDataAndType(uri, "image/*");
        }
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 320);
        intent.putExtra("outputY", 320);
        intent.putExtra("return-data", true);
        ((Activity) context).startActivityForResult(intent, 3);
    }

    // 以下是关键，原本uri返回的是file:///...来着的，android4.4返回的是content:///...
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/"
                            + split[1];
                }

            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(context, contentUri, selection,
                        selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri,
                                       String selection, String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection,
                    selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri
                .getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri
                .getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri
                .getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri
                .getAuthority());
    }

    /**
     * 根据文件名fileNmae获得文件file
     *
     * @param fileName
     * @return 返回图片File对象
     */
    public File getOutputMediaFileUri(String fileName) {
        //

        String fileRootName = getRootPath().toString();

        File imageFile = new File(fileRootName + File.separator + fileName);
        Log.v("imageFile=", imageFile.toString());
        return imageFile;
    }

    /**
     * 根据时间定义图片名称
     *
     * @return 返回图片File对象
     */
    public String getFileName() {

        // Create a media file name
        String timeStamp = new DateFormat().format("yyyyMMdd_hhmmss",
                Calendar.getInstance(Locale.CHINA))
                + ".png";
        return timeStamp;
    }

    /**
     * 保存图片的文件夹
     *
     * @return 返回文件夹
     */
    public File getRootPath() {
        // 保存图片的文件夹
        File file = new File(Environment.getExternalStorageDirectory() + "/"
                + rootPath + "/Pic");
        if (!file.exists()) {
            file.mkdirs();// 创建文件夹
        }
        return file;
    }
    private int pos;

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

}