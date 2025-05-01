package Service;

import DAO.MessageDAO;
import Model.Message;

import java.util.List;

public class MessageService {

    private MessageDAO messageDAO;

    public MessageService() {
        this.messageDAO = new MessageDAO();
    }

    public MessageService(MessageDAO messageDAO) {
        this.messageDAO = messageDAO;
    }

    public Message createMessage(Message message) {
        try {
            if (message.getMessage_text() == null || message.getMessage_text().trim().isEmpty()) {
                throw new IllegalArgumentException("Message text cannot be blank");
            }
            if (message.getMessage_text().length() > 255) {
                throw new IllegalArgumentException("Message text cannot be longer than 255 characters");
            }

        return messageDAO.createMessage(message);
    } catch (IllegalArgumentException e) {
        e.printStackTrace();
        return null;
    } catch (Exception e) {
        e.printStackTrace();
        return null;
        }
    }

    public List<Message> getAllMessages() {
        try {
            return messageDAO.getAllMessages();
        } catch (Exception e) {
            e.printStackTrace();
            return null; 
        }
    }

    public Message getMessageById(int id) {
        try {
            return messageDAO.getMessageById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return null; 
        }
    }

    public Message deleteMessageById(int id) {
        try {
            Message message = messageDAO.getMessageById(id); 
            if (message != null) {
                return messageDAO.deleteMessage(id); 
            }
            return null; 
        } catch (Exception e) {
            e.printStackTrace();
            return null; 
        }
    }

    public Message updateMessageById(int id, Message updatedMessage) {
        try {
            if (updatedMessage.getMessage_text() == null || updatedMessage.getMessage_text().trim().isEmpty()) {
                throw new IllegalArgumentException("Message text cannot be blank");
            }
            if (updatedMessage.getMessage_text().length() > 255) {
                throw new IllegalArgumentException("Message text cannot be longer than 255 characters");
            }
            return messageDAO.updateMessage(id, updatedMessage.getMessage_text());
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return null; 
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Message> getAllMessagesForUser(int accountId) {
        try {
            return messageDAO.getMessagesByUser(accountId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}



