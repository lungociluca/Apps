package DataLayer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class LogIn implements Serializable {

    public class Data implements Serializable {
        public String name, password;

        public Data(String name, String password) {
            this.name = name;
            this.password = password;
        }
    }

    private List<Data> users = new ArrayList<>();
    private List<Data> employees = new ArrayList<>();
    private Data administrator = new Data("Lungoci Luca", "Password");


    public String addUser(String name, String password) {
        Data toAdd = new Data(name, password);
        for(Data user : users)
            if(user.name.equals(name))
                return "User already exists";
        users.add(toAdd);
        return "New user added";
    }

    public void addEmployee(String name, String password) {
        Data toAdd = new Data(name, password);
        for(Data user : employees)
            if(user.name.equals(name))
                return ;
        employees.add(toAdd);
    }

    public String signIn(String name, String password) {
        Data toAdd = new Data(name, password);

        if(administrator.name.equals(name) && administrator.password.equals(password))
            return "Admin";

        for(Data employee : employees) {
            if(employee.name.equals(name) && employee.password.equals(password))
                return "Employee";
        }

        for(Data user : users) {
            if(user.name.equals(name) && user.password.equals(password))
                return "Client";
        }
        return "NO";
    }

    public void printUsers() {
        for(Data data : users)
            System.out.println(data.name);
    }
}
