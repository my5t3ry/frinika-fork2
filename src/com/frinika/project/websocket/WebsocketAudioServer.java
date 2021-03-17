// Copyright (C) 2007 Steve Taylor.
// Distributed under the Toot Software License, Version 1.0. (See
// accompanying file LICENSE_1_0.txt or copy at
// http://www.toot.org/LICENSE_1_0.txt)

package com.frinika.project.websocket;

import com.frinika.project.websocket.WebsocketAudioDevicee.DirectDL;
import com.frinika.project.websocket.WebsocketAudioDevicee.DirectSDL;
import com.frinika.project.websocket.WebsocketAudioDevicee.DirectTDL;
import com.frinika.toot.PriorityAudioServer;
import com.sun.media.sound.SoftMixingMixer;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioFormat.Encoding;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;
import javax.sound.sampled.Mixer.Info;
import uk.org.toot.audio.core.AudioBuffer;
import uk.org.toot.audio.core.ChannelFormat;
import uk.org.toot.audio.core.AudioBuffer.MetaInfo;
import uk.org.toot.audio.server.AbstractAudioServer;
import uk.org.toot.audio.server.AudioLine;
import uk.org.toot.audio.server.IOAudioProcess;
import uk.org.toot.audio.server.JavaSoundAudioServer;

/**
 * An Audiothis that adapts any other Audiothis to add a non-real-time capability.
 */
public class WebsocketAudioServer
    extends AbstractAudioServer {

  private byte[] sharedByteBuffer;
  private int sampleSizeInBits = 16;
  private float sampleRate = 44100.0F;
  private AudioFormat format;
  private List<WebsocketAudioServer.WebsocketAudioOutput> outputs;
  private List<WebsocketAudioServer.WebsockeAudioInput> inputs;
  private int lineBufferBytes = 32768;
  private Mixer mixer ;
  private boolean running = false;

  public WebsocketAudioServer() {
    bufferFrames = 128;
    this.format = new AudioFormat(Encoding.PCM_SIGNED,44100,16,2,4,44100,false);
    this.outputs = new ArrayList();
    this.mixer = new WebsocketAudioDeviceProvider().getMixer(null);
    this.inputs = new ArrayList();
  }

  protected void checkFormat() {
    if (this.format == null) {
      this.lineBufferBytes = this.format.getFrameSize() * (int) this.getSampleRate() / 5;
      this.sharedByteBuffer = this.createByteBuffer();
    }
  }

  public int getSampleSizeInBits() {
    return this.sampleSizeInBits;
  }

  public void setSampleSizeInBits(int sampleSizeInBits) {
    if (this.format != null) {
      throw new IllegalStateException("too late, format already set");
    } else {
      this.sampleSizeInBits = sampleSizeInBits;
    }
  }

  public float getSampleRate() {
    return this.sampleRate;
  }

  public void setSampleRate(float sampleRate) {
    if (this.format != null) {
      throw new IllegalStateException("too late, format already set");
    } else {
      this.sampleRate = sampleRate;
    }
  }


  protected void resizeBuffers(int bufferFrames) {
    super.resizeBuffers(bufferFrames);
    this.sharedByteBuffer = this.createByteBuffer();
  }

  protected byte[] createByteBuffer() {
    this.checkFormat();
    byte[] ret = new byte[this._createAudioBuffer("hack").getByteArrayBufferSize(this.format)];
    return ret;
  }

  public int getOutputLatencyFrames() {
    return 128;
  }

  @Override
  public int getTotalLatencyFrames() {
    return 0;
  }

  public int getInputLatencyFrames() {
    return this.inputs.size() == 0 ? 0
        : ((WebsockeAudioInput) this.inputs.get(0)).getLatencyFrames();
  }

  @Override
  public boolean isRunning() {
    return this.running;
  }

  public List<String> getAvailableOutputNames() {

    return Arrays.asList( mixer.getMixerInfo().getName());
  }

  public List<String> getAvailableInputNames() {
//
    return Arrays.asList( mixer.getMixerInfo().getName());
  }

  protected Info inputForName(String name) {
    return mixer.getMixerInfo();
  }

  protected Info outputForName(String name) {
    return mixer.getMixerInfo();
  }

  protected void startImpl() {
    Iterator i$ = this.inputs.iterator();

    WebsocketAudioOutput output;
    while (i$.hasNext()) {
      output = (WebsocketAudioOutput) i$.next();

      try {
        output.start();
      } catch (Exception var5) {
        var5.printStackTrace();
      }
    }

    i$ = this.outputs.iterator();

    while (i$.hasNext()) {
      output = (WebsocketAudioOutput) i$.next();

      try {
        output.start();
      } catch (Exception var4) {
        var4.printStackTrace();
      }
    }

    running =true;
  }

  protected void stopImpl() {

    Iterator i$ = this.outputs.iterator();

    WebsockeAudioInput input;
    while (i$.hasNext()) {
      input = (WebsockeAudioInput) i$.next();

      try {
        input.stop();
      } catch (Exception var5) {
        var5.printStackTrace();
      }
    }

    i$ = this.inputs.iterator();

    while (i$.hasNext()) {
      input = (WebsockeAudioInput) i$.next();

      try {
        input.stop();
      } catch (Exception var4) {
        var4.printStackTrace();
      }
    }

  }

  public IOAudioProcess openAudioOutput(String name, String label) throws Exception {
    boolean wasRunning = this.running;
    this.checkFormat();
    if (this.running) {
      this.stop();
    }

    if (name == null) {
      name = (String) this.getAvailableOutputNames().get(0);
      System.out.println(label + " null name specified, using " + name);
    }

    WebsocketAudioOutput output;
    try {
      output = new WebsocketAudioOutput(this.format, this.outputForName(name), label);
      output.open();
      this.outputs.add(output);
    } finally {
      if (wasRunning) {
        this.start();
      } else {
        this.checkStart();
      }

    }

    return output;
  }

  public void closeAudioOutput(IOAudioProcess output) {
    if (!(output instanceof WebsocketAudioOutput)) {
      throw new IllegalArgumentException(output.getName() + " is not a JavaSoundAudioOutput");
    } else {
      WebsocketAudioOutput jsoutput = (WebsocketAudioOutput) output;
      if (jsoutput.isActive()) {
        jsoutput.stop();
      }

      jsoutput.close();
      this.outputs.remove(output);
    }
  }

  public IOAudioProcess openAudioInput(String name, String label) throws Exception {
    this.checkFormat();
    if (name == null) {
      name = (String) this.getAvailableInputNames().get(0);
      System.out.println(label + " null name specified, using " + name);
    }

    WebsockeAudioInput input = new WebsockeAudioInput(this.format, this.inputForName(name), label,
        name);
    input.open();
    this.inputs.add(input);
    if (this.running) {
      input.start();
    }

    return input;
  }

  public void closeAudioInput(IOAudioProcess input) {
    if (!(input instanceof WebsockeAudioInput)) {
      throw new IllegalArgumentException(input.getName() + " is not a JavaSoundAudioInput");
    } else {
      WebsockeAudioInput jsinput = (WebsockeAudioInput) input;
      if (jsinput.isActive()) {
        jsinput.stop();
      }

      jsinput.close();
      this.inputs.remove(input);
    }
  }
  protected void minimiseInputLatency() {
    Iterator i$ = this.inputs.iterator();

    while (i$.hasNext()) {
      WebsockeAudioInput input = (WebsockeAudioInput) i$.next();
      input.flush();
    }

  }

  protected void controlGained() {
    this.minimiseInputLatency();
  }

  public String getConfigKey() {
    return "javasound";
  }


  protected class WebsockeAudioInput extends WebsockerSoundAudioLine {

    protected WebsocketAudioDevicee.DirectTDL lineIn;
    protected javax.sound.sampled.DataLine.Info infoIn;
    protected MetaInfo metaInfo;
    protected long framesRead = 0L;
    private boolean doFlush = false;

    public WebsockeAudioInput(AudioFormat format, Info info, String label, String location)
        throws LineUnavailableException {
      super(format, info, label);
      this.infoIn = new javax.sound.sampled.DataLine.Info(DirectTDL.class, format);
      if (!mixer.isLineSupported(this.infoIn)) {
        throw new LineUnavailableException(this.mixerInfo + " does not support " + this.infoIn);
      } else {
        this.metaInfo = new MetaInfo(label, location);
      }
    }

    public void open() throws Exception {
      if (this.lineIn == null || !this.lineIn.isOpen()) {
        this.lineIn = (WebsocketAudioDevicee.DirectTDL) mixer.getLine(this.infoIn);
        this.lineIn.open(this.format, WebsocketAudioServer.this.lineBufferBytes);
      }
    }

    public void start() throws Exception {
      this.framesRead = this.lineIn.getLongFramePosition();
      this.lineIn.start();
    }

    public void stop() {
      this.lineIn.stop();
      this.lineIn.flush();
    }

    public void close() {
      if (this.lineIn != null && this.lineIn.isOpen()) {
        this.lineIn.close();
      }

    }

    public void flush() {
      this.doFlush = true;
    }

    public int processAudio(AudioBuffer buffer) {
      buffer.setMetaInfo(this.metaInfo);
    if (!buffer.isRealTime()) {
      return 1;
    } else {
      buffer.setChannelFormat(this.channelFormat);
      int avail = this.lineIn.available();
      if (avail < WebsocketAudioServer.this.sharedByteBuffer.length) {
        buffer.makeSilence();
      } else {
        this.latencyFrames = (int)(this.lineIn.getLongFramePosition() - this.framesRead);
        this.lineIn.read(WebsocketAudioServer.this.sharedByteBuffer, 0, WebsocketAudioServer.this.sharedByteBuffer.length);
        buffer.initFromByteArray(WebsocketAudioServer.this.sharedByteBuffer, 0, WebsocketAudioServer.this.sharedByteBuffer.length, this.format);
        this.framesRead += (long)(WebsocketAudioServer.this.sharedByteBuffer.length / this.format.getFrameSize());
        if (this.doFlush) {
          this.lineIn.flush();
          long fp = this.lineIn.getLongFramePosition();
          this.framesRead = fp;
          this.latencyFrames = 0;
          this.doFlush = false;
        }
      }

//      return 0;
    }
      return 0;
    }

    public boolean isActive() {
      return this.lineIn == null ? false : this.lineIn.isActive();
    }
  }

  protected class WebsocketAudioOutput extends WebsockerSoundAudioLine {

    protected WebsocketAudioDevicee.DirectSDL lineOut;
    protected javax.sound.sampled.DataLine.Info infoOut;
    protected long framesWritten = 0L;

    public WebsocketAudioOutput(AudioFormat format, Info info, String label)
        throws LineUnavailableException {
      super(format, info, label);
      this.infoOut = new javax.sound.sampled.DataLine.Info(WebsocketAudioDevicee.DirectSDL.class, format);

    }

    public void open() throws Exception {
      if (this.lineOut == null || !this.lineOut.isOpen()) {
        this.lineOut = (DirectSDL) mixer.getLine(this.infoOut);
        this.lineOut.open(this.format, WebsocketAudioServer.this.lineBufferBytes);
       }
    }

    public void start() throws Exception {
      this.framesWritten = this.lineOut.getLongFramePosition();
      this.lineOut.start();
    }

    public void stop() {
      this.lineOut.stop();
      this.lineOut.flush();
    }

    public void close() {
      if (this.lineOut != null && this.lineOut.isOpen()) {
        this.lineOut.close();
      }

    }

    public int processAudio(AudioBuffer buffer) {
    if (!buffer.isRealTime()) {
      return 0;
    } else {
      int nbytes = buffer.convertToByteArray(WebsocketAudioServer.this.sharedByteBuffer, 0, this.format);
      if (this.lineOut.available() > WebsocketAudioServer.this.sharedByteBuffer.length) {
        this.lineOut.write(WebsocketAudioServer.this.sharedByteBuffer, 0, nbytes);
        this.framesWritten += (long)(nbytes / this.format.getFrameSize());
      }

      long framePos = this.lineOut.getLongFramePosition();
      this.latencyFrames = (int)(this.framesWritten - framePos);
      if (this.latencyFrames < 0) {
        this.latencyFrames = 0;
      }

      return 0;
    }
    }

    public boolean isActive() {
      return this.lineOut == null ? false : this.lineOut.isActive();
    }
  }

  protected abstract class WebsockerSoundAudioLine implements AudioLine {

    protected AudioFormat format;
    protected Info mixerInfo;
    protected String label;
    protected int latencyFrames = -1;
    protected ChannelFormat channelFormat;

    public WebsockerSoundAudioLine(AudioFormat format, Info info, String label) {
      this.format = format;
      this.mixerInfo = info;
      this.label = label;
      switch (format.getChannels()) {
        case 1:
          this.channelFormat = ChannelFormat.MONO;
          break;
        case 2:
          this.channelFormat = ChannelFormat.STEREO;
      }

    }

    public String getName() {
      return this.label;
    }

    public ChannelFormat getChannelFormat() {
      return this.channelFormat;
    }

    public int getLatencyFrames() {
      return this.latencyFrames;
    }

    public abstract void start() throws Exception;

    public abstract void stop() throws Exception;

    public abstract boolean isActive();
  }
}
