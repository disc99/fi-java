package fi.java.processor;

import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.IntStream;

public class Java8 {

    @SneakyThrows
    public static void main(String[] args) {
        String root = "./java8/src/main/java/fi";
        FileUtils.deleteDirectory(Paths.get(root).toFile());
        List<Class<?>> classes = Arrays.asList(
                Function.class,
                Supplier.class,
                Consumer.class,
                Predicate.class
        );

        Processor processor = new Processor(root);
        IntStream.rangeClosed(3, 22).forEach(n -> classes.forEach(c -> processor.create(c, n)));
    }
}
