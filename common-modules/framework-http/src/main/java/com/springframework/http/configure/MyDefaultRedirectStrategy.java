package com.springframework.http.configure;

/**
 * @author summer
 * 2018/8/8
 */
@Configuration
public class MyDefaultRedirectStrategy {
    private static final transient Logger DB_LOGGER = LoggerFactory.getLogger(MyDefaultRedirectStrategy.class);

    @Bean("defaultRedirectStrategy")
    public DefaultRedirectStrategy defaultRedirectStrategy() {
        return new DefaultRedirectStrategy() {
            @Override
            public boolean isRedirected(HttpRequest request, HttpResponse response, HttpContext context) {
                boolean isRedirect = false;
                try {
                    isRedirect = super.isRedirected(request, response, context);
                } catch (ProtocolException e) {
                    DB_LOGGER.error("isRedirected:" + e);
                }
                if (!isRedirect) {
                    int responseCode = response.getStatusLine().getStatusCode();
                    if (responseCode == /*301*/HttpStatus.SC_MOVED_PERMANENTLY || responseCode == /*302*/HttpStatus.SC_MOVED_TEMPORARILY) {
                        return true;
                    }
                }
                return isRedirect;
            }

        };
    }

}
