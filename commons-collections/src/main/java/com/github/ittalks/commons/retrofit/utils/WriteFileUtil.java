package com.github.ittalks.commons.retrofit.utils;

import com.github.ittalks.commons.retrofit.interfaces.Error;
import com.github.ittalks.commons.retrofit.interfaces.Progress;
import com.github.ittalks.commons.retrofit.interfaces.Success;
import okhttp3.ResponseBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.math.BigDecimal;

/**
 * Created by 刘春龙 on 2017/7/19.
 */
public class WriteFileUtil {

    private static final Logger logger = LoggerFactory.getLogger(WriteFileUtil.class);

    public static void writeFile(ResponseBody body, String path, Progress progress, Success mSuccessCallBack, Error mErrorCallBack) {
        File file = new File(path);

        InputStream is = null;
        OutputStream os = null;

        ProgressInfo progressInfo = new ProgressInfo();

        try {

            // 初始化进度
            progressInfo.total = body.contentLength();
            progressInfo.read = 0;

            byte[] byteBuffer = new byte[4096];// 字节缓冲
            is = body.byteStream();//输入流
            os = new FileOutputStream(file);//输出流

            while (true) {
                int read = is.read(byteBuffer);
                if (read == -1) {
                    break;
                }
                os.write(byteBuffer, 0, read);
                progressInfo.read += read;

                BigDecimal pread = new BigDecimal(progressInfo.read);
                BigDecimal ptotal = new BigDecimal(progressInfo.total);
                double percent = pread.multiply(new BigDecimal(100)).divide(ptotal, 3, BigDecimal.ROUND_HALF_UP).doubleValue();
                logger.info("download progress：" + percent + "%");
            }

            os.flush();
            mSuccessCallBack.Success(path);

        } catch (IOException e) {
            mErrorCallBack.Error(e);
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 进度
     */
    protected static class ProgressInfo {
        long read = 0;
        long total = 0;
    }
}
