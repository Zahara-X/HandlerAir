package server.handlers.entity;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public class ArrayEntityServer {
   private volatile int pack;
   private volatile int x ,y;
   private volatile int width, height;
   private volatile int speed = 8;
   public ArrayEntityServer(int x, int y, int width, int height) {
       this.x = x;
       this.y = y;
       this.width = width;
       this.height = height;
   }
   public ArrayEntityServer() {}

   public void keyboardKeys(short key) {
       pack = 0x0;
       if((key & (1 << 0)) != 0) {y -= speed; pack = 0x1;}
       if((key & (1 << 2)) != 0) {y += speed; pack = 0x1;}
       if((key & (1 << 3)) != 0) {x -= speed; pack = 0x1;}
       if((key & (1 << 4)) != 0) {x += speed; pack = 0x1;}
   }
   public void output_keys(ChannelHandlerContext ctx) {
       if(pack == 0x1) {
           ByteBuf buf = ctx.alloc().buffer(0xa);
           buf.writeShort(0x3);
           buf.writeInt(y);
           buf.writeInt(x);
           ctx.write(buf);
           ctx.flush();
       }
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