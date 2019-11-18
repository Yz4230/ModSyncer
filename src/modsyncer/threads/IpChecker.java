package modsyncer.threads;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IpChecker extends Thread {
    public static String globalIpAddr;
    @Override
    public void run() {
        try {
            URL url = new URL("http://checkip.dyndns.org:8245/");
            InputStreamReader streamReader = new InputStreamReader(url.openStream());
            BufferedReader bufferedReader = new BufferedReader(streamReader);
            String line = bufferedReader.readLine();
            Pattern pattern =  Pattern.compile("((?:\\d{1,3}\\.?){4})");
            Matcher matcher = pattern.matcher(line);
            if (matcher.find()) {
                globalIpAddr = matcher.group();
            }
        } catch (IOException e) {
            globalIpAddr = "";
        }
    }
}
