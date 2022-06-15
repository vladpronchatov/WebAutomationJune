package datasupply;

import base.CommonAPI;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import reporting.TestLogger;

import java.util.List;

public class SearchItems extends CommonAPI {

    public void itemsSearch() throws Exception {
        TestLogger.log(getClass().getSimpleName() + ": " + convertToString(new Object(){}.getClass().getEnclosingMethod().getName()));
        SuppleData suppleData = new SuppleData();
        List<String> items = suppleData.getItemListFromDB();
        for(String item:items) {
            typeOnWebElement("twotabsearchtextbox",item);
            Thread.sleep(500);
            clearInputBox("twotabsearchtextbox");
        }
    }
}