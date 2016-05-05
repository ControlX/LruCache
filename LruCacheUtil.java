import android.content.Context;
import android.graphics.Bitmap;

import java.util.LinkedHashMap;

/**
 * Created by Abhishek Verma on 5/5/2016.
 * <p>
 * Note:
 * - This cache currently supports persisting in objects only and not in filesystem.
 * - Since we want to maintain a order of entries and flush out earlier ones we are making it as LinkedHashMap
 * - There is an expiry time for cache which makes it empty in case timeToLive is reached.
 */
public class LruCacheUtil {
    private LinkedHashMap<String, Bitmap> cacheMapping;
    private long totalTime;
    private Context context;

    public LruCacheUtil(Context context, long expiryTime, int sizeOfCache) {
        //Check for any non-zero value
        validateInitMethodCall(expiryTime, sizeOfCache);
        init(context, expiryTime, sizeOfCache);
    }

    private void init(Context context, long expiryTime, final int sizeOfCache) {
        this.context = context;

        //adding expirytime to current time
        totalTime = System.currentTimeMillis() + expiryTime;

        //remove eldest entry will remove oldest entry on insertion if condition size() >= sizeOfCache is met
        cacheMapping = new LinkedHashMap(sizeOfCache) {
            @Override
            protected boolean removeEldestEntry(Entry eldest) {
                boolean removeStatus = false;
                if (size() >= sizeOfCache) {
                    removeStatus = true;
                    String fileName = resolveFileNameFromUrl((String) eldest.getKey());
                    deleteValueInFileSystem(fileName);
                }
                return removeStatus;
            }
        };
    }

    private void validateCacheExpiryTime() {
        if (System.currentTimeMillis() >= totalTime) {
            cacheMapping.clear();
            //Todo: Delete all files from system with context reference
        }
    }

    private void validateInitMethodCall(long expiryTime, int sizeOfCache) {
        try {
            if (expiryTime <= 0 || sizeOfCache <= 0)
                throw new IllegalArgumentException("expiryTime and sizeOfCache should be a NonZero Positive value");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Idea here is to getExternalStorageDirectory for that application via Context and write our
     * bitmaps into the storage.
     * <p>
     * We will maintain only number [size] specified by user for cache in file system
     */
    private void persistValuesInFileSystem(String fileName) {
        //Todo: getStorage from context of the particular application and persist.
    }

    /**
     * This method call will remove image from file system
     * <p>
     * Fired when <removeEldestEntry> is called or when <validateCacheExpiryTime's> condition is met
     */
    private void deleteValueInFileSystem(String fileName) {
        //Delete file from fileSystem with this particular fileName.
        //Todo: delete the file from system
    }

    /**
     * @param url
     * @return
     */
    private String resolveFileNameFromUrl(String url) {

        return null;
    }

    //====================Public methods to gain access for cache information========================//
    public boolean isPresent(String url) {
        //validate time stamp before proceeding
        validateCacheExpiryTime();

        if (cacheMapping.isEmpty())
            return false;

        return cacheMapping.containsKey(url);
    }

    public Bitmap getCacheValue(String url) {
        validateCacheExpiryTime();

        if (cacheMapping.isEmpty())
            return null;

        return cacheMapping.get(url);
    }

    public void persistCacheValue(String url, Bitmap bitmap) {
        String fileName = resolveFileNameFromUrl(url);
        cacheMapping.put(url, bitmap);
        persistValuesInFileSystem(fileName);
    }
}
