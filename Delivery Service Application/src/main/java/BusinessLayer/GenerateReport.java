package BusinessLayer;

import DataLayer.WriteReport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GenerateReport {
    /**/
    public static List<String> generateReport(HashMap<Order, List<MenuItem>> map, int timeLowerLimit, int timeHigherLimit, int timesOrdered, int nrTimesOrdered, int orderValue, int specificDateDay, int specificDateMonth) {


        List<String> text = new ArrayList<>();
        String s = "The orders performed between " + minutesToTime(timeLowerLimit)  + " and " + minutesToTime(timeHigherLimit) + " are:\n";
        text.add(s);

        map.entrySet().forEach(entry -> {
            int time = timeToMinutes(entry.getKey().getOrderDate());
            if(time >= timeLowerLimit && time <= timeHigherLimit )
                text.add("\t-> " + entry.getKey().getOrderId() + " " + entry.getKey().getClientId() + " " + entry.getKey().getOrderDate().toString() + "\n");
        });
        text.add("\n\nProducts ordered more than " + timesOrdered + " are:\n");
        getProductsOrderedNTimes(map, timesOrdered, text);
        text.add("\n\n" + clientsReport(map, nrTimesOrdered, orderValue) + "\n\n" + getProductsOrderedInSpecificDay(map, specificDateDay, specificDateMonth));
        WriteReport writeReport = new WriteReport();
        writeReport.openFileW();
        writeReport.addRecord(text);
        writeReport.closeFileW();
        return text;
    }

    private static String getProductsOrderedInSpecificDay(HashMap<Order, List<MenuItem>> map, int specificDateDay, int specificDateMonth) {
        String s = "Products ordered in " + specificDateDay + "/" + specificDateMonth +"/2021 are\n";
        List<MenuItem> list = new ArrayList<>();
        map.entrySet().forEach(entry -> {
            if(entry.getKey().getOrderDate().day == specificDateDay && entry.getKey().getOrderDate().month == specificDateMonth)
                list.addAll(entry.getValue());
        });

        int[] nrTimesAppears = new int[list.size()];
        int i = 0; int j = 0, size = list.size();

        for(int k =0; k < size; k++)
            nrTimesAppears[k] = 1;

        while(i < size - 1) {
            j = i + 1;
            while(j < size) {
                if(list.get(i).equals(list.get(j))) {
                    list.remove(j);
                    size--;
                    nrTimesAppears[i]++;
                }
                j++;
            }
            i++;
        }

        int index = 0;
        for(MenuItem item : list) {
            s += "\t-> " + item.getName() + " was ordered " + nrTimesAppears[index] + " times.\n";
            index++;
        }

        return s;
    }

    private static int timeToMinutes(Order.Time time) {
        return time.hour * 60 + time.minute;
    }

    private static String minutesToTime(int minutes) {
        return (minutes / 60) + ":" + (minutes % 60);
    }

    private static void getProductsOrderedNTimes(HashMap<Order, List<MenuItem>> map, int timeOrdered, List<String> result){
        List<String> allItemsEverOrdered = new ArrayList<>();
        map.entrySet().forEach(entry -> {
            entry.getValue().forEach(el ->  allItemsEverOrdered.add(el.getName()) );
        });

        int[] nrTimesAppears = new int[allItemsEverOrdered.size()];
        int i = 0; int j = 0, size = allItemsEverOrdered.size();

        for(int k =0; k < size; k++)
            nrTimesAppears[k] = 1;

        while(i < size - 1) {
            j = i + 1;
            while(j < size) {
                if(allItemsEverOrdered.get(i).equals(allItemsEverOrdered.get(j))) {
                    allItemsEverOrdered.remove(j);
                    size--;
                    nrTimesAppears[i]++;
                }
                j++;
            }
            i++;
        }

        for(int k =0; k < size; k++) {
            if(nrTimesAppears[k] >= timeOrdered)
                result.add("\t -> " + allItemsEverOrdered.get(k) + "\n");
        }

    }

    private static String clientsReport(HashMap<Order, List<MenuItem>> map, int nrTimesOrdered, int orderValue) {
        List<String> result = new ArrayList<>();
        List<String> clientsOfOrders = new ArrayList<>();
        map.entrySet().forEach( entry -> {
            clientsOfOrders.add(entry.getKey().getClientId());
        });

        int[] nrTimesAppears = new int[clientsOfOrders.size()];
        int i = 0; int j = 0, size = clientsOfOrders.size();

        for(int k =0; k < size; k++)
            nrTimesAppears[k] = 1;

        while(i < size - 1) {
            j = i + 1;
            while(j < size) {
                if(clientsOfOrders.get(i).equals(clientsOfOrders.get(j))) {
                    clientsOfOrders.remove(j);
                    size--;
                    nrTimesAppears[i]++;
                }
                j++;
            }
            i++;
        }

        map.entrySet().stream().
                filter(entry -> (entry.getKey().getPrice() >= orderValue && nrOfTimesAClientAppears(nrTimesAppears, clientsOfOrders, entry.getKey().getClientId()) >= nrTimesOrdered)).
                forEach( orderListEntry -> {result.add(orderListEntry.getKey().getClientId() + " " + orderListEntry.getKey().getPrice());});
        String s = "Clients who ordered more than " + nrTimesOrdered + ", with orders greater than " + orderValue + " are:\n";
        for(String client : result)
            s += "\t -> " + client + "\n";
        return s;
    }

    private static int nrOfTimesAClientAppears(int[] nrTimesAppears, List<String> clientNames, String searchedClient) {
        int index = 0;
        for(String client : clientNames) {
            if(client.equals(searchedClient))
                return nrTimesAppears[index];
            index++;
        }
        return -1;
    }
}
