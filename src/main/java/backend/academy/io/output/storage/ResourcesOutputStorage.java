package backend.academy.io.output.storage;

import backend.academy.Main;
import lombok.extern.log4j.Log4j2;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;
import java.util.stream.Stream;


@Log4j2
public class ResourcesOutputStorage extends OutputStorage {

    public ResourcesOutputStorage() {
        var loader = Main.class.getClassLoader();
        AtomicInteger counter = new AtomicInteger();
        var list = IntStream.range(0, 11)
            .mapToObj(i -> String.format("frames/frame-%d.txt", i))
            .map(loader::getResourceAsStream)
            .peek(stream->{
                if (Objects.isNull(stream)) {
                    log.warn("No frame {} found", counter.getAndIncrement());
                }
            })
            .filter(Objects::nonNull)
            .map(InputStreamReader::new)
            .map(BufferedReader::new)
            .map(BufferedReader::lines)
            .map(Stream::toList)
                .toList();
        load(list);

    }


}
