package com.max.web.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.FieldError;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import javax.validation.executable.ExecutableValidator;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Aspect
@Component
public class RequestParamValidateAspect {
    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final ExecutableValidator validator = factory.getValidator().forExecutables();
    ParameterNameDiscoverer parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
    @Before("(execution(* com..*.controller..*.*(..)))")
    public void before(JoinPoint point) throws SecurityException, ParamValidException {
        Object target = point.getThis();
        Object[] args = point.getArgs();
        Method method = ((MethodSignature) point.getSignature()).getMethod();
        Set<ConstraintViolation<Object>> validResult = validMethodParams(target, method, args);
        List<FieldError> errorList = new ArrayList<FieldError>();
        if (!CollectionUtils.isEmpty(validResult)) {
            String[] parameterNames = parameterNameDiscoverer.getParameterNames(method);
            for (ConstraintViolation<Object> constraintViolation : validResult) {
                PathImpl pathImpl = (PathImpl) constraintViolation.getPropertyPath();
                int paramIndex = pathImpl.getLeafNode().getParameterIndex();
                String paramName = parameterNames[paramIndex];
                if (constraintViolation.getMessageTemplate().contains("{")) {
                    FieldError error = new FieldError(pathImpl.toString(), paramName, constraintViolation.getMessage());
                    errorList.add(error);
                } else {
                    FieldError error = new FieldError(pathImpl.toString(), "", constraintViolation.getMessage());
                    errorList.add(error);
                }
            }
            throw new ParamValidException("", errorList);
        }
    }
    private <T> Set<ConstraintViolation<T>> validMethodParams(T obj, Method method, Object[] params) {
        return validator.validateParameters(obj, method, params);
    }
}
