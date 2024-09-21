package backend.academy.io.output.storage;

import backend.academy.Main;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class ResourcesOutputStorage extends OutputStorage {

    private final Integer framesCountExclusive = 11;

    public ResourcesOutputStorage() {
        var loader = Main.class.getClassLoader();
        AtomicInteger counter = new AtomicInteger();
        var list = IntStream.range(0, framesCountExclusive)
            .mapToObj(i -> String.format("frames/frame-%d.txt", i))
            .map(loader::getResourceAsStream)
            .peek(stream -> {
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
