package org.example.handlers;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import org.example.handlers.gui.entity.ArrayEntity;

public class NettyHandler extends ChannelInboundHandlerAdapter {
    private final ArrayEntity entity;
    public NettyHandler(ArrayEntity entity) {
        this.entity = entity;
    }
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        try {
            ByteBuf buf = (ByteBuf) msg;

            while (buf.isReadable()) {
                // Нам нужно прочитать МИНИМУМ кодек (2 байта), чтобы понять, что делать дальше
                if (buf.readableBytes() < 2) {
                    break;
                }

                // Маркируем индекс на случай, если целого пакета внутри кодека не хватит
                buf.markReaderIndex();
                short code = buf.readShort();

                // --- ВАРИАНТ 1: ПАКЕТ СПАВНА ---
                if (code == 0x1) {
                    if (buf.readableBytes() < 8) { // Нам нужно еще 8 байт для long packet
                        buf.resetReaderIndex(); // Байт не хватает — откатываемся и ждем сеть
                        break;
                    }
                    long packet = buf.readLong();
                    entity.spawn_player(packet);
                }

                // --- ВАРИАНТ 2: ПАКЕТ ДВИЖЕНИЯ ---
                else if (code == 0x3) {
                    if (buf.readableBytes() < 8) { // Нам нужно еще 8 байт (4 для X + 4 для Y)
                        buf.resetReaderIndex(); // Откатываемся
                        break;
                    }
                    // ВНИМАНИЕ: Сначала X, потом Y (как на сервере!)
                    int y = buf.readInt();
                    int x = buf.readInt();
                    entity.setY(y);
                    entity.setX(x);
                }

                // --- ВАРИАНТ 3: МУСОР В СЕТИ ---
                else {
                    // Если прилетел неизвестный кодек, отматываем назад и пропускаем 1 байт
                    buf.resetReaderIndex();
                    buf.readByte();
                }
            }

        } finally {
            // Netty сам очистит память через пулы сокетов, когда буфер опустеет.
            // Если вы хотите контролировать вручную — вызывайте release только если буфер ПУСТ.
            ByteBuf buf = (ByteBuf) msg;
            if (!buf.isReadable()) {
                ReferenceCountUtil.release(msg);
            }
        }

    }
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println("Сервер активен!");
    }
}