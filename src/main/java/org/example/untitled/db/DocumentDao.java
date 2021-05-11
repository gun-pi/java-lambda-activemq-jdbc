package org.example.untitled.db;

import java.sql.*;

public class DocumentDao {

    private static final String DATABASE_URL =
            "jdbc:postgresql://postgresdatabasepublic.cx96u0a6s3vd.eu-central-1.rds.amazonaws.com:5432/postgres";
    private static final String DATABASE_USERNAME = "postgres";
    private static final String DATABASE_PASSWORD = "exampleexample";
    private static final String QUERY = "INSERT INTO documents (content, published_on) VALUES (?, ?) RETURNING id";

    public static Long save(DocumentEntity documentEntity) {
        try (Connection connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(QUERY, Statement.RETURN_GENERATED_KEYS)) {
            connection.setAutoCommit(false);
            statement.setString(1, documentEntity.getContent());
            statement.setTimestamp(2, Timestamp.valueOf(documentEntity.getPublishedOn()));
            statement.executeUpdate();

            ResultSet rs = statement.getGeneratedKeys();
            Long id = null;
            if (rs.next()) {
                id = rs.getLong(1);
            }
            connection.commit();
            return id;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
