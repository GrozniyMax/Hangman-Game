package backend.academy.io.output.storage;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class ResourcesOutputStorage extends OutputStorage {

    public ResourcesOutputStorage() {
        var loader = Main.class.getClassLoader();
        var list = IntStream.range(0, 11)
            .mapToObj(i -> String.format("not commited yet/resources/frames/frame-%d.txt", i))
            .map(loader::getResourceAsStream)
            .filter(Objects::nonNull)
            .map(InputStreamReader::new)
            .map(BufferedReader::new)
            .map(BufferedReader::lines)
            .map(Stream::toList)
                .toList();
        load(list);

    }


}
