package wstest.srv.netty;

import com.lexicalscope.jewel.cli.CliFactory;

import org.caudexorigo.http.netty4.NettyHttpServer;
import org.caudexorigo.http.netty4.NettyHttpServerCliArgs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.util.internal.logging.Slf4JLoggerFactory;

public class Main {
  private static Logger log = LoggerFactory.getLogger(Main.class);

  public static void main(String[] args) throws Exception {
    log.info("Starting WSTest Http daemon");

    try {
      // Verify if the Aalto parser is in the classpath
      Class.forName("com.fasterxml.aalto.stax.InputFactoryImpl").newInstance();
      Class.forName("com.fasterxml.aalto.stax.OutputFactoryImpl").newInstance();
      Class.forName("com.fasterxml.aalto.stax.EventFactoryImpl").newInstance();

      // If we made it here without errors set Aalto as our StaX parser
      System.setProperty("javax.xml.stream.XMLInputFactory",
          "com.fasterxml.aalto.stax.InputFactoryImpl");
      System.setProperty("javax.xml.stream.XMLOutputFactory",
          "com.fasterxml.aalto.stax.OutputFactoryImpl");
      System.setProperty("javax.xml.stream.XMLEventFactory",
          "com.fasterxml.aalto.stax.EventFactoryImpl");
    } catch (Throwable t) {
      log.warn("Aalto was not found in the classpath, will fallback to use the native parser");
    }

    final NettyHttpServerCliArgs cargs = CliFactory.parseArguments(NettyHttpServerCliArgs.class,
        args);

    InternalLoggerFactory.setDefaultFactory(Slf4JLoggerFactory.getDefaultFactory());
    NettyHttpServer server = new NettyHttpServer();
    server.setPort(cargs.getPort());
    server.setHost(cargs.getHost());
    server.setRouter(new NettyWSRouter());
    server.start();
  }
}
