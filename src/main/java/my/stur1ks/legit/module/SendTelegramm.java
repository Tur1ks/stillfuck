package my.stur1ks.legit.module;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class SendTelegramm {
    private static final String BOT_TOKEN = "Хуй те а не токен";
    private static final String CHAT_ID = "получать тут - @getmyid_bot";

    public static void sendMessage(String message) {
        try {
            String urlString = String.format("https://api.telegram.org/bot%s/sendMessage?chat_id=%s&text=%s&parse_mode=HTML",
                    BOT_TOKEN, CHAT_ID, URLEncoder.encode(message, "UTF-8"));

            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.getInputStream().close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendFile(File file, String caption) {
        try {
            String boundary = "---" + System.currentTimeMillis();
            URL url = new URL("https://api.telegram.org/bot" + BOT_TOKEN + "/sendDocument");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

            try (OutputStream os = conn.getOutputStream();
                 PrintWriter writer = new PrintWriter(new OutputStreamWriter(os, StandardCharsets.UTF_8))) {

                writer.append("--").append(boundary).append("\r\n");
                writer.append("Content-Disposition: form-data; name=\"chat_id\"\r\n\r\n");
                writer.append(CHAT_ID).append("\r\n");

                writer.append("--").append(boundary).append("\r\n");
                writer.append("Content-Disposition: form-data; name=\"document\"; filename=\"").append(caption).append("\"\r\n");
                writer.append("Content-Type: application/octet-stream\r\n\r\n");
                writer.flush();

                FileInputStream fis = new FileInputStream(file);
                byte[] buffer = new byte[8192];
                int count;
                while ((count = fis.read(buffer)) != -1) {
                    os.write(buffer, 0, count);
                }
                os.flush();
                fis.close();

                writer.append("\r\n--").append(boundary).append("--\r\n");
                writer.flush();
            }

            conn.getInputStream().close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
