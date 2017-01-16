package de.tum.aat.model;

import java.io.Serializable;

/**
 * Created by Naum on 1/16/2017.
 */
public class BadRequestError implements Serializable {

    public String cause;
    public String[] stackTrace;
    public String message;
    public String localizedMessage;
    public String[] suppressed;

}
