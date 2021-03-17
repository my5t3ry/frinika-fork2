package com.frinika.project.websocket;

import javax.sound.sampled.Mixer;
import javax.sound.sampled.Mixer.Info;
import javax.sound.sampled.spi.MixerProvider;

public final class WebsocketAudioDeviceProvider extends MixerProvider {

  // STATIC VARIABLES

  /**
   * Set of info objects for all port input devices on the system.
   */
  private static WebsocketAudioDeviceeInfo[] infos;

  /**
   * Set of all port input devices on the system.
   */
  private static WebsocketAudioDevicee[] devices;

  // STATIC

  static {
    // initialize
//    Platform.initialize();
  }

  // CONSTRUCTOR


  /**
   * Required public no-arg constructor.
   */
  public  WebsocketAudioDeviceProvider() {
    synchronized (WebsocketAudioDeviceProvider.class) {
      init();
    }
  }

  private static void init() {
    // get the number of input devices

    infos = new WebsocketAudioDeviceeInfo[1];
    devices = new WebsocketAudioDevicee[1];
    devices[0] = new WebsocketAudioDevicee(new WebsocketAudioDeviceeInfo(0, 0, 1, "WebsoketDevice", "Sash", "Descriptioon",
        "0"));
    // fill in the info objects now.
    infos[0] =new WebsocketAudioDeviceeInfo(0, 0, 1, "WebsoketDevice", "Sash", "Descriptioon",
        "0") ;
  }

  public Mixer.Info[] getMixerInfo() {
    synchronized (WebsocketAudioDeviceProvider.class) {
      Mixer.Info[] localArray = new Mixer.Info[infos.length];
      System.arraycopy(infos, 0, localArray, 0, infos.length);
      return localArray;
    }
  }

  @Override
  public Mixer getMixer(Info info) {
    synchronized (WebsocketAudioDeviceProvider.class) {
      return devices[0];
    }
  }
  /**
   * Info class for DirectAudioDevices.  Adds an index value and a string for making native
   * references to a particular device. This constructor is called from native.
   */
  static final class WebsocketAudioDeviceeInfo extends Mixer.Info {

    private final int index;
    private final int maxSimulLines;

    // For ALSA, the deviceID contains the encoded card index, device index, and sub-device-index
    private final int deviceID;

    protected WebsocketAudioDeviceeInfo(int index, int deviceID, int maxSimulLines,
        String name, String vendor,
        String description, String version) {
      super(name, vendor, "Direct Audio Device: " + description, version);
      this.index = index;
      this.maxSimulLines = maxSimulLines;
      this.deviceID = deviceID;
    }

    int getIndex() {
      return index;
    }

    int getMaxSimulLines() {
      return maxSimulLines;
    }

    int getDeviceID() {
      return deviceID;
    }
  } // class DirectAudioDeviceInfo

  // NATIVE METHODS

}
