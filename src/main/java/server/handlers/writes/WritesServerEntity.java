package server.handlers.writes;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import server.handlers.entity.ArrayEntityServer;

public class WritesServerEntity {

    public void spawn_entity(ChannelHandlerContext ctx, ArrayEntityServer entity) {
        ByteBuf buf = ctx.alloc().buffer(0x4);
        buf.writeShort(0x1);
        long packet = ((long) (entity.getX() & 0xFF)) |
                ((long) (entity.getY() & 0xFF) << 8) |
                ((long) (entity.getWidth() & 0xFF) << 16) |
                ((long) (entity.getHeight() & 0xFF) << 24);
        buf.writeLong(packet);
        ctx.write(buf);
        ctx.flush();
    }
}