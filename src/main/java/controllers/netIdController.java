package controllers;

import javax.ws.rs.*;

@Path("/netid")
public class netIdController {
    @GET
    public String getNetId() {
        return "gkc24";
    }
}
