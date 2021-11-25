package beastbook.core;

/**
 * Interface used for core classes with ids.
 */
public interface IdClasses {
  String getName();

  String getId();

  void setId(String id) throws Exceptions.IllegalIdException;
}
