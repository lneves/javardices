/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License, version 2.0 (the
 * "License"); you may not use this file except in compliance with the License. You may
 * obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under
 * the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */
package org.jboss.netty.example.http.helloworld;

import org.caudexorigo.Shutdown;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

/**
 * An HTTP server that sends back the content of the received HTTP request in a pretty
 * plaintext form.
 */
public class HttpHelloWorldServer {
  private final int port;

  public HttpHelloWorldServer(int port) {
    this.port = port;
  }

  public void run() {
    // Configure the server.
    ServerBootstrap bootstrap = new ServerBootstrap(new NioServerSocketChannelFactory(Executors
        .newCachedThreadPool(), Executors.newCachedThreadPool()));

    // Enable TCP_NODELAY to handle pipelined requests without latency.
    bootstrap.setOption("child.tcpNoDelay", true);
    bootstrap.setOption("backlog", 1024);

    // Set up the event pipeline factory.
    bootstrap.setPipelineFactory(new HttpHelloWorldServerPipelineFactory());

    // Bind and start to accept incoming connections.
    InetSocketAddress inet = new InetSocketAddress(port);
    bootstrap.bind(inet);
    System.out.printf("Httpd started. Listening on: %s%n", inet.toString());
  }

  public static void main(String[] args) {
    try {
      int port;
      if (args.length > 0) {
        port = Integer.parseInt(args[0]);
      } else {
        port = 8080;
      }
      new HttpHelloWorldServer(port).run();
    } catch (Throwable t) {
      Shutdown.now(t);
    }
  }
}
