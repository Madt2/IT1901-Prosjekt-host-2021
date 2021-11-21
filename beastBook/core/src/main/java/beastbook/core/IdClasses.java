package beastbook.core;

public interface IdClasses {
    String getName();

    String getId();

    void setId(String id) throws Exceptions.IllegalIdException;
}
