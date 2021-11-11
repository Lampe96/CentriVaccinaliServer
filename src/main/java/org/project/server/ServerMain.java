package org.project.server;

import org.project.database.DbHelper;
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

            try {
                Statements.initializeDb();

                Runtime.getRuntime().addShutdownHook(
                        new Thread(() -> {
                            try {
                                DbHelper.closeStatement();
                                DbHelper.closeConnection();
                            } catch (SQLException e) {
                                e.printStackTrace();
                                System.exit(-1);
                            }
                        })
                );
            } catch (SQLException e) {
                e.printStackTrace();
                System.exit(-1);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        /*try {
            EmailUtil.sendVerifyEmail("fedemai22@gmail.com", "Fede", 999999);
            //EmailUtil.sendVerifyEmail("kuka621@gmail.com", "Kuka", 999999);
            //EmailUtil.sendVerifyEmail("lampe96@hotmail.it", "Lampe96", 999999);
            //EmailUtil.sendVerifyEmail("melania.lazzarin98@libero.it", "Mela" 999999);
            //EmailUtil.sendVerifyEmail("lampefede92@gmail.com", "Lampe", 999999);
        } catch (MessagingException | IOException e) {
            e.printStackTrace();
        }*/
    }
}
