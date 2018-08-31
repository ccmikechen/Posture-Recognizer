package mis.kuas.net;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;

/**
 * Created by kuasmis on 16/10/11.
 */
public class ServerUrlManager {

    private static final String URL_FILE_PATH = "/sdcard/stance/uri.cfg";

    private static ServerUrlManager urlManager = null;

    private String serverUrl = null;

    private File file = null;

    private ServerUrlManager() {
        this.file = new File(URL_FILE_PATH);
    }

    public static ServerUrlManager getInstance() {
        if (urlManager == null) {
            urlManager = new ServerUrlManager();
        }
        return urlManager;
    }

    synchronized public String getURL() {
        if (this.serverUrl != null) {
            return this.serverUrl;
        }
        if (!file.exists()) {
            return "";
        }
        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line = bufferedReader.readLine();
            bufferedReader.close();
            fileReader.close();
            this.serverUrl = line;
            return line;
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    synchronized public void setURL(String url) throws IOException {
        if (url.equals("")) {
            return;
        }
        FileWriter writer = new FileWriter(file);
        writer.write(url);
        writer.flush();
        writer.close();

        this.serverUrl = url;
    }

}
