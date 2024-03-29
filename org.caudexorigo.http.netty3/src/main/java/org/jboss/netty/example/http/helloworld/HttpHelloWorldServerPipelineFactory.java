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

import static org.jboss.netty.channel.Channels.pipeline;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.handler.codec.http.HttpRequestDecoder;
import org.jboss.netty.handler.codec.http.HttpResponseEncoder;

public class HttpHelloWorldServerPipelineFactory implements ChannelPipelineFactory {
  public HttpHelloWorldServerPipelineFactory() {
    super();
  }

  public ChannelPipeline getPipeline() throws Exception {
    // Create a default pipeline implementation.
    ChannelPipeline pipeline = pipeline();

    // Uncomment the following line if you want HTTPS
    // SSLEngine engine =
    // SecureChatSslContextFactory.getServerContext().createSSLEngine();
    // engine.setUseClientMode(false);
    // pipeline.addLast("ssl", new SslHandler(engine));

    pipeline.addLast("decoder", new HttpRequestDecoder());
    // Uncomment the following line if you don't want to handle HttpChunks.
    // pipeline.addLast("aggregator", new HttpChunkAggregator(1048576));
    pipeline.addLast("encoder", new HttpResponseEncoder());
    // Remove the following line if you don't want automatic content compression.
    // pipeline.addLast("deflater", new HttpContentCompressor());
    pipeline.addLast("handler", new HttpHelloWorldServerHandler());
    return pipeline;
  }
}
