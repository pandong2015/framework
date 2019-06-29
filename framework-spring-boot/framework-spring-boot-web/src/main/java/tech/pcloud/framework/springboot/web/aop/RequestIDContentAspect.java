package tech.pcloud.framework.springboot.web.aop;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.util.IdGenerator;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import tech.pcloud.framework.utility.common.IdGenerate;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Enumeration;

/**
 * Created by pandong on 17-7-11.
 */
@Slf4j
@Aspect
@Component
public class RequestIDContentAspect {
    public static final String REQUEST_ID = "request_id";

    @Pointcut(value = "(@annotation(org.springframework.web.bind.annotation.RequestMapping)" +
            " || @annotation(org.springframework.web.bind.annotation.PostMapping)" +
            " || @annotation(org.springframework.web.bind.annotation.GetMapping)" +
            " || @annotation(org.springframework.web.bind.annotation.PutMapping)" +
            " || @annotation(org.springframework.web.bind.annotation.DeleteMapping)) " +
            " && !@annotation(tech.pcloud.framework.springboot.web.aop.UnLog)")
    public void pointcut() {
    }

    /**
     * before request add "REQUEST_ID" to MDC and display request log
     *
     * @param point pointcut
     */
    @Before(value = "pointcut()")
    public void before(JoinPoint point) {
        MDC.put(REQUEST_ID, String.valueOf(IdGenerate.generate(0)));
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String beanName = point.getSignature().getDeclaringTypeName();
        String methodName = point.getSignature().getName();
        String uri = request.getRequestURI();
        String method = request.getMethod();
        StringBuffer stringBuilder = new StringBuffer();
        stringBuilder.append("request url : ").append("[").append(method).append("]").append(uri)
                .append(", exec method : ").append(beanName).append(".").append(methodName).append("()")
                .append(", request parameter : [");
        Arrays.stream(point.getArgs())
                .filter(o -> o != null).forEach(o -> stringBuilder.append(o.toString()).append(","));
        stringBuilder.delete(stringBuilder.length() - 1, stringBuilder.length());
        stringBuilder.append("]");
        stringBuilder.append(" , request headers : [");
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            stringBuilder.append("{").append(name).append(":").append(request.getHeader(name)).append("},");
        }
        stringBuilder.delete(stringBuilder.length() - 1, stringBuilder.length());
        stringBuilder.append("]");
        log.info(stringBuilder.toString());
    }

    /**
     * aop around log request cost time
     *
     * @param pjp pointcut
     * @return result
     * @throws Throwable ProceedingJoinPoint.proceed() throwable
     */
    @Around(value = "pointcut()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object o = pjp.proceed(pjp.getArgs());
        log.info("request exec time --> " + (System.currentTimeMillis() - startTime) + "ms");
        return o;
    }

    /**
     * clear MDC
     *
     * @param point pointcut
     */
    @AfterReturning(value = "pointcut()")
    public void after(JoinPoint point) {
        MDC.clear();
    }
}
