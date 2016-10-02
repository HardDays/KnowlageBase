package ru.knowledgebase.exceptionmodule.configexceptions;

/**
 * Created by vova on 02.10.16.
 */
public class ConfigParseException extends Error{
    public ConfigParseException(){
        super("Cannot parse config!");
    }

}
