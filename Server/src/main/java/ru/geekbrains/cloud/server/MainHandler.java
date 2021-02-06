package ru.geekbrains.cloud.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import ru.geekbrains.cloud.common.*;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class MainHandler extends ChannelInboundHandlerAdapter {

    private SqlAuthManager sqlAuthManager;
    private List<String> users;
    private boolean contains;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        sqlAuthManager = new SqlAuthManager();
        try {
            System.out.println(msg.toString());
            if (msg instanceof FileRequest) {
                FileRequest fr = (FileRequest) msg;
                if (Files.exists(Paths.get("ServerStorage/" + fr.getFilename()))) {
                    FileMessage fm = new FileMessage(Paths.get("ServerStorage/" + fr.getFilename()));
                    ctx.writeAndFlush(fm);
                }
            }
            if (msg instanceof FileSend) {
                FileSend fs = (FileSend) msg;
                Files.write(Paths.get("serverStorage/" + fs.getName()), fs.getData(), StandardOpenOption.CREATE);
            }
            if (msg instanceof GetServerListFiles){
                GetServerListFiles clientsFiles = (GetServerListFiles) msg;
                Files.list(Paths.get("serverStorage/")).map(path -> path.getFileName().toString()).forEach(o -> clientsFiles.setList(o));
                ctx.writeAndFlush(clientsFiles);
            }
            if (msg instanceof ServerDeleteFile){
                ServerDeleteFile sd = (ServerDeleteFile) msg;
                Files.delete(Paths.get("ServerStorage/" + sd.getFilename()));
            }

            if (msg instanceof TryToAuth){
                String username = "";
                TryToAuth sd = (TryToAuth) msg;
                username = sqlAuthManager.getNickNameByLoginAndPassword(sd.getLogin(), sd.getPass());
                ctx.writeAndFlush(username);
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