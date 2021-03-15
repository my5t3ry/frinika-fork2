package com.frinika.project.moc;

import com.frinika.project.FrinikaAudioServer;
import com.frinika.toot.PriorityAudioServer;
import java.util.Properties;
import uk.org.toot.audio.server.AudioServerConfiguration;
import uk.org.toot.audio.server.ExtendedAudioServer;

public class MocConfiuration  extends AudioServerConfiguration {
    private static final String USER_BUFFER = ".buffer.milliseconds";
    private static final String LATENCY = ".latency.milliseconds";
    private static final String PRIORITY = ".priority";

  public void setServer(FrinikaAudioServer server) {
    this.server = server;
  }

  private FrinikaAudioServer server;
    private boolean hasPriority;

  public MocConfiuration() {
  }

    public Properties getProperties() {
      Properties p = new Properties();
//      String k = this.server.getConfigKey();
//      p.setProperty(k + ".buffer.milliseconds", String.valueOf(this.server.getBufferMilliseconds()));
//      p.setProperty(k + ".latency.milliseconds", String.valueOf(this.server.getLatencyMilliseconds()));
//      if (this.hasPriority) {
//        PriorityAudioServer pas = (PriorityAudioServer)this.server;
//        p.setProperty(k + ".priority", String.valueOf(pas.getPriority()));
//      }

      return p;
    }

    public void applyProperties(Properties p) {
      if (p == null) {
        System.err.println("null properties passed to ExtendedAudioServerConfiguration.applyProperties()");
      } else {
//        String k = this.server.getConfigKey();
//        String value = p.getProperty(k + ".buffer.milliseconds");
//        if (value != null) {
//          this.server.setBufferMilliseconds(Float.valueOf(value));
//        }
//
//        value = p.getProperty(k + ".latency.milliseconds");
//        if (value == null) {
//          value = p.getProperty(k + "_outputBuffer");
//        }
//
//        if (value != null) {
//          this.server.setLatencyMilliseconds(Float.valueOf(value));
//        }
//
//        if (this.hasPriority) {
//          value = p.getProperty(k + ".priority");
//          if (value == null) {
//            value = p.getProperty(k + "_priority");
//          }

//          if (value != null) {
//            PriorityAudioServer pas = (PriorityAudioServer)this.server;
//            pas.requestPriority(Integer.valueOf(value));
//          }
        }

      }



}
