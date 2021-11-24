module beastbook.core {
  requires com.fasterxml.jackson.core;
  requires transitive com.fasterxml.jackson.databind;
  exports beastbook.core;
  exports beastbook.json;
  exports beastbook.json.internal;
}
