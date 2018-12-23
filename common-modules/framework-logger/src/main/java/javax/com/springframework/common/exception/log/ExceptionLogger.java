package javax.com.springframework.common.exception.log;

import com.google.common.base.Strings;
import com.springframework.constants.Constant;
import com.springframework.exception.BaseException;
import com.springframework.exception.BaseKnownException;
import com.springframework.log.util.RequestUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.concurrent.ExecutionException;

/**
 * Created by kainlin on 2015/10/18.
 */
public class ExceptionLogger {

    private static final Logger exLogger = LoggerFactory.getLogger(Constant.EXCPETION_LOGGER);

    private static final int MAX_LEVEL = 3;

    private static boolean supportNestedServletException;

    static {
        try {
            Class.forName("org.springframework.web.util.NestedServletException");
            supportNestedServletException = true;
        } catch (NoClassDefFoundError | ClassNotFoundException ex) {
            supportNestedServletException = false;
        }
    }

    private static void logBaseException(BaseException ex, HttpServletRequest request) {
        Throwable cause = ex.getCause() == null ? ex : ex.getCause();

        new ExceptionLogObject(
//                request != null ? RequestUtils.getFullURL(request) : Constant.EMPTY_STRING,
                request != null ? filterPwd(request) : Constant.EmptyString,
                ex instanceof BaseKnownException ? Constant.LOGLEVEL_WARN : Constant.LOGLEVEL_ERROR,
                request != null ? RequestUtils.getRemoteIP(request) : Constant.EmptyString,
                new Date(),
                cause.getClass().getName(),
                ExceptionUtils.getStackTrace(cause),
                cause.getMessage(),
                request != null ? RequestUtils.getHeadersAndBody(request) : Constant.EmptyString,
                ex.getHttpCode(),
                ex.getErrorCode(),
                ex.getErrorMessage(),
                ex.getInternalErrorCode(),
                ex.getInternalErrorMessage(),
                ex.getCustomInfo()).log(exLogger);
    }

    public static Exception tryGetBaseException(Exception ex, int maxLevel) {
        if (maxLevel <= 0) {
            return ex;
        }

        if (ex instanceof BaseException) {
            return ex;
        }

        if ((ex.getClass() == RuntimeException.class || ex.getClass() == ExecutionException.class
                || (supportNestedServletException && ex.getClass() == org.springframework.web.util.NestedServletException.class)
                || ex.getClass() == java.lang.reflect.InvocationTargetException.class) //如果通过反射执行的，需要特别处理
                && ex.getCause() != null) {
            Throwable innerEx = ex.getCause();
            if (innerEx instanceof Exception) {
                return tryGetBaseException((Exception) innerEx, maxLevel - 1);
            }
        }

        return ex;

    }

    public static void logAndThrow( BaseException ex) throws BaseException {
        if (exLogger != null) {
            logBaseException(ex, null);
        }
        throw ex;
    }

    public static void logAndThrow( BaseException ex, HttpServletRequest request) throws BaseException {
        if (exLogger != null) {
            logBaseException(ex, request);
        }
        throw ex;
    }

    public static Exception log(Exception ex) {
        return log(ex, null);
    }

    public static Exception log(Exception ex, HttpServletRequest request) {

        ex = tryGetBaseException(ex, MAX_LEVEL);
        if (exLogger != null) {


            if (ex instanceof BaseException) {
                logBaseException((BaseException) ex, request);
            } else //unhandled exception
            {
                new ExceptionLogObject(
                        request != null ? RequestUtils.getFullURL(request) : Constant.EmptyString,
                        Constant.LOGLEVEL_ERROR,
                        request != null ? RequestUtils.getRemoteIP(request) : Constant.EmptyString,
                        new Date(),
                        ex.getClass().getName(),
                        ExceptionUtils.getStackTrace(ex),
                        ex.getMessage(),
                        request != null ? RequestUtils.getHeadersAndBody(request) : Constant.EmptyString,
                        Constant.HTTPCODE_INTERNAL_SERVER_ERROR,
                        Constant.ERRORCODE_INTERNAL_SERVER_ERROR,
                        Constant.ERRORMESSAGE_INTERNAL_ERROR,
                        Constant.ERRORCODE_INTERNAL_SERVER_ERROR,
                        Constant.ERRORMESSAGE_INTERNAL_ERROR,
                        null).log(exLogger);
            }
        }
        return ex;
    }

    /**
     * 日志打印过滤敏感信息
     *
     * @param request
     * @return
     */
    private static String filterPwd(HttpServletRequest request) {
        String fullUrl = RequestUtils.getFullURL(request);
        String returnStr = fullUrl;
        try {
            String[] filterPara = {"pwd"};//需要过滤的参数
            for (String curFilterPara : filterPara) {
                String pwdValue = request.getParameter(curFilterPara);
                if (!Strings.isNullOrEmpty(pwdValue)) {
                    returnStr = returnStr.replace(pwdValue, "******");
                }
            }
        } catch (Exception e) {
            return returnStr;
        }
        return returnStr;
    }
}
