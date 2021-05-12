package io.github.portlek.workload;

import org.jetbrains.annotations.NotNull;

public interface Player {

  @NotNull
  String getName();

  void sendMessage(@NotNull String message);
}
