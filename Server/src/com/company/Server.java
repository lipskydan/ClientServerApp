package com.company;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private ServerSocket server = null;
    private Socket sock = null;
    private PrintWriter out = null;
    private BufferedReader in = null;

    // запустить сервер
    public void start(int port) throws IOException {
        server = new ServerSocket(port);

        while (true) {

            sock = server.accept();
            System.out.println("[Server::start()] Принимаем соединение от нового клиента");

            // Получаем потоки ввода-вывода
            in = new BufferedReader(new InputStreamReader(sock.getInputStream( )));
            out = new PrintWriter(sock.getOutputStream(), true);

            // Пока соединение активно, обрабатываем запросы
            while (processQuery());
        }
    }

    // обработка запроса
    private boolean processQuery() {
        int result = 0; // Результат операции
        int comp_code = 0; // Код завершения
        try {

            String query = in.readLine();

            if (query==null) {
                return false;
            }
            else  System.out.println("[Server::processQuery()] Получаем запрос от клиента: " + query);

            // Разбиваю строку на поля, разделенные через #
            String[] fields = query.split("#");
            if (fields.length != 3) {
                comp_code = 1; // неверное количество параметров
            } else {
                try {
                    int oper = Integer.valueOf(fields[0]);
                    int v1 = Integer.valueOf(fields[1]);
                    int v2 = Integer.valueOf(fields[2]);
                    if (oper == 0)
                        result = v1+v2; // операция - сложение
                    else if (oper == 1)
                        result = v1-v2; // операция - вычитание
                    else
                        comp_code = 2; // неверный код операции
                } catch (NumberFormatException e) {
                    comp_code = 3; // неверный тип параметров
                }
            }

            String response = comp_code+"#"+result;
            System.out.println("[Server::processQuery()] Формируем ответ: " + response);

            out.println(response);
            System.out.println("[Server::processQuery()] Отправляем клиенту: " + response + "\n\n");
            return true;
        } catch(IOException e) {
            return false;
        }
    }

    // главный метод
    public static void main(String[] args)
    {
        try {
            Server srv = new Server();
            srv.start(12345);
        } catch(IOException e) {
            System.out.println("Возникла ошибка");
        }
    }
}