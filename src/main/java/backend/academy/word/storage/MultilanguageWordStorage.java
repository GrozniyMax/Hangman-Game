package backend.academy.word.storage;

import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;
import lombok.Getter;

/**
 * Мультиязычное хранилище слов(для хранения используется {@link ResourceBundle} в resources
 */
public class MultilanguageWordStorage extends WordsStorage {

    private static final Map<Locale, Character> LOCALE_TO_CHAR_OF_ALPHABET = Map
        .of(Locale.of("ru"), 'р',
            Locale.ENGLISH, 'j');

    @Getter
    private Locale locale;

    public MultilanguageWordStorage(Locale locale, String resourcePath) {
        this.locale = LOCALE_TO_CHAR_OF_ALPHABET.containsKey(locale) ? locale : Locale.ENGLISH;
        var bundle = ResourceBundle.getBundle(resourcePath, this.locale);
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
        return LOCALE_TO_CHAR_OF_ALPHABET.containsKey(locale);
    }

    public static Character.UnicodeBlock ofLanguage(Locale locale) {
        if (!hasLanguage(locale)) {
            return null;
        }
        return Character.UnicodeBlock.of(LOCALE_TO_CHAR_OF_ALPHABET.get(locale));
    }

    @Override
    public Locale getCurrentLocale() {
        return locale;
    }
}
