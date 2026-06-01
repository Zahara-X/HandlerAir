package server.handlers;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import server.handlers.entity.ArrayEntityServer;
import server.handlers.entity.PlayerServer;
import server.handlers.writes.WritesServerEntity;

public class NettyHandler extends ChannelInboundHandlerAdapter {
    private final ArrayEntityServer arrayEntityServer = new PlayerServer(100,100,100,100);
    private final WritesServerEntity writesServer = new WritesServerEntity();
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;

        // Флаг, который покажет, прочитали ли мы ВСЁ без остатка
        boolean fullyRead = false;

        try {
            while (buf.isReadable()) {
                if (buf.readableBytes() < 2) {
                    // Остался 1 фрагментарный байт — не трогаем его и НЕ вызываем release!
                    break;
                }
                short key = buf.readShort();
                arrayEntityServer.keyboardKeys(key);
            }

            // Если буфер опустел полностью — значит, фрагментации нет
            if (!buf.isReadable()) {
                fullyRead = true;
            }

            arrayEntityServer.output_keys(ctx);

        } finally {
            // Мы сами освобождаем буфер ТОЛЬКО если он вычитан до нуля!
            // Если там остался кусочек пакета, Netty должен сохранить его до следующего тика.
            if (fullyRead) {
                ReferenceCountUtil.release(msg);
            }
        }
    }
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Игрок подключился!");
        writesServer.spawn_entity(ctx, arrayEntityServer);
    }
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Игрок вышел!");
    }
}