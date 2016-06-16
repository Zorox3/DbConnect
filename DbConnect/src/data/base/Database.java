package data.base;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
    private static boolean debug = false;
    private Connection connection;

    private Statement statement;

    private DbData data = new DbData();

    public Database(String databasePath, String databaseName, String username, String password) {
            try {
                    Class.forName("com.mysql.jdbc.Driver");
                    this.connection = DriverManager
                                    .getConnection("jdbc:mysql:"+databasePath+"/"+databaseName+"?user="+username+"&password="+password);

            } catch (Exception e) {
                    System.err.println(e.getClass().getName() + ": " + e.getMessage());
                    System.exit(0);
            }
    }


    public Database(String dbName) {
            try {
                    Class.forName("org.sqlite.JDBC");
                    this.connection = DriverManager
                                    .getConnection("jdbc:sqlite:db/"+dbName+".db");

            } catch (Exception e) {
                    System.err.println(e.getClass().getName() + ": " + e.getMessage());
                    System.exit(0);
            }
    }

    public DbData getData() {
            return data;
    }


    /**
     *
     * @param debug
     * SQL-Statement / Data to Console
     */
    public static void setDebug(boolean debug) {
            Database.debug = debug;
    }




    /** EXECUTE START **/
    public boolean execute(String sql, boolean isQuery) {

            if (Database.debug) {
                    System.err.println(sql);
            }

            try {
                    this.statement = connection.createStatement();
            } catch (SQLException sqle) {
                    return false;
            }

            try {
                    if (isQuery) {

                            ResultSet rs = this.statement.executeQuery(sql);
                            this.data.clear();

                            while (rs.next()) {
                                    int columnCount = rs.getMetaData().getColumnCount();
                                    DbValue row = new DbValue();

                                    for (int i = 1; i <= columnCount; i++) {
                                            String rowName = rs.getMetaData().getColumnLabel(i);
                                            row.add(rowName, rs.getString(i));
                                    }

                                    data.add(rs.getInt(1), row);

                            }
                            rs.close();
                            this.statement.close();
                            return true;
                    } else {
                            this.statement.executeUpdate(sql);
                            this.statement.close();
                            return true;
                    }
            } catch (SQLException sqle) {
                    sqle.getStackTrace();
                    System.err.println(sqle.getMessage());
                    return false;
            }
    }

    /** EXECUTE END **/






    /** SELECT START **/

    public DbData select(String tbl, String select, String where,
                    String order, String group, String limit) {
            String sql = "SELECT";

            sql += " " + select;
            sql += " FROM " + tbl;
            if (where != "-1") {
                    sql += " WHERE " + where;
            }
            if (order != "-1") {
                    sql += " ORDER BY " + order;
            }
            if (group != "-1") {
                    sql += " GROUP BY " + group;
            }
            if (limit != "-1") {
                    sql += " LIMIT " + limit;
            }

            if(this.execute(sql, true)){
            	return data;
            }else{
            	return null;
            }
            
            

    }

    public DbData select(String tbl) {
            return select(tbl, "*", "-1", "-1", "-1", "-1");
    }

    public DbData select(String tbl, String select) {
            return select(tbl, select, "-1", "-1", "-1", "-1");
    }

    public DbData select(String tbl, String select, String where) {
            return select(tbl, select, where, "-1", "-1", "-1");
    }

    public DbData select(String tbl, String select, String where, String order) {
            return select(tbl, select, where, order, "-1", "-1");
    }

    public DbData select(String tbl, String select, String where,
                    String order, String group) {
            return select(tbl, select, where, order, group, "-1");
    }

    /** SELECT END **/



    /** UPDATE START **/

    public boolean update(String tbl, String update, String where) {

            String upt = "UPDATE " + tbl;

            if (update != "-1")
                    upt += " SET " + update;

            if (where != "-1")
                    upt += " WHERE " + where;

            return execute(upt, false);

    }

    /** UPDATE END **/




    /** INSERT START **/

    public boolean insert(String tbl, String[] col, String[] var) {
            return insert(tbl, col, var);
    }

    public boolean insert(String tbl, String[] col, int[] var) {
            return insert(tbl, col, var);
    }

    public boolean insert(String tbl, String[] col, Object[] var) {
            String out_var = "";
            String out_col = "";

            for (int i = 0; i <= var.length - 1; i++) {
                    if (out_var != "")
                            out_var += ", ";
                    out_var += "'" + var[i] + "'";
            }

            for (int j = 0; j <= col.length - 1; j++) {
                    if (out_col != "")
                            out_col += ", ";
                    out_col += "`" + col[j] + "`";
            }

            String insert = "INSERT INTO `" + tbl + "` (" + out_col + " ) VALUES ("
                            + out_var + ")";

            return execute(insert, false);
    }

    /** INSERT END **/



    /** DELETE START **/
    public boolean delete(String tbl, String where) {

            String del = "DELETE FROM " + tbl + " WHERE " + where;

            //select(tbl, "*", where);

            return execute(del, false);

    }
    /** DELETE END **/

}
