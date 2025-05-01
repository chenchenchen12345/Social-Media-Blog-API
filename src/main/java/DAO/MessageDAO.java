package DAO;

import Model.Message;
import Util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageDAO {

    private Connection conn;

    public MessageDAO(Connection conn) {
        this.conn = conn;
    }

    public MessageDAO() {
        this.conn = ConnectionUtil.getConnection();
    }

    public Message createMessage(Message message) throws SQLException {
        
        if (message.getMessage_text().isEmpty() || message.getMessage_text().length() > 255 || message.getPosted_by() <= 0) {
            throw new SQLException("Invalid message text or user.");
        }

        String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, message.getPosted_by()); 
            stmt.setString(2, message.getMessage_text());  
            stmt.setLong(3, message.getTime_posted_epoch());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    message.setMessage_id(rs.getInt(1));  
                }
            }
            return message;
        }
    }

    public List<Message> getAllMessages() throws SQLException {
        String sql = "SELECT * FROM message";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            try (ResultSet rs = stmt.executeQuery()) {
                List<Message> messages = new ArrayList<>();
                while (rs.next()) {
                    messages.add(new Message(
                            rs.getInt("message_id"),
                            rs.getInt("posted_by"),
                            rs.getString("message_text"),
                            rs.getLong("time_posted_epoch")
                    ));
                }
                return messages;
            }
        }
    }

    public Message getMessageById(int messageId) throws SQLException {
        String sql = "SELECT * FROM message WHERE message_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, messageId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Message(
                            rs.getInt("message_id"),
                            rs.getInt("posted_by"),
                            rs.getString("message_text"),
                            rs.getLong("time_posted_epoch")
                    );
                }
            }
        }
        return null;
    }

    public Message deleteMessage(int messageId) throws SQLException {
        Message message = getMessageById(messageId);
        if (message == null) {
            return null;
        }

        String sql = "DELETE FROM message WHERE message_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, messageId);
            stmt.executeUpdate();
        }
        return message;
    }

    public Message updateMessage(int messageId, String newText) throws SQLException {
        if (newText.isEmpty() || newText.length() > 255) {
            throw new SQLException("Invalid message text.");
        }

        String sql = "UPDATE message SET message_text = ? WHERE message_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newText);
            stmt.setInt(2, messageId);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) return null;  
        }

        return getMessageById(messageId);
    }

    public List<Message> getMessagesByUser(int accountId) throws SQLException {
        String sql = "SELECT * FROM message WHERE posted_by = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, accountId);
            try (ResultSet rs = stmt.executeQuery()) {
                List<Message> messages = new ArrayList<>();
                while (rs.next()) {
                    messages.add(new Message(
                            rs.getInt("message_id"),
                            rs.getInt("posted_by"),
                            rs.getString("message_text"),
                            rs.getLong("time_posted_epoch")
                    ));
                }
                return messages;
            }
        }
    }
}


