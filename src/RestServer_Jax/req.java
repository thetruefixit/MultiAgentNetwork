package RestServer_Jax;

/**
 * Created by Fixit on 19.05.2014.
 */

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@Path("/req/{req_data}")
public class req {
    @GET
   // @Produces("application/json"+";charset=utf-8")
    public String getClichedMessage(@PathParam("req_data") String id) {
        if(idCheck(id)) {
            ServerClass.ids.add(id);
            return "Retrived";
        }
        else return "Error id";
    }
    private boolean idCheck(String id) {
        boolean result = true;
            for (int i=0;i<id.length();i++) {
                if((id.charAt(i)>='0')&&(id.charAt(i)<='9'))
                  result = true;
                else {
                    result = false;
                    break;
                }
            }
        return result;
    }
}
