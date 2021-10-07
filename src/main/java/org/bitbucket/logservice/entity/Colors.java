package org.bitbucket.logservice.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Colors {
  GREEN("#32CD32"),
  BLUE("#1E90FF"),
  ORANGE("#FFD700"),
  RED("#FF0000"),
  GREY("#DDDDDD");

  private final String message;
}
