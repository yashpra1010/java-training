package Processes;

import java.io.*;
import java.util.*;

public class FpingTask
{

    public static void main(String[] args) throws IOException
    {
        var HOST_FILE_PATH = "/home/yash/IdeaProjects/FirstProject/src/Processes/hosts.txt";

        HashMap<String, Integer> ipAndPacketCount = new HashMap<>();

        ArrayList<String> hosts;

        Scanner sc = new Scanner(System.in);

        try(FileInputStream fis = new FileInputStream(HOST_FILE_PATH); FileOutputStream fos = new FileOutputStream(HOST_FILE_PATH))
        {

            System.out.print("Enter list of IPs comma-separated: ");

            var userInputHosts = sc.next();

            userInputHosts = userInputHosts.replaceAll("\\p{Blank}", "").replaceAll(",", "\n");

            fos.write(userInputHosts.getBytes());

            byte[] inByte = new byte[fis.available()];

            fis.read(inByte);

            String inStr = new String(inByte);

            hosts = new ArrayList<>(List.of(inStr.split("\n")));

        }

        System.out.print("Enter packet count: ");

        var packetCount = sc.next();

        System.out.print("Enter time (secs) for polling: ");

        var timeForPolling = sc.nextInt();

        ProcessBuilder processBuilder = new ProcessBuilder();

        processBuilder.command("fping", "-c", packetCount, "-f", HOST_FILE_PATH).redirectErrorStream(true);
        while(true)
        {
            Process process = processBuilder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;

            while((line = reader.readLine()) != null)
            {
                System.out.println(line);
                for(String host : hosts)
                {
                    ipAndPacketCount.putIfAbsent(host, 0);

                    if(line.contains(host) && line.contains(" 0% loss"))
                    {
                        var value = ipAndPacketCount.get(host);

                        ipAndPacketCount.put(host, ++value);
                    }

                }
            }
            for(Map.Entry<String, Integer> data : ipAndPacketCount.entrySet())
            {
                if(Integer.parseInt(packetCount) != data.getValue())
                {
                    System.out.println(data.getKey() + " is DOWN");
                }
                else
                    System.out.println(data.getKey() + " is UP");
            }

            System.out.println(ipAndPacketCount);
            System.out.println("-----------------------------------\n");
            sc.close();
            process.destroy();
            try
            {
                Thread.sleep(timeForPolling * 1000);
            } catch(InterruptedException e)
            {
                throw new RuntimeException(e);
            }
            ipAndPacketCount.clear();
        }


    }
}
