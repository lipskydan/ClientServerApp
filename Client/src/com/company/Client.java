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

    public void sendQueryAndGetResponse( String query) throws IOException {
        out.println(query);
        System.out.println("[Client::sendQueryAndGetResponse()] Отправляем запрос: " + query);

        String[] fields = in.readLine().split("#");
        System.out.print("[Client::sendQueryAndGetResponse()] Получаем ответ:\n");

        try {
            // Код завершения
            int comp_code = Integer.valueOf(fields[0]);

            // Результат операции
            String result = " ";
           for (int i = 1; i < fields.length; i++){
               result += fields[i] + "\n";
           }

            if (comp_code==0){
                System.out.println(result + "\n\n");
            } else throw new IOException("Error while processing query");
        } catch(NumberFormatException e) {
            throw new IOException("Invalid response from server");
        }
    }
    public void disconnect() throws IOException {
        sock.close();
        System.out.println("[Client::disconnect()]");
    }

////////////////////////////////////////////////////////////////////

    public void addCountry(int idCountry, String name) throws IOException {
        String query = 1+"#"+idCountry+"#"+name;
        System.out.println("[Client::addCountry()] Формируем запрос: " + query);

        sendQueryAndGetResponse(query);
    }

    public void deleteCountry(int idCountry) throws IOException {
        String query = 2+"#"+idCountry;
        System.out.println("[Client::deleteCountry()] Формируем запрос: " + query);
        sendQueryAndGetResponse(query);
    }

    public void countCitiesInCountry(int idCountry) throws IOException {
        String query = 6+"#"+idCountry;
        System.out.println("[Client::countCitiesInCountry()] Формируем запрос: " + query);
        sendQueryAndGetResponse(query);
    }

    public void showCountries() throws IOException {
        String query = 9+"#";
        System.out.println("[Client::showCountries()] Формируем запрос: " + query);
        sendQueryAndGetResponse(query);
    }

////////////////////////////////////////////////////////////////////

    public void addCity(int idCity, int idCountry, String nameCity, int count, int isCapital) throws IOException {
        String query = 3+"#"+idCity+"#"+idCountry+"#"+nameCity+"#"+count+"#"+isCapital;
        System.out.println("[Client::addCity()] Формируем запрос: " + query);
        sendQueryAndGetResponse(query);
    }

    public void deleteCity(int idCity) throws IOException {
        String query = 4+"#"+idCity;
        System.out.println("[Client::deleteCity()] Формируем запрос: " + query);
        sendQueryAndGetResponse(query);
    }

    public void editingCity(int idCity, int newIdCountry, String newNameCity, int newCount, int newIsCapital) throws IOException {
        String query = 5+"#"+idCity+"#"+newIdCountry+"#"+newNameCity+"#"+newCount+"#"+newIsCapital;
        System.out.println("[Client::editingCity()] Формируем запрос: " + query);
        sendQueryAndGetResponse(query);
    }

    public void showFullListCities() throws IOException {
        String query = 7+"#";
        System.out.println("[Client::showFullListCities()] Формируем запрос: " + query);
        sendQueryAndGetResponse(query);
    }

    public void showListCities(int idCountry) throws IOException {
        String query = 8+"#"+idCountry;
        System.out.println("[Client::showListCities()] Формируем запрос: " + query);
        sendQueryAndGetResponse(query);
    }

////////////////////////////////////////////////////////////////////


    public static void main(String[] args) {
        try
        {
            Client client = new Client("localhost",12345);

            System.out.println("-------------------------------------------");

            client.showCountries();
            client.showListCities(1);
            client.showListCities(2);
            client.showListCities(3);

            System.out.println("-------------------------------------------");

            client.addCountry(1, "Russia");
            client.addCountry(2, "USA");
            client.addCountry(3,"Canada");

            client.showCountries();

            System.out.println("-------------------------------------------");

            client.addCity(1,1,"MOSCOW",11612943,1);
            client.addCity(2,1,"SOCHI",343334,0);
            client.addCity(3,2,"NEW YORK",8363710,0);
            client.addCity(4,2,"LOS ANGELES",56451241,0);
            client.addCity(5,3,"TORONTO",94234710,1);

            client.showListCities(1);
            client.showListCities(2);
            client.showListCities(3);

            System.out.println("-------------------------------------------");

            client.countCitiesInCountry(1);
            client.countCitiesInCountry(2);
            client.countCitiesInCountry(3);

            System.out.println("-------------------------------------------");

            client.deleteCity(1);
            client.deleteCity(2);

            client.deleteCountry(1);

            System.out.println("-------------------------------------------");

            client.editingCity(3,2,"NEW YORK",8363710, 1);

            System.out.println("-------------------------------------------");

            client.showFullListCities();

            System.out.println("-------------------------------------------");

            client.deleteCity(3);
            client.deleteCity(4);
            client.deleteCity(5);

            client.deleteCountry(2);
            client.deleteCountry(3);

            System.out.println("-------------------------------------------");

            client.showCountries();
            client.showFullListCities();

            System.out.println("-------------------------------------------");

            client.disconnect();
        }catch(IOException e)
        {
            System.out.println("Возникла ошибка");
            e.printStackTrace();
        }
    }
}