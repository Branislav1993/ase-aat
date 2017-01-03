package de.tum.aat.model;

import java.io.Serializable;
import java.util.Locale;

/**
 * Created by Naum on 1/2/2017.
 */
public class Exercise implements Serializable {
    public String content;

    @Override
    public String toString() {
        return String.format(Locale.ENGLISH, "%s\n", content);
    }

}
