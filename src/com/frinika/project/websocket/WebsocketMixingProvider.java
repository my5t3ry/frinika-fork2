package com.frinika.project.websocket;

import com.sun.media.sound.SoftMixingMixer;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.Mixer.Info;
import javax.sound.sampled.spi.MixerProvider;

public class WebsocketMixingProvider extends MixerProvider {

  static WebsocketMixer globalmixer = null;

  static Thread lockthread = null;

  static final Object mutex = new Object();

  public Mixer getMixer(Info info) {
    if (!(info == null || info == WebsocketMixer.info)) {
      throw new IllegalArgumentException("Mixer " + info.toString()
          + " not supported by this provider.");
    }
    synchronized (mutex) {
      if (lockthread != null)
        if (Thread.currentThread() == lockthread)
          throw new IllegalArgumentException("Mixer "
              + info.toString()
              + " not supported by this provider.");
      if (globalmixer == null)
        globalmixer = new WebsocketMixer();
      return globalmixer;
    }

  }

  public Info[] getMixerInfo() {
    return new Info[] { WebsocketMixer.info };
  }


}
