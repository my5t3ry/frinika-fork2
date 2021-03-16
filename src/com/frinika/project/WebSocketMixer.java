package com.frinika.project;

import javax.KKKsound.sampled.Control;
import javax.sound.sampled.Control.Type;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;

public class WebSocketMixer extends Line
{

  @Override
  public Info getLineInfo() {
    return null;
  }

  @Override
  public void open() throws LineUnavailableException {

  }

  @Override
  public void close() {

  }

  @Override
  public boolean isOpen() {
    return false;
  }

  @Override
  public Control[] getControls() {
    return new Control[0];
  }

  @Override
  public boolean isControlSupported(Type control) {
    return false;
  }

  @Override
  public Control getControl(Type control) {
    return null;
  }

  @Override
  public void addLineListener(LineListener listener) {

  }

  @Override
  public void removeLineListener(LineListener listener) {

  }
}
