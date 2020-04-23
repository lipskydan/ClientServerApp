package com.company;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerWithBD {

    private ServerSocket server = null;
    private Socket sock = null;
    private PrintWriter out = null;
    private BufferedReader in = null;
    private Map map = null;

    ServerWithBD(){
        map = new Map("MAP","localhost",3306,"root", "Steps.123");
    }

    public void start(int port) throws IOException {
        server = new ServerSocket(port);

        while (true) {
            sock = server.accept();
            System.out.println("[Server::start()] Принимаем соединение от нового клиента");

            in = new BufferedReader(new InputStreamReader(sock.getInputStream( )));
            out = new PrintWriter(sock.getOutputStream(), true);

            while (processQuery());
        }
    }

    private boolean processQuery() {
        int comp_code = 0;
        try {

            String query = in.readLine();

            if (query==null) {
                return false;
            }
            else  System.out.println("[Server::processQuery()] Получаем запрос от клиента: " + query);

            String[] fields = query.split("#");

            int oper = Integer.valueOf(fields[0]);
            int idCountry = 0;
            int idCity = 0;
            String nameCountry = "";
            String nameCity = "";
            int count = 0;
            int isCapital = 0;
            String response = "";

            switch (oper){
                case 1:
                    idCountry = Integer.valueOf(fields[1]);
                    nameCountry = fields[2];
                    System.out.println("[Server::processQuery()] Client want to add country (ID: " + idCountry + " " + "Name: " + nameCountry + ")");
                    map.addCountry(idCountry,nameCountry);
                    response = comp_code+"#"+"Country was added (ID: " + idCountry + " " + "Name: " + nameCountry + ")";
                    System.out.println("[Server::processQuery()] Формируем ответ");

                    sendResponse(response);
                    break;

                case 2:
                    idCountry = Integer.valueOf(fields[1]);
                    System.out.println("[Server::processQuery()] Client want to delete country (ID: " + idCountry + ")");
                    map.deleteCountry(idCountry);
                    response = comp_code+"#"+"Country was deleted (ID: " + idCountry + ")";
                    System.out.println("[Server::processQuery()] Формируем ответ");

                    sendResponse(response);
                    break;

                case 3:
                    idCity = Integer.valueOf(fields[1]);
                    idCountry = Integer.valueOf(fields[2]);
                    nameCity = fields[3];
                    count = Integer.valueOf(fields[4]);
                    isCapital = Integer.valueOf(fields[5]);

                    System.out.println("[Server::processQuery()] Client want to add city (idCity: " + idCity +
                            " idCountry: " + idCountry + " " + "name: " + nameCity + " " + "count: " + count +
                            " " + "isCapital: " + isCapital + ")");

                    map.addCity(idCity,idCountry,nameCity,count,isCapital);

                    response = comp_code+"#"+"City was added (idCity: " + idCity + " idCountry: " + idCountry + " " + "name: "
                            + nameCity + " " + "count: " + count + " " + "isCapital: " + isCapital + ")";

                   sendResponse(response);
                    break;

                case 4:
                    idCity = Integer.valueOf(fields[1]);
                    System.out.println("[Server::processQuery()] Client want to delete city (ID: " + idCity + ")");

                    map.deleteCity(idCity);

                    response = comp_code+"#"+"City was deleted (ID: " + idCity + ")";
                    System.out.println("[Server::processQuery()] Формируем ответ");

                    sendResponse(response);

                    break;

                case 5:
                    idCity = Integer.valueOf(fields[1]);
                    idCountry = Integer.valueOf(fields[2]);
                    nameCity = fields[3];
                    count = Integer.valueOf(fields[4]);
                    isCapital = Integer.valueOf(fields[5]);
                    System.out.println("[Server::processQuery()] Client want to edit city (ID: " + idCity + ")");

                    map.changeCityInfo(idCity,idCountry,nameCity,count,isCapital);

                    response = comp_code+"#"+"City was edited (idCity: " + idCity + " idCountry: " + idCountry + " " + "name: "
                            + nameCity + " " + "count: " + count + " " + "isCapital: " + isCapital + ")";
                    System.out.println("[Server::processQuery()] Формируем ответ");

                    sendResponse(response);
                    break;

                case 6:
                    idCountry = Integer.valueOf(fields[1]);
                    System.out.println("[Server::processQuery()] Client want to know quantity of cities in country (ID: " + idCountry + ")");

                    response = comp_code+"#"+"Quantity of cities in country (ID: " + idCountry + ") is " + map.countCities(idCountry);
                    System.out.println("[Server::processQuery()] Формируем ответ");

                    sendResponse(response);
                    break;

                case 7:
                    System.out.println("[Server::processQuery()] Client want to see full list of cities");


                    response = comp_code+"#"+map.showAllCities();
                    System.out.println("[Server::processQuery()] Формируем ответ");

                    sendResponse(response);
                    break;

                case 8:
                    idCountry = Integer.valueOf(fields[1]);
                    System.out.println("[Server::processQuery()] Client want to see list of cities for country (ID: " + idCountry + ")");

                    response = comp_code+"#"+map.showCities(idCountry);
                    System.out.println("[Server::processQuery()] Формируем ответ");

                    sendResponse(response);
                    break;

                case 9:
                    System.out.println("[Server::processQuery()] Client want to see list of countries");

                    response = comp_code+"#"+map.showCountries();
                    System.out.println("[Server::processQuery()] Формируем ответ");

                    sendResponse(response);
                    break;

                default:
                    System.out.println("[Server::processQuery()] Unknown oper");
                    break;
            }

            return true;
        } catch(IOException e) {
            return false;
        }
    }

    void sendResponse(String response){
        out.println(response);
        System.out.println("[Server::sendResponse()] Отправляем клиенту" + "\n\n");
    }

    public static void main(String[] args)
    {
        try {
            ServerWithBD srv = new ServerWithBD();
            srv.start(12345);
        } catch(IOException e) {
            System.out.println("Возникла ошибка");
        }
    }

}
