package com.github.ittalks.fn.common.result;

import com.github.ittalks.fn.common.advice.exception.ErrorMessage;
import org.springframework.http.HttpStatus;

/**
 * Created by 刘春龙 on 2017/3/9.
 */
public class ErrorCode {

    public static final ErrorMessage BAD_REQUEST = registerErrorMsg("S01001", "未知错误", HttpStatus.BAD_REQUEST);
    public static final ErrorMessage INTERNAL_SERVER_ERROR = registerErrorMsg("S01002", "服务器内部错误", HttpStatus.INTERNAL_SERVER_ERROR);
    public static final ErrorMessage HTTP_CONNECTION_TIMEOUT = registerErrorMsg("C01001", "链接超时", HttpStatus.REQUEST_TIMEOUT);
    public static final ErrorMessage PARAM_INVALID = registerErrorMsg("R01001", "参数不合法", HttpStatus.BAD_REQUEST);
    public static final ErrorMessage PARAM_MISSING = registerErrorMsg("R01002", "缺少必填参数", HttpStatus.BAD_REQUEST);
    public static final ErrorMessage REQ_INVALID = registerErrorMsg("R02001", "非法请求", HttpStatus.BAD_REQUEST);
    public static final ErrorMessage REQ_BIZ = registerErrorMsg("B01001", "业务异常", HttpStatus.INTERNAL_SERVER_ERROR);

    //google
    public static final ErrorMessage GOOGLE_OAUTH2_WEB_FAIL = registerErrorMsg("G01001", "Web服务器应用程序用户授权失败", HttpStatus.INTERNAL_SERVER_ERROR);
    public static final ErrorMessage GOOGLE_OAUTH2_BIND_REPEAT = registerErrorMsg("G01002", "重复授权绑定", HttpStatus.BAD_REQUEST);
    public static final ErrorMessage GOOGLE_OAUTH2_BIND_NONEXIST = registerErrorMsg("G01003", "授权绑定记录不存在", HttpStatus.BAD_REQUEST);
    public static final ErrorMessage GOOGLE_OAUTH2_BIND_ERROR = registerErrorMsg("G01004", "保存授权绑定记录失败", HttpStatus.INTERNAL_SERVER_ERROR);
    public static final ErrorMessage GOOGLE_OAUTH2_UNBIND_ERROR = registerErrorMsg("G01005", "删除授权绑定记录失败", HttpStatus.INTERNAL_SERVER_ERROR);


    private static ErrorMessage registerErrorMsg(String errorCode, String errorMsg, HttpStatus httpStatus) {
        return ErrorMessage.create()
                .setErrorCode(errorCode)
                .setErrorMsg(errorMsg)
                .setHttpStatus(httpStatus);
    }
}
