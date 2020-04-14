package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


public class Client {
    private Socket sock = null;
    private PrintWriter out = null;
    private BufferedReader in = null;

    public Client(String ip, int port) throws IOException {

        sock = new Socket(ip,port);
        System.out.println("[Client::Client()] Устанавливаем соединение");

        in = new BufferedReader(new InputStreamReader(sock.getInputStream( )));
        out = new PrintWriter(sock.getOutputStream(), true);
    }

    private int sendQuery(int operation, int value1, int value2) throws IOException {

        String query = operation+"#"+value1+"#"+value2;
        System.out.println("[Client::sendQuery()] Формируем запрос: " + query);

        out.println(query);
        System.out.println("[Client::sendQuery()] Отправвляем запрос: " + query);

        String[] fields = in.readLine().split("#");
        System.out.print("[Client::sendQuery()] Получаем ответ: ");

        if (fields.length!=2) throw new IOException("Invalid response from server");

        try {

            // Код завершения
            int comp_code = Integer.valueOf(fields[0]);

            // Результат операции
            int result = Integer.valueOf(fields[1]);
            if (comp_code==0){
                System.out.println(result + "\n\n");
                return result;
            }
            else
                throw new IOException("Error while processing query");
        } catch(NumberFormatException e) {
            throw new IOException("Invalid response from server");
        }
    }

    // посчитать сумму чисел
    public int sum(int value1, int value2) throws IOException {
        return sendQuery(0, value1, value2);
    }

    // посчитать разность чисел
    public int sub(int value1, int value2) throws IOException {
        return sendQuery(1, value1, value2);
    }

    // отсоединиться
    public void disconnect() throws IOException {
        sock.close();
    }

    // главный метод
    public static void main(String[] args) {
        try
        {
            Client client = new Client("localhost",12345);
            int a = client.sum(15,20);
            int b = client.sub(30,38);
            int c = client.sum(10,20);
//            System.out.println(a);
//            System.out.println(b);
//            System.out.println(c);
            client.disconnect();
        }catch(IOException e)
        {
            System.out.println("Возникла ошибка");
            e.printStackTrace();
        }
    }
}