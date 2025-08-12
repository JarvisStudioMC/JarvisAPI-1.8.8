package fr.nkri.japi.utils.database;

import java.sql.*;

public class DataBase {

    private Connection connection;
    private final String host, user, password, dbName;
    private final int port;

    public DataBase(final String host, final String user, final String password, final String dbName, final int port){
        this.host = host;
        this.user = user;
        this.password = password;
        this.dbName = dbName;
        this.port = port;
    }

    //Récupérer la connexion pour les reqiestes
    public Connection getConnection() throws SQLException{
        ensureConnection();
        if(connection != null && !connection.isClosed()){
            return connection;
        }

        return connection;
    }

    //fermeture de la close (n'oublier pas de faire la connexion lors de la fermture du plugin !)
    public void closeConnection() throws SQLException {
        if(connection != null && !connection.isClosed()){
            connection.close();
        }
    }

    //Retourne un boolean en fonction de si la table existe, gère l'erreur avec un printStackTrace et renvoi false si la table n'exite pas.
    public boolean tableExist(final String tableName){
        try {
            return tableExists(tableName);
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    //Permet de re crée une connection si la connexion n'existe plus
    public void ensureConnection() throws SQLException {
        if(connection == null || (!connection.isValid(5))) {
            this.connection = DriverManager.getConnection(toURL(), getUser(), getPassword());
        }
    }

    //Retour un boulean en fonction de si la table existe ou non. (ne gère pas l'erreur)
    public boolean tableExists(final String tableName) throws SQLException {
        final DatabaseMetaData databaseMetaData = connection.getMetaData();
        final ResultSet resultSet = databaseMetaData.getTables(null, null, tableName, new String[]{"TABLE"});

        return resultSet.next();
    }

    public void openConnection(){
        try {
            this.connection = DriverManager.getConnection(toURL(), getUser(), getPassword());
            //LOGS SUCCES
        }
        catch (SQLException e) {
            //LOGS ERROR
            e.printStackTrace();
        }
    }

    public String toURL(){
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("jdbc:mysql://").append(getHost()).append(":").append(getPort()).append("/").append(getDbName());

        return stringBuilder.toString();
    }

    public String getHost() {
        return host;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public String getDbName() {
        return dbName;
    }

    public int getPort() {
        return port;
    }
}
