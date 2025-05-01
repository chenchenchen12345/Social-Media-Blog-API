package Service;

import DAO.MessageDAO;
import Model.Message;

import java.util.List;

public class MessageService {

    private MessageDAO messageDAO;
    private AccountService accountService; 

    public MessageService() {
        this.messageDAO = new MessageDAO();
        this.accountService = new AccountService();
    }

    public MessageService(MessageDAO messageDAO, AccountService accountService) {
        this.messageDAO = messageDAO;
        this.accountService = accountService;
    }

    public Message createMessage(Message message) {
        try {
            if (message.getMessage_text() == null || message.getMessage_text().trim().isEmpty()) {
                throw new IllegalArgumentException("Message text cannot be blank");
            }
            if (message.getMessage_text().length() > 255) {
                throw new IllegalArgumentException("Message text cannot be longer than 255 characters");
            }
            if (message.getPosted_by() <= 0) {
                throw new IllegalArgumentException("Posted_by must be a positive user ID");
            }
            if (accountService.getAccountById(message.getPosted_by()) == null) {
                throw new IllegalArgumentException("User does not exist");
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



