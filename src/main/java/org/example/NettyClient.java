package org.example;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.example.handlers.NettyHandler;
import org.example.handlers.gui.entity.ArrayEntity;
import org.example.handlers.gui.entity.Player;

public class NettyClient {
    private final ArrayEntity arrayEntity;
    public NettyClient(String local, int port, ArrayEntity arrayEntity) {
        this.arrayEntity = arrayEntity;
        NioEventLoopGroup group = new NioEventLoopGroup(1);
        try {
            Bootstrap boot = new Bootstrap();
            boot.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_RCVBUF, 32 * 1024)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new NettyHandler(arrayEntity));
                        }
                    });
            ChannelFuture future = boot.connect(local, port).sync();
            arrayEntity.synchronized_channel(future.channel());
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }
}