package ru.geekbrains.cloud.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import ru.geekbrains.cloud.common.*;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;

public class MainHandler extends ChannelInboundHandlerAdapter {

    private SqlAuthManager sqlAuthManager;
    private static HashMap<String, Integer> map = new HashMap<>();

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
                System.out.println(2);
                GetServerListFiles clientsFiles = (GetServerListFiles) msg;
                System.out.println(3);
                Files.list(Paths.get("serverStorage/")).map(path -> path.getFileName().toString()).forEach(o -> clientsFiles.setList(o));
                System.out.println(4);
                ctx.writeAndFlush(clientsFiles);
                System.out.println(5);
            }
            if (msg instanceof ServerDeleteFile){
                ServerDeleteFile sd = (ServerDeleteFile) msg;
                Files.delete(Paths.get("ServerStorage/" + sd.getFilename()));
            }

            if (msg instanceof TryToAuth){
                String username = "";
                TryToAuth sd = (TryToAuth) msg;
                username = sqlAuthManager.getNickNameByLoginAndPassword(sd.getLogin(), sd.getPass());
                if (!map.containsKey(username)){
                    map.put(username, 1);
                }else if (map.containsKey(username)){
                    map.put(username, map.get(username) + 1);
                }

                for (Map.Entry<String, Integer> map : map.entrySet()){
                    if (map.getKey().equals(username) && map.getValue() > 1){
                        username = "";
                        ctx.writeAndFlush(username);
                    }else if (map.getKey().equals(username) && map.getValue() == 1){
                        ctx.writeAndFlush(username);;
                    }
                }

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