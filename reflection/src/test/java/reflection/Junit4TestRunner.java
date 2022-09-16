package reflection;

import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

class Junit4TestRunner {

    @Test
    void run() throws Exception {
        Junit4Test junit4Test = new Junit4Test();
        Class<Junit4Test> clazz = Junit4Test.class;

        // TODO Junit4Test에서 @MyTest 애노테이션이 있는 메소드 실행
        Method[] declaredMethods = clazz.getDeclaredMethods();

        for (Method method : declaredMethods) {
            MyTest declaredAnnotation = method.getDeclaredAnnotation(MyTest.class);
            if (declaredAnnotation == null) {
                return;
            }
            method.invoke(junit4Test);
        }
    }
}
