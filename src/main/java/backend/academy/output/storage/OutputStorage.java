package backend.academy.output.storage;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;


public class OutputStorage {


    protected final List<List<String>> frames = new LinkedList<>();

    public void load(List<List<String>>  frames){
        this.frames.addAll(frames);
    }

    public void loadFromFiles(String ... filePaths){
        loadFromFiles(Arrays.stream(filePaths)
        .map(File::new).toList());
    }

    public void loadFromFiles(List<File> files){
        loadFromFiles(files.stream()
            .filter(File::exists)
            .map(File::toPath)
            .toList());


    }

    public void loadFromFiles(Collection<Path> paths){
        paths.stream()
            .map(this::getContentFromFile)
            .filter((list)-> Optional.ofNullable(list).isPresent())
            .forEach(frames::add);
    }



    /**
     * Читает весь файл в кодировке UTF-8
     * @param file путь до файла
     * @return список строк иначе null
     */
    private List<String> getContentFromFile(Path file){
        try {
            return Files.readAllLines(file, StandardCharsets.UTF_8);
        } catch (IOException e) {
            return null;
        }
    }

    public List<String> getFrame(int index){
        if(frames.size() > index && index>=0){
            return frames.get(index);
        }else {
            return frames.get(frames.size()-1);
        }
    }

    public Integer getFramesCount(){
        return frames.size();
    }


}
