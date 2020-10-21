package epam.hospital;

import by.epam.hospital.connection.ConnectionException;
import by.epam.hospital.connection.ConnectionPool;

import java.sql.*;

public class Temp {
    public static final String SQL = "INSERT INTO icd (code, title) VALUES (?, ?)";
    public static final String SQL_FIND = "SELECT code FROM icd WHERE id = ?";

    public static void main(String[] args) {
        try {
//            File file = new File("C:\\Education\\java\\projects\\hospital\\icd10pcs_codes_2020.txt");
            Connection connection = ConnectionPool.getInstance().getConnection();
            int i = 1;
//            Scanner scanner = new Scanner(file);
//            while (scanner.hasNextLine()) {
            while (i < 77566) {
                PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND);
                preparedStatement.setInt(1, i);
                preparedStatement.execute();
                ResultSet resultSet = preparedStatement.getResultSet();
                resultSet.next();
                try {
                    resultSet.wasNull();
                } catch (NullPointerException e) {
                    System.out.println(i);
                }
//                String data = scanner.nextLine();
//                PreparedStatement preparedStatement = connection.prepareStatement(SQL);
//
//                String[] icdValues = data.split(" ", 2);
//                preparedStatement.setString(1, icdValues[0]);
//                preparedStatement.setString(2, icdValues[1]);
//                preparedStatement.execute();
//                preparedStatement.close();

//                for (String ret: data.split(" ", 2)){
//                    System.out.println(ret);
//                }
                resultSet.close();
                preparedStatement.close();
                i++;
//                System.out.println(i++);
            }
//            scanner.close();
            connection.close();
//        } catch (FileNotFoundException e) {
//            System.out.println("An error occurred.");
//            e.printStackTrace();
        } catch (ConnectionException | SQLException e) {
            e.printStackTrace();
        }
    }
}
