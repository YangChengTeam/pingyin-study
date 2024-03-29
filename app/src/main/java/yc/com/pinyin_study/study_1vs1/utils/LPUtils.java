package yc.com.pinyin_study.study_1vs1.utils;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import yc.com.rthttplibrary.util.LogUtil;


public class LPUtils {

    private static final String TAG = LPUtils.class.getSimpleName();
    private static WeakReference<LPUtils> mUtilsWeakReference = null;

    public static synchronized LPUtils getInstance() {
        synchronized (LPUtils.class) {
            if (null == mUtilsWeakReference || null == mUtilsWeakReference.get()) {
                LPUtils utils = new LPUtils();
                mUtilsWeakReference = new WeakReference<LPUtils>(utils);
            }
        }
        return mUtilsWeakReference.get();
    }

    /**
     * 给单词的双舌音部分加上颜色
     *
     * @param word   源单词
     * @param letter 双舌音部分
     * @return
     */
    public String addWordLetterColor(String word, String letter) {
        if (TextUtils.isEmpty(word)) return word;
        if (TextUtils.isEmpty(letter)) return word;
        if (word.contains(letter)) {
            return word.replace(letter, "<font color='#FD0000'>" + letter + "</font>");
        }
        return word;
    }

    /**
     * 给音标加上颜色
     *
     * @param wordPhonetic 源音标
     * @param phonetic
     * @return
     */
    public String addWordPhoneticLetterColor(String wordPhonetic, String phonetic) {
        if (TextUtils.isEmpty(wordPhonetic)) return wordPhonetic;
        if (TextUtils.isEmpty(phonetic)) return wordPhonetic;
        String replace = phonetic.replace("/", "");
        if (wordPhonetic.contains(replace)) {
            return wordPhonetic.replace(replace, "<font color='#FD0000'>" + replace + "</font>");
        }
        return phonetic;
    }

    /**
     * 给音标元音部分加上颜色
     *
     * @param phrase
     * @return
     */
    public String addPhraseLetterColor(String phrase) {
        if (TextUtils.isEmpty(phrase)) return phrase;

        phrase = phrase.replaceAll("#", "-");

        List<Integer> pos = appearNumber(phrase, "-");

        if (pos.size() == 0) return phrase;
        String temp = phrase;
        for (int i = 0; i < pos.size() - 1; i += 2) {

            try {


                String sub = temp.substring(pos.get(i), pos.get(i + 1));
                phrase = phrase.replace(sub, "<font color='#FD0000'>" + sub + "</font>");

            } catch (Exception e) {
                LogUtil.msg(e.getMessage());
            }

        }

        return phrase.replaceAll("-", "");
    }


    private List<Integer> appearNumber(String srcText, String findText) {
        List<Integer> positions = new ArrayList<>();
        int count = 0;
        int index = 0;
        while ((index = srcText.indexOf(findText, index)) != -1) {
            positions.add(index);
            index = index + findText.length();
            count++;
        }
        positions.add(count);
        return positions;
    }


//    /**
//     * 获取视频缓存的目录
//     *
//     * @return
//     */
//    public String getVideoCacheDir(Context context) {
//        String cachePath = getFileDir(context);//内部缓存无需权限
//        Log.d(TAG, "内部缓存");
//        if (null == cachePath) {
//            if (EasyPermissions.hasPermissions(context, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)) {
//                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
//                    File file = new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + "/.Phonogram/Cache/Video/");
//                    if (!file.exists()) {
//                        file.mkdirs();
//                    }
//                    cachePath = file.getAbsolutePath();
//                    //使用外部缓存
//                    Log.d(TAG, "SD卡缓存");
//                }
//            }
//        }
//        return cachePath;
//    }


    /**
     * 获取临时文件缓存目录
     *
     * @return
     */
    public String getFileDir(Context context) {
        String cacheRootPath = null;
        if (null != context.getFilesDir()) {
            cacheRootPath = context.getFilesDir().getPath();
        } else if (isSdCardAvailable()) {
            if (null != context.getExternalCacheDir()) {
                cacheRootPath = context.getExternalCacheDir().getPath();//SD卡内部临时缓存目录
            }
        } else if (null != context.getCacheDir()) {
            cacheRootPath = context.getCacheDir().getPath();
        } else {
            File cacheDirectory = getCacheDirectory(context, null);
            if (null != cacheDirectory) {
                cacheRootPath = cacheDirectory.getAbsolutePath();
            }
        }
        return cacheRootPath;
    }


    public boolean isSdCardAvailable() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }


    /**
     * 获取应用专属缓存目录
     * android 4.4及以上系统不需要申请SD卡读写权限
     * 因此也不用考虑6.0系统动态申请SD卡读写权限问题，切随应用被卸载后自动清空 不会污染用户存储空间
     *
     * @param context 上下文
     * @param type    文件夹类型 可以为空，为空则返回API得到的一级目录
     * @return 缓存文件夹 如果没有SD卡或SD卡有问题则返回内存缓存目录，否则优先返回SD卡缓存目录
     */
    public File getCacheDirectory(Context context, String type) {
        File appCacheDir = getExternalCacheDirectory(context, type);
        if (appCacheDir == null) {
            appCacheDir = getInternalCacheDirectory(context, type);
        }

        if (appCacheDir == null) {
            Log.e("getCacheDirectory", "getCacheDirectory fail ,the reason is mobile phone unknown exception !");
        } else {
            if (!appCacheDir.exists() && !appCacheDir.mkdirs()) {
                Log.e("getCacheDirectory", "getCacheDirectory fail ,the reason is make directory fail !");
            }
        }
        return appCacheDir;
    }


    /**
     * 获取SD卡缓存目录
     *
     * @param context 上下文
     * @param type    文件夹类型 如果为空则返回 /storage/emulated/0/Android/data/app_package_name/cache
     *                否则返回对应类型的文件夹如Environment.DIRECTORY_PICTURES 对应的文件夹为 .../data/app_package_name/files/Pictures
     *                {@link Environment#DIRECTORY_MUSIC},
     *                {@link Environment#DIRECTORY_PODCASTS},
     *                {@link Environment#DIRECTORY_RINGTONES},
     *                {@link Environment#DIRECTORY_ALARMS},
     *                {@link Environment#DIRECTORY_NOTIFICATIONS},
     *                {@link Environment#DIRECTORY_PICTURES}, or
     *                {@link Environment#DIRECTORY_MOVIES}.or 自定义文件夹名称
     * @return 缓存目录文件夹 或 null（无SD卡或SD卡挂载失败）
     */
    public File getExternalCacheDirectory(Context context, String type) {
        File appCacheDir = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            if (TextUtils.isEmpty(type)) {
                appCacheDir = context.getExternalCacheDir();
            } else {
                appCacheDir = context.getExternalFilesDir(type);
            }

            if (appCacheDir == null) {// 有些手机需要通过自定义目录
                appCacheDir = new File(Environment.getExternalStorageDirectory(), "Android/data/" + context.getPackageName() + "/cache/" + type);
            }

            if (appCacheDir == null) {
                Log.e("getExternalDirectory", "getExternalDirectory fail ,the reason is sdCard unknown exception !");
            } else {
                if (!appCacheDir.exists() && !appCacheDir.mkdirs()) {
                    Log.e("getExternalDirectory", "getExternalDirectory fail ,the reason is make directory fail !");
                }
            }
        } else {
            Log.e("getExternalDirectory", "getExternalDirectory fail ,the reason is sdCard nonexistence or sdCard mount fail !");
        }
        return appCacheDir;
    }


    /**
     * 获取内存缓存目录
     *
     * @param type 子目录，可以为空，为空直接返回一级目录
     * @return 缓存目录文件夹 或 null（创建目录文件失败）
     * 注：该方法获取的目录是能供当前应用自己使用，外部应用没有读写权限，如 系统相机应用
     */
    public File getInternalCacheDirectory(Context context, String type) {
        File appCacheDir = null;
        if (TextUtils.isEmpty(type)) {
            appCacheDir = context.getCacheDir();// /data/data/app_package_name/cache
        } else {
            appCacheDir = new File(context.getFilesDir(), type);// /data/data/app_package_name/files/type
        }

        if (!appCacheDir.exists() && !appCacheDir.mkdirs()) {
            Log.e("getInternalDirectory", "getInternalDirectory fail ,the reason is make directory fail !");
        }
        return appCacheDir;
    }
}
