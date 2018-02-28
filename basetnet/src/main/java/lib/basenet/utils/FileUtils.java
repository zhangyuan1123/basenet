package lib.basenet.utils;

import android.text.TextUtils;
import android.webkit.MimeTypeMap;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by zhaoyu1 on 2017/3/9.
 */

public final class FileUtils {
    public static final void saveFile(InputStream is, File file) throws IOException {
        InputStream inputStream = is;
        if (null != inputStream) {
            FileOutputStream fos = null;
            int len = 0;
            try {
                byte[] bys = new byte[4 * 1024];
                fos = new FileOutputStream(file);
                while ((len = inputStream.read(bys)) != -1) {
                    fos.write(bys, 0, len);
                }
                fos.flush();
            } finally {
                if (fos != null) {
                    fos.close();
                }
            }
        }
    }

    public static final void saveFile(byte[] bytes, File file) throws IOException {
        if (bytes != null) {
            FileOutputStream fos = new FileOutputStream(file);
            //byte[] bys = new byte[4 * 1024];
            fos.write(bytes);
            fos.flush();
            fos.close();
        }
    }

    public static String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        if(TextUtils.isEmpty(type)) {
            type = "application/octet-stream";
        }
        return type;
    }

    public static void  deleteFile(String path) {
        File file = new File(path);
        if(file.exists()) {
            file.delete();
        }
    }

}
