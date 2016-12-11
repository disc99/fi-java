package ji.java.processor;


import com.github.jknack.handlebars.Handlebars;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.Value;

import java.lang.reflect.Method;
import java.util.stream.Collector;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;

public class Processor {
    Collector<CharSequence, ?, String> byComma = joining(", ");

    @SneakyThrows
    String convert(Class<?> clazz, int num) {
        Fi fi = new Fi();
        Method method = clazz.getMethods()[0];
        ReturnType returnType = new ReturnType(method.getGenericReturnType().getTypeName());
        ParamType paramType = new ParamType(method.getParameterCount() >= 1
                ? method.getGenericParameterTypes()[0].getTypeName() : "");

        fi.packageName = clazz.getPackage().getName();
        fi.className = clazz.getSimpleName();
        fi.num = num;
        fi.generics = Stream.concat(paramType.steam(num), returnType.stream()).collect(byComma);
        fi.returnType = returnType.val();
        fi.methodName = method.getName();
        fi.methodParams = paramType.steam(num).map(t -> t + " " + t.toLowerCase()).collect(byComma);

        return new Handlebars().compile("fi").apply(fi);
    }

    @Getter
    static class Fi {
        String packageName;
        String className;
        int num;
        String generics;
        String returnType;
        String methodName;
        String methodParams;
    }

    @Value
    static class ParamType implements Type {
        // "" or primitive or type parameter
        String val;

        Stream<String> steam(int num) {
            return isGenerics() ? IntStream.rangeClosed(1, num).mapToObj(n -> val+n) : Stream.empty();
        }

        @Override
        public String val() {
            return val;
        }
        @Override
        public boolean isGenerics() {
            return val.length() == 1;
        }
    }
    @Value
    static class ReturnType implements Type {
        // "" or primitive or type parameter
        String val;

        Stream<String> stream() {
            return isGenerics() ? Stream.of(val) : Stream.empty();
        }

        @Override
        public String val() {
            return val;
        }
        @Override
        public boolean isGenerics() {
            return val.length() == 1;
        }
    }

    interface Type {
        String val();
        boolean isGenerics();
    }
}

