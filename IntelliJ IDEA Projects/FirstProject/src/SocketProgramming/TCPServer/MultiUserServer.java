package SocketProgramming.TCPServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class MultiUserServer
{
    public static void main(String[] args)
    {
        try
        {
            ServerSocket serverSocket = new ServerSocket(3000);

            System.out.println("Waiting for clients...");

            boolean stop = false;

            while(!stop)
            {
                Socket socket = serverSocket.accept();

                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

                out.println("Hello Client!");

                BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                String clientInput = input.readLine();

                System.out.println(clientInput);

                if(clientInput.equals("exit"))
                {
                    stop = true;
                }

                input.close();

                out.close();

                socket.close();

            }
            serverSocket.close();

        } catch(IOException e)
        {
            System.out.println(e);
        }
    }
}
