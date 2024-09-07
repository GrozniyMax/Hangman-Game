package backend.academy.word.storage;

import backend.academy.Main;
import lombok.Getter;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * Мультиязычное хранилище слов(для хранения используется {@link ResourceBundle} в resources
 *
 */
public class MultilanguageWordStorage extends WordsStorage{

    private static final Map<Locale, Character> supportedLanguages = Map
        .of(Locale.of("ru"), 'р',
            Locale.ENGLISH, 'j');

    @Getter
    private Locale locale;

    public MultilanguageWordStorage(Locale locale) {
        this.locale = locale;
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

    public static boolean hasLanguage(Locale locale) {
        return supportedLanguages.containsKey(locale);
    }

    public static Character.UnicodeBlock ofLanguage(Locale locale) {
        if (!hasLanguage(locale)) return null;
        return Character.UnicodeBlock.of(supportedLanguages.get(locale));
    }

    @Override
    public Locale getCurrentLocale() {
        return locale;
    }
}
