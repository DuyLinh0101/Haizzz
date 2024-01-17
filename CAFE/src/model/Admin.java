
package model;


public class Admin {
    private int ID;
    private String UserName;
    private String PassWord;
    private String Email;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String UserName) {
        this.UserName = UserName;
    }

    public String getPassWord() {
        return PassWord;
    }

    public void setPassWord(String PassWord) {
        this.PassWord = PassWord;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public int getMaxRowAdminTable() {
        AdminDao adminDao=new AdminDao();
        return adminDao.getMaxRowAdminTable();
    }

    public boolean login(String username, String password) {
        AdminDao adminDao=new AdminDao();
        return adminDao.login(username, password);
    }
    
}
