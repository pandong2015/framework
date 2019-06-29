package tech.pcloud.framework.springboot.web.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * Created by pandong on 17-7-11.
 */
@Slf4j
@Aspect
@Component
public class LogContentAspect {

    @Pointcut("(@annotation(tech.pcloud.framework.springboot.web.aop.Log) || @within(tech.pcloud.framework.springboot.web.aop.Log)) && !@annotation(tech.pcloud.framework.springboot.web.aop.UnLog) ")
    public void pointcut() {
    }

    /**
     * aop around log cost time
     *
     * @param pjp pointcut
     * @return result
     * @throws Throwable ProceedingJoinPoint.proceed() throwable
     */
    @Around("pointcut()")
    public Object time(ProceedingJoinPoint pjp) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object o = pjp.proceed(pjp.getArgs());
        log.info(pjp.getSignature().toLongString()
                + ": exec time --> " + (System.currentTimeMillis() - startTime) + "ms");
        return o;
    }
}
