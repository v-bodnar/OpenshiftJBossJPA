package ua.openshift.test;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import ua.openshift.entities.User;

/**
 * Servlet implementation class Servlet
 */
@WebServlet("/Servlet")
public class Servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	@PersistenceContext(unitName="DoctorVera")
	private EntityManager em;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Servlet() {
        super();
       
    }
    
	public static Connection getConnection() {
		try {
			InitialContext ic = new InitialContext();
			Context initialContext = (Context) ic.lookup("java:comp/env");
			DataSource datasource = (DataSource) initialContext.lookup("java:jboss/datasources/MySQLDS");
			//DataSource datasource = (DataSource) ic.lookup("java:jboss/datasources/MySQLDS");
			return datasource.getConnection();
		} catch (Exception e) {
			System.out.println(e.getLocalizedMessage());
			return null;
		}
	}
	protected List<User> parseResultSet(ResultSet rs){
		LinkedList<User> result = new LinkedList<User>();
		try {
			while (rs.next()) {
				User user = new User();
				user.setId(rs.getInt("Id"));
				user.setLogin(rs.getString("login"));
				user.setName(rs.getString("name"));
			}
		} catch (Exception e) {
			e.printStackTrace();;
		}
		return result;
	}
	
    public User findByPK(Integer key){
        List<User> list = null;
        String sql = "SELECT * FROM  Users  WHERE id = ? ;";
        try (PreparedStatement statement = getConnection().prepareStatement(sql)) {
            statement.setInt(1, key);
            ResultSet rs = statement.executeQuery();
            list = parseResultSet(rs);
        } catch (Exception e) {
            e.printStackTrace();;
        }
        
        return list.iterator().next();
    }
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter pw = response.getWriter();
		pw.print("Servlet");
		//User user = em.find(User.class,1);
		pw.print(findByPK(1).getName());
		//pw.print(user.getName());
		pw.flush();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
