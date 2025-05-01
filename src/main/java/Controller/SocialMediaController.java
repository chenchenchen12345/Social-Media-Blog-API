package Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;
import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {

     private AccountService accountService;
     private MessageService messageService;

     public SocialMediaController() {
        this.accountService = new AccountService(); 
        this.messageService = new MessageService(); 
    }

    public SocialMediaController(AccountService accountService, MessageService messageService) {
        this.accountService = accountService;
        this.messageService = messageService;
    }

    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();

        //account endpoints
        app.post("/register", this::registerUser);
        app.post("/login", this::loginUser);

        // message endpoints
        app.post("/messages", this::createMessage);
        app.get("/messages", this::getAllMessages);
        app.get("/messages/{message_id}", this::getMessageById);
        app.delete("/messages/{message_id}", this::deleteMessageById);
        app.patch("/messages/{message_id}", this::updateMessageById);
        app.get("/accounts/{user_id}/messages", this::getAllMessagesForUser);


        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void registerUser(Context ctx) {
        Account account = ctx.bodyAsClass(Account.class);
        Account registered = accountService.registerUser(account);
            if (registered != null) {
                ctx.status(200).json(registered);
            } else {
                ctx.status(400).result("");
            }
    }

    private void loginUser(Context ctx) {
        Account credentials = ctx.bodyAsClass(Account.class);
        Account loggedIn = accountService.loginUser(credentials);
            if (loggedIn != null) {
                ctx.status(200).json(loggedIn);
            } else {
                ctx.status(401).result("");
            }
    }

    public void createMessage(Context ctx) {
        try {
            Message message = ctx.bodyAsClass(Message.class);
            Message created = messageService.createMessage(message);
            if (created != null) {
                ctx.status(200).json(created);
            } else {
                ctx.status(400).result(""); 
            }
        } catch (Exception e) {
            ctx.status(500).result(""); 
            e.printStackTrace(); 
        }
    }

    private void getAllMessages(Context ctx) {
        List<Message> messages = messageService.getAllMessages();
        ctx.status(200).json(messages);
    }

    private void getAllMessagesForUser(Context ctx) {
        int userId = Integer.parseInt(ctx.pathParam("user_id"));
        List<Message> messages = messageService.getAllMessagesForUser(userId);
            if (messages != null && !messages.isEmpty()) {
                ctx.status(200).json(messages);  
            } else {
                ctx.status(200).json(new ArrayList<>());  
            }
}

    private void getMessageById(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("message_id"));
        Message message = messageService.getMessageById(id);
            if (message != null) {
                ctx.status(200).json(message);
            } else {
                ctx.status(200).result("");
            }
    }

    private void deleteMessageById(Context ctx) {
    int id = Integer.parseInt(ctx.pathParam("message_id"));
    try {
        Message deletedMessage = messageService.deleteMessageById(id);
            if (deletedMessage != null) {
                ctx.status(200).json(deletedMessage);  
            } else {
                ctx.status(200).result("");  
            }
        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).result("Internal Server Error");
        }
}

    private void updateMessageById(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("message_id"));
        Message updates = ctx.bodyAsClass(Message.class);
        Message updated = messageService.updateMessageById(id, updates);
            if (updated != null) {
                ctx.status(200).json(updated);
            } else {
                ctx.status(400).result("");
            }
    }
}