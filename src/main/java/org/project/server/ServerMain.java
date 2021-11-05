package org.project.server;

import org.project.database.Statements;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.SQLException;

public class ServerMain {

    public static void main(String[] args) {
        try {
            ServerImpl server = new ServerImpl();
            Registry reg = LocateRegistry.createRegistry(Server.PORT);
            reg.rebind(Server.NAME, server);
            System.out.println("SERVER PRONTO");
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        /*try {
            //EmailUtil.sendVerifyEmail("fedemai22@gmail.com", "Fede", 999999);
            //EmailUtil.sendVerifyEmail("kuka621@gmail.com", "Kuka", 999999);
            //EmailUtil.sendVerifyEmail("lampe96@hotmail.it", "Lampe96", 999999);
            //EmailUtil.sendVerifyEmail("melania.lazzarin98@libero.it", "Mela" 999999);
            //EmailUtil.sendVerifyEmail("lampefede92@gmail.com", "Lampe", 999999);
        } catch (MessagingException e) {
            e.printStackTrace();
        }*/

        try {
            Statements.initializeDb();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
