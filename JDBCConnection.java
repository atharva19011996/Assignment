import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
public class JDBCConnection {
    public void inquiry(Statement stmt){
        try{
            ResultSet rs = stmt.executeQuery("select itemName,itemCost from productList where itemName='Watch'");
            String itemName = "";
            double itemCostUSD = 0;
            double itemCostEUR = 0;
            double currencyValue = 0;
            String currencyAlpha = "EUR";
            while(rs.next()){
                itemName = rs.getString("itemName");
                itemCostUSD = rs.getDouble("itemCost");
            }
            rs = stmt.executeQuery("select currencyValue from exRate where currencyAlpha='EUR'");
            while(rs.next()){
                currencyValue = rs.getDouble("currencyValue");
                itemCostEUR = itemCostUSD * currencyValue;
            }
            System.out.println(itemName+" => "+currencyAlpha+":("+itemCostUSD+" X "+currencyValue+")"+"="+itemCostEUR+", USD:("+itemCostUSD+")");
        }
        catch(SQLException ex){
            System.out.println(ex.getMessage());
        }
    }
    public void insertProduct(Statement stmt, String itemName, double costInUSD){
        try{
            String sqlString = "Insert into ProductList(itemName,itemCost) values("+"'"+itemName+"',"+costInUSD+")";
            stmt.executeUpdate(sqlString);
        }
        catch (SQLException ex){
            System.out.println(ex.getMessage());
        }
    }
    public static void main(String [] args){
        Connection conn = null;
        Statement stmt = null;
        try{
            DriverManager.registerDriver(new com.mysql.jdbc.Driver());
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/JDBCAssignment?"+"user=root&password=AgreeYa&useSSL=false");
            stmt = conn.createStatement();
            JDBCConnection connObject = new JDBCConnection();
            connObject.inquiry(stmt);
            connObject.insertProduct(stmt,"Phone",650);
        }
        catch(SQLException ex){
            System.out.println(ex.getMessage());
        }
    }
}
