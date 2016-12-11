package ji.java.processor;

import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.IntStream;

import static java.util.Collections.singletonList;

public class ProcessorTest {

    @Test
    public void test() throws Exception {
        FileUtils.deleteDirectory(Paths.get("../java8/src/main/java/fi").toFile());

        IntStream.rangeClosed(3, 5).forEach(n -> Arrays.asList(
                Function.class,
                Supplier.class,
                Consumer.class,
                Comparable.class
                ).forEach(c -> create(c, n)));

    }

    @SneakyThrows
    void create(Class<?> clazz, int num) {
        String fi = new Processor().convert(clazz, num);
        System.out.println(fi);

        String packageName = clazz.getPackage().getName().replaceAll("\\.", "/");
        String className = clazz.getSimpleName();
        String dirName = "../java8/src/main/java/fi/" + packageName;
        Path dir = Paths.get(dirName);
        Path file = Paths.get(dirName + "/" + className + num + ".java");

        Files.createDirectories(dir);
        Files.createFile(file);
        Files.write(file, singletonList(fi));
    }

}
