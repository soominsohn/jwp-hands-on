package nextstep.study.di.stage3.context;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Set;
import java.util.stream.Collectors;
import nextstep.study.ConsumerWrapper;
import nextstep.study.FunctionWrapper;

/**
 * 스프링의 BeanFactory, ApplicationContext에 해당되는 클래스
 */
class DIContainer {

    private final Set<Object> beans;

    public DIContainer(final Set<Class<?>> classes) throws IllegalAccessException {
        this.beans = classes.stream()
                .map(FunctionWrapper.apply(Class::getDeclaredConstructor))
                .peek(constructor -> constructor.setAccessible(true))
                .map(FunctionWrapper.apply(Constructor::newInstance))
                .collect(Collectors.toSet());
        setFields();
    }

    private void setFields() throws IllegalAccessException {
        for (Object bean : beans) {
            Field[] fields = bean.getClass().getDeclaredFields();
            injectFields(fields, bean);

        }
    }

    private void injectFields(Field[] fields, Object bean) throws IllegalAccessException {
        for (Field field : fields) {
            field.setAccessible(true);
            beans.stream()
                    .filter(it -> field.getType().isInstance(it))
                    .forEach(ConsumerWrapper.accept(targetBean -> field.set(bean, targetBean)));
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> aClass) {
        return beans.stream()
                .filter(it -> it.getClass().equals(aClass))
                .findFirst()
                .map(it -> (T) it)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 bean이 없습니다."));
    }
}
