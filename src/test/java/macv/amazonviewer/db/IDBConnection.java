package macv.amazonviewer.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

//Importar variables estáticas de la clase para llamarlas directamente
import static macv.amazonviewer.db.DataBaseParams.*;

public interface IDBConnection {
    default Connection getDBConnection (){
        Connection con = null;

        try {
        con = DriverManager.getConnection(
                URL + DB + "?useTimeZone=true&serverTimeZone=UTC",
                USER,
                PASSWORD
        );

        if (con != null){
            System.out.println("CONEXIÓN EXITOSA");
        }

        } catch (SQLException e){
            System.out.println(e);
            System.out.println("ERROR EN CONEXIÓN");

        }

        return con;
    }
}
