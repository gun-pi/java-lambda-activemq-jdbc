package org.example.untitled.db;

import java.sql.*;
import java.util.Random;

public class DocumentDao {

    private static final String URL =
            "jdbc:postgresql://database-1.cx96u0a6s3vd.eu-central-1.rds.amazonaws.com:5432/postgres";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "exampleexample";
    private static final String QUERY =
            "INSERT INTO documents (content, published_on, id) VALUES (?, ?, ?) RETURNING id";

    private static final Random random = new Random();

    private static final Connection connection;

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Long save(DocumentEntity documentEntity) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(QUERY, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setString(1, documentEntity.getContent());
        preparedStatement.setTimestamp(2, Timestamp.valueOf(documentEntity.getPublishedOn()));
        preparedStatement.setLong(3, random.nextLong());
        preparedStatement.executeUpdate();

        ResultSet rs = preparedStatement.getGeneratedKeys();
        Long id = null;
        if (rs.next()) {
            id = rs.getLong(1);
        }
        return id;
    }
}
