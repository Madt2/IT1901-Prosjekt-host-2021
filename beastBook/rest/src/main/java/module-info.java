module beastbook.rest {
  //exports beastbook.restapi;
  exports beastbook.restserver;
  //exports beastbook;
  requires javafx.controls;
  requires javafx.fxml;
  requires beastbook.core;
  requires spring.context;
  requires spring.web;
  requires spring.beans;
  requires spring.boot;
  requires spring.boot.autoconfigure;
}