module beastbook.rest {
  exports beastbook.client;
  requires beastbook.core;
  requires spring.web;
  requires spring.beans;
  requires spring.boot;
  requires spring.boot.autoconfigure;
  requires spring.context;

  opens beastbook.server to spring.beans, spring.context, spring.web;
}