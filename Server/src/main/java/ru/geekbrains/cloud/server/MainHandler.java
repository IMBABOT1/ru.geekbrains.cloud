package ru.geekbrains.cloud.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import ru.geekbrains.cloud.common.FileMessage;
import ru.geekbrains.cloud.common.FileRequest;
import ru.geekbrains.cloud.common.FileSend;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class MainHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            if (msg instanceof FileRequest) {
                FileRequest fr = (FileRequest) msg;
                if (Files.exists(Paths.get("ServerStorage/" + fr.getFilename()))) {
                    FileMessage fm = new FileMessage(Paths.get("ServerStorage/" + fr.getFilename()));
                    ctx.writeAndFlush(fm);
                }
            }
            if (msg instanceof FileSend) {
                System.out.println(2);
                FileSend fs = (FileSend) msg;
                System.out.println(3);
                Files.write(Paths.get("serverStorage/" + fs.getName()), fs.getData(), StandardOpenOption.CREATE);
                System.out.println(4);
            }
        }finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
