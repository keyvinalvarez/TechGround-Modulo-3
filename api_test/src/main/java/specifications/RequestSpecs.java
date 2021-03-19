package specifications;

import java.util.Base64;
import helpers.RequestHelper;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;

public class RequestSpecs {

    public static RequestSpecification generateToken(){
        RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();

        String token = RequestHelper.getUserToken();

        requestSpecBuilder.addHeader("Authorization", "Bearer " + token);
        return requestSpecBuilder.build();
    };

    public static RequestSpecification generateFakeToken(){
        RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();
        requestSpecBuilder.addHeader("Authorization", "Beasadrer wrongtoken");
        return requestSpecBuilder.build();
    };

    public static RequestSpecification basicAuthentication(){
        RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();

        String authBasic = new String(Base64.getEncoder().encode(String.format("%s:%s", "testuser", "testpass").getBytes()));
        requestSpecBuilder.addHeader("Authorization", "Basic " + authBasic);

        return requestSpecBuilder.build();
    };

}
