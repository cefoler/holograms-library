package com.cefoler.holograms.exception;

public final class HologramException extends RuntimeException {

  public HologramException() {
  }

  public HologramException(final String message) {
    super(message);
  }

  public HologramException(final String message, final Throwable cause) {
    super(message, cause);
  }

}
