package com.github.ittalks.fn.common.interceptor;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * Created by 刘春龙 on 2017/6/2.
 *
 * 基于JWT的认证拦截器
 */
public class SecurityInterceptor extends HandlerInterceptorAdapter {

    /*
    private static String signSecret = null;
    private static List<String> whiteList;//白名单

    public SecurityInterceptor() throws IOException {

        super();
        Properties properties = new Properties();
        properties.load(SecurityInterceptor.class.getClassLoader()
                .getResourceAsStream("common/signature.properties"));
        signSecret = (String) properties.get("signSecret");
        if (signSecret.isEmpty()) {
            throw new IOException("the signature can not be empty");
        }
        whiteList = getWhiteList();
    }

    public static List<String> getWhiteList() {
        Properties properties = new Properties();
        try {
            properties.load(SecurityInterceptor.class.getClassLoader()
                    .getResourceAsStream("common/verify.properties"));
            String whiteList = (String) properties.get("white_list");
            if (StringUtils.isEmpty(whiteList))
                throw new IOException("no white list");
            return Arrays.asList(whiteList.split(","));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<String>();
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Skip skip = handlerMethod.getMethodAnnotation(Skip.class);
            if (skip != null) {
                return true;
            }
            if (whiteList != null && whiteList.size() > 0) {
                Inner inner = handlerMethod.getMethodAnnotation(Inner.class);
                if (inner != null) {
                    boolean legal = verifyInner(request);
                    if (legal)
                        return true;
                }
            }
        }


    }

    private boolean verifyInner(HttpServletRequest request) {
        String ipAddress = IpUtil.getTrueIp(request);
        return whiteList.contains(ipAddress);
    }

    private boolean checkSign(HttpServletRequest request, HttpServletResponse response)
            throws NoSuchAlgorithmException {
        String clientId = request.getParameter("client");
        String clientVersion = request.getParameter("clientVersion");
        String uuid = request.getParameter("uuid");
        String timestamp = request.getParameter("time");

        if (StringUtils.isEmpty(clientId) ||
                StringUtils.isEmpty(clientVersion) ||
                StringUtils.isEmpty(uuid) ||
                StringUtils.isEmpty(timestamp)) {
            throw new RequiredParameterException();
        }
    }
    */
}
