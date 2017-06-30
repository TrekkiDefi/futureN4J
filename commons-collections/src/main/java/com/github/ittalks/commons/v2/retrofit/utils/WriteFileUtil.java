package com.github.ittalks.commons.v2.retrofit.utils;

import com.github.ittalks.commons.v2.retrofit.interfaces.Error;
import com.github.ittalks.commons.v2.retrofit.interfaces.Progress;
import com.github.ittalks.commons.v2.retrofit.interfaces.Success;
import okhttp3.ResponseBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * Created by 刘春龙 on 2017/6/8.
 */
public class WriteFileUtil {

    private static final Logger logger = LoggerFactory.getLogger(WriteFileUtil.class);

    public static void writeFile(ResponseBody body, String path, Progress progress, Success mSuccessCallBack, Error mErrorCallBack) {

        File file = new File(path);

        InputStream inputStream = null;
        OutputStream outputStream = null;

        ProgressInfo progressInfo = new ProgressInfo();

        try {
            progressInfo.total = body.contentLength();
            progressInfo.read = 0;

            byte[] readBuffer = new byte[4096];
            inputStream = body.byteStream();//输入流
            outputStream = new FileOutputStream(file);//输出流

            while (true) {
                int read = inputStream.read(readBuffer);
                if (read == -1) {
                    break;
                }
                outputStream.write(readBuffer, 0, read);
                progressInfo.read += read;
                logger.info("Download progress：" + progressInfo.read * 1.0 / progressInfo.total * 100 + "%");
            }

            mSuccessCallBack.Success(path);
            outputStream.flush();
        } catch (IOException e) {
            mErrorCallBack.Error(e);
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
            }
        }
    }

    protected static class ProgressInfo {
        public long read = 0;
        public long total = 0;
    }
}
