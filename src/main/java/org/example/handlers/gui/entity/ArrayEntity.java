package org.example.handlers.gui.entity;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;

import java.awt.*;

public abstract class ArrayEntity {
      private Channel channel;
      private ByteBuf buf;
      private volatile int x, y;
      private volatile int width, height;

      public ArrayEntity(int x, int y, int width, int height) {
          this.x = x;
          this.y = y;
          this.width = width;
          this.height = height;
      }
      public ArrayEntity() {}

       public void move(int _000p23_) {
          buf = channel.alloc().buffer(0x2);
          buf.writeShort(_000p23_);
          channel.write(buf);
          channel.flush();
    }
    public void synchronized_channel(Channel channel) {
          this.channel = channel;
    }

     public void spawn_player(long packet) {
       long x = packet & 0xFF;
       long y = (packet >> 8) & 0xFF;
       long width = (packet >> 16) & 0xFF;
       long height = (packet >> 24) & 0xFF;
       this.x = (int)x;
       this.y = (int)y;
       this.width = (int)width;
       this.height = (int)height;
     }
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}