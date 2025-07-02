package team3.kummit.interceptor;

import java.util.UUID;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LogInterceptor implements HandlerInterceptor {

    private static final String LOG_ID = "logId";
    private static final String START_TIME = "startTime";
    private static final String MEMBER_ID = "memberId";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String requestURI = request.getRequestURI();
        String logId = UUID.randomUUID().toString();
        String memberId = request.getParameter(MEMBER_ID) == null ? "0" : request.getParameter(MEMBER_ID);

        request.setAttribute(LOG_ID, logId);
        request.setAttribute(START_TIME, System.currentTimeMillis());

        if (handler instanceof HandlerMethod handlerMethod) {
            log.info("REQUEST [{}][{}][{}] from memberId = {}", logId, requestURI, handlerMethod.getMethod().getName(), memberId);
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) {
        String requestURI = request.getRequestURI();
        String logId = (String) request.getAttribute(LOG_ID);
        Long startTime = (Long) request.getAttribute(START_TIME);
        long duration = System.currentTimeMillis() - startTime;

        log.info("RESPONSE [{}][{}][{}ms]", logId, requestURI, duration);

        if (ex != null) {
            log.error("EXCEPTION [{}][{}]", logId, requestURI, ex);
        }
    }
}
