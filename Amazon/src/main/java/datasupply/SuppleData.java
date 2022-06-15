package datasupply;

import datasource.ConnectToSqlDB;

import java.util.ArrayList;
import java.util.List;

public class SuppleData {

    public static List<String> getTestData(){
        List<String> listOfItems = new ArrayList<String>();
        listOfItems.add("Honey");
        listOfItems.add("Toothpaste");
        listOfItems.add("Java Book");
        listOfItems.add("Laptop");
        listOfItems.add("iPhone");
        listOfItems.add("Salmon");

        return listOfItems;
    }
    ConnectToSqlDB connectToSqlDB = new ConnectToSqlDB();
    public List<String> getItemListFromDB() throws Exception {
        List<String> list = connectToSqlDB.readDataBase("ItemList","items");

        return list;
    }
}