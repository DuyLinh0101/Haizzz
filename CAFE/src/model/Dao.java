/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import cafe.Product;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class Dao {

    Connection con = MyConnection.getConnection();
    PreparedStatement ps;
    ResultSet rs;
    Statement st;
    private int pid;

    public void getallProducts(JTable table) {
        String sql = "select * from PRODUCT order by ProID desc";
        try {
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            DefaultTableModel model = (DefaultTableModel) table.getModel();

            Object[] row;
            while (rs.next()) {
                row = new Object[4];
                row[0] = rs.getInt(1);
                row[1] = rs.getString(2);
                row[2] = rs.getDouble(3);
                row[3] = rs.getBytes(4);
                model.addRow(row);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.getLogger(Dao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean insertProduct(Product p) {
        String sq1 = "insert into PRODUCT(ProName, Price, Images) values (?,?,?)";
        try {
            if (con == null || con.isClosed()) {
                System.out.println("Connection is not valid");
                return false;
            }
            if (p == null) {
                System.out.println("Product object is null");
                return false;

            }
            ps = con.prepareStatement(sq1);
            ps.setString(1, p.getName());
            ps.setDouble(2, p.getPrice());
            ps.setBytes(3, p.getImage());

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                if (rowsAffected > 0) {
                    return true;
                } else {
                    return false;
                }
            }
            return ps.executeUpdate() > 0;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }

    }

    public boolean update(Product product) {
        String sql = "update PRODUCT set ProName = ?, Price = ? where ProID = ?";
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, product.getName());
            ps.setDouble(2, product.getPrice());
            ps.setInt(3, product.getId());

            return ps.executeUpdate() > 0;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean delete(Product product) {
        try (PreparedStatement ps = con.prepareStatement("delete from PRODUCT where ProID=?")) {
            ps.setInt(1, product.getId());
            return ps.executeUpdate() > 0;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public int getMaxRowOrderTable() {
        int row = 0;

        try {
            st = con.createStatement();
            rs = st.executeQuery("SELECT max(CartID) from CART");
            while (rs.next()) {
                row = rs.getInt(1);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.getLogger(Dao.class.getName()).log(Level.SEVERE, null, ex);
        }

        return row + 1;

    }

    public boolean isProductExist(int cid, int pid) {
        try {
            ps = con.prepareStatement("SELECT * FROM CART WHERE CartID=? and PaymentID=?");
            ps.setInt(1, cid);
            ps.setInt(2, pid);
            rs = ps.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.getLogger(Dao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;

    }

    public boolean insertCart(Cart cart) {
        String sql = "INSERT INTO CART (CartID,PaymentID,ProName,Quantity,Price,Total) values(?,?,?,?,?,?)";
        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, cart.getId());
            ps.setInt(2, cart.getPid());
            ps.setString(3, cart.getpName());
            ps.setInt(4, cart.getQty());
            ps.setDouble(5, cart.getPrice());
            ps.setDouble(6, cart.getTotal());
            return ps.executeUpdate() > 0;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;

        }

    }

    public int getMaxRowPaymentTable() {
        int row = 0;

        try {
            st = con.createStatement();
            rs = st.executeQuery("SELECT max(PaymentID) from PAYMENT");
            while (rs.next()) {
                row = rs.getInt(1);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.getLogger(Dao.class.getName()).log(Level.SEVERE, null, ex);
        }

        return row + 1;

    }

    public int getMaxRowACartTable() {
        int row = 0;

        try {
            st = con.createStatement();
            rs = st.executeQuery("SELECT max(CartID) from CART");
            while (rs.next()) {
                row = rs.getInt(1);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.getLogger(Dao.class.getName()).log(Level.SEVERE, null, ex);
        }

        return row;

    }

    public double subTotal() {
        double subTotal = 0.0;
        int cid = getMaxRowACartTable();

        try {
            st = con.createStatement();
            rs = st.executeQuery("Select sum(Total) as 'Total' from CART where CartID = '" + cid + "'");

            if (rs.next()) {
                subTotal = rs.getDouble(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Dao.class.getName()).log(Level.SEVERE, null, ex);
        }

        return subTotal;
    }
    
    public void getProductsFromCart(JTable table) {
        int cid = getMaxRowACartTable();
        String sql = "select * from CART WHERE CartID=?";
        
        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, cid);
            rs = ps.executeQuery();
            DefaultTableModel model = (DefaultTableModel) table.getModel();

            Object[] row;
            while (rs.next()) {
                row = new Object[6];
                row[0] = rs.getInt(1);
                row[1] = rs.getInt(1);
                row[2] = rs.getString(3);
                row[3] = rs.getInt(4);
                row[4] = rs.getDouble(5);
                row[5] = rs.getDouble(6);
                model.addRow(row);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.getLogger(Dao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public boolean insertPayment(Payment payment) {
        String sql = "INSERT INTO PAYMENT (PaymentID,CusName,ProId,PaymentName,Total,PaymentDate) values(?,?,?,?,?,?)";
        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, payment.getPid());
            ps.setString(2, payment.getcName());
            ps.setString(3, payment.getProId());
            ps.setString(4, payment.getProName());
            ps.setDouble(5, payment.getTotal());
            ps.setString(6, payment.getDate());
            return ps.executeUpdate() > 0;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;

        }

    }
     public boolean deleteCart(int cid) {
        try {
            ps=con.prepareStatement("DELETE FROM CART WHERE CartID=?");
            ps.setInt(1,cid);
            return ps.executeUpdate()>0;
        } catch (Exception ex) {
            return false;
        }
        
    }
     public void getPaymentDetails(JTable table) {
        int cid = getMaxRowACartTable();
        String sql = "select * from PAYMENT ORDER BY PaymentID desc";
        
        try {
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            DefaultTableModel model = (DefaultTableModel) table.getModel();

            Object[] row;
            while (rs.next()) {
                row = new Object[6];
                row[0] = rs.getInt(1);
                row[1] = rs.getString(2);
                row[2] = rs.getString(3);
                row[3] = rs.getString(4);
                row[4] = rs.getDouble(5);
                row[5] = rs.getString(6);
                model.addRow(row);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.getLogger(Dao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
     public int totalProduct(){
         int total=0;
         
        try {
            rs=st.executeQuery("SELECT COUNT(*) as 'total' FROM PRODUCT");
            st=con.createStatement();
            if(rs.next()){
                total=rs.getInt(1);
            }
        } catch (Exception ex) {
            Logger.getLogger(Dao.class.getName()).log(Level.SEVERE, null, ex);
        }
         return total;
     }
      public int todayRevenue(String date){
         int total=0;
         
        try {
            rs=st.executeQuery("SELECT COUNT(*) as 'total' FROM PRODUCT");
            st=con.createStatement();
            if(rs.next()){
                total=rs.getInt(1);
            }
        } catch (Exception ex) {
            Logger.getLogger(Dao.class.getName()).log(Level.SEVERE, null, ex);
        }
         return total;
     }
}
