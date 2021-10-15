package org.project;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ServerMain {

    public static void main(String[] args) {
        try {
            ServerImpl server = new ServerImpl();
            Registry reg = LocateRegistry.createRegistry(Server.PORT);
            reg.rebind("SERVER", server);

            System.out.println("AAAAAAAAAAAA" + server);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
