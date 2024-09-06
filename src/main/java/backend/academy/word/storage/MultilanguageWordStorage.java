package backend.academy.word.storage;

import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * Мультиязычное хранилище слов(для хранения используется {@link ResourceBundle} в resources
 *
 */
public class MultilanguageWordStorage extends WordsStorage{

    public MultilanguageWordStorage(Locale locale) {
        var bundle = ResourceBundle.getBundle("words.words", locale);
        this.load(converResourceBundleToProperties(bundle));
    }

    protected Properties converResourceBundleToProperties(ResourceBundle bundle) {
        var properties = new Properties();
        for (String key : bundle.keySet()) {
            properties.put(key, bundle.getString(key));
        }
        return properties;
    }
}
