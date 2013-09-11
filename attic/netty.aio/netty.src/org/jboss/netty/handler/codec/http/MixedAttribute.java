/*
 * Copyright 2009 Red Hat, Inc.
 *
 * Red Hat licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package org.jboss.netty.handler.codec.http;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import org.jboss.netty.buffer.ChannelBuffer;

/**
 * Mixed implementation using both in Memory and in File with a limit of size
 * @author <a href="http://www.jboss.org/netty/">The Netty Project</a>
 * @author Andy Taylor (andy.taylor@jboss.org)
 * @author <a href="http://gleamynode.net/">Trustin Lee</a>
 * @author <a href="http://openr66.free.fr/">Frederic Bregier</a>
 *
 */
public class MixedAttribute implements Attribute {
    private Attribute attribute = null;

    private long limitSize = 0;

    public MixedAttribute(String name,
            long limitSize) throws NullPointerException,
            IllegalArgumentException {
        this.limitSize = limitSize;
        attribute = new MemoryAttribute(name);
    }

    public MixedAttribute(String name, String value,
            long limitSize) throws NullPointerException,
            IllegalArgumentException {
        this.limitSize = limitSize;
        if (value.length() > this.limitSize) {
            try {
                attribute = new DiskAttribute(name, value);
            } catch (IOException e) {
                // revert to Memory mode
                try {
                    attribute = new MemoryAttribute(name, value);
                } catch (IOException e1) {
                    throw new IllegalArgumentException(e);
                }
            }
        } else {
            try {
                attribute = new MemoryAttribute(name, value);
            } catch (IOException e) {
                throw new IllegalArgumentException(e);
            }
        }
    }

    @Override
    public void addContent(ChannelBuffer buffer, boolean last)
            throws IOException {
        if (attribute instanceof MemoryAttribute) {
            if (attribute.length() + buffer.readableBytes() > limitSize) {
                DiskAttribute diskAttribute = new DiskAttribute(attribute
                        .getName());
                if (((MemoryAttribute) attribute).getChannelBuffer() != null) {
                    diskAttribute.addContent(((MemoryAttribute) attribute)
                        .getChannelBuffer(), last);
                }
                attribute = diskAttribute;
            }
        }
        attribute.addContent(buffer, last);
    }

    @Override
    public void delete() {
        attribute.delete();
    }

    @Override
    public byte[] get() throws IOException {
        return attribute.get();
    }

    @Override
    public ChannelBuffer getChannelBuffer() throws IOException {
        return attribute.getChannelBuffer();
    }

    @Override
    public Charset getCharset() {
        return attribute.getCharset();
    }

    @Override
    public String getString() throws IOException {
        return attribute.getString();
    }

    @Override
    public String getString(Charset encoding) throws IOException {
        return attribute.getString(encoding);
    }

    @Override
    public boolean isCompleted() {
        return attribute.isCompleted();
    }

    @Override
    public boolean isInMemory() {
        return attribute.isInMemory();
    }

    @Override
    public long length() {
        return attribute.length();
    }

    @Override
    public boolean renameTo(File dest) throws IOException {
        return attribute.renameTo(dest);
    }

    @Override
    public void setCharset(Charset charset) {
        attribute.setCharset(charset);
    }

    @Override
    public void setContent(ChannelBuffer buffer) throws IOException {
        if (buffer.readableBytes() > limitSize) {
            if (attribute instanceof MemoryAttribute) {
                // change to Disk
                DiskAttribute diskAttribute = new DiskAttribute(attribute
                        .getName());
                attribute = diskAttribute;
            }
        }
        attribute.setContent(buffer);
    }

    @Override
    public void setContent(File file) throws IOException {
        if (file.length() > limitSize) {
            if (attribute instanceof MemoryAttribute) {
                // change to Disk
                DiskAttribute diskAttribute = new DiskAttribute(attribute
                        .getName());
                attribute = diskAttribute;
            }
        }
        attribute.setContent(file);
    }

    @Override
    public void setContent(InputStream inputStream) throws IOException {
        if (attribute instanceof MemoryAttribute) {
            // change to Disk even if we don't know the size
            DiskAttribute diskAttribute = new DiskAttribute(attribute
                    .getName());
            attribute = diskAttribute;
        }
        attribute.setContent(inputStream);
    }

    @Override
    public HttpDataType getHttpDataType() {
        return attribute.getHttpDataType();
    }

    @Override
    public String getName() {
        return attribute.getName();
    }

    @Override
    public int compareTo(InterfaceHttpData o) {
        return attribute.compareTo(o);
    }

    @Override
    public String toString() {
        return "Mixed: " + attribute.toString();
    }

    @Override
    public String getValue() throws IOException {
        return attribute.getValue();
    }

    @Override
    public void setValue(String value) throws IOException {
        attribute.setValue(value);
    }

    @Override
    public ChannelBuffer getChunk(int length) throws IOException {
        return attribute.getChunk(length);
    }

    @Override
    public File getFile() throws IOException {
        return attribute.getFile();
    }

}
