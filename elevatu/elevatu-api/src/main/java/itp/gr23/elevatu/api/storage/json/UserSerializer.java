package itp.gr23.elevatu.api.storage.json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonParseException;

import itp.gr23.elevatu.objects.User;

import java.lang.reflect.Type;

public final class UserSerializer implements JsonSerializer<User>,
        JsonDeserializer<User> {

    // keys for json object
    private final String usernameField = "username";
    private final String passwordHashField = "passwordHash";
    private final String passwordSaltField = "passwordSalt";
    @Override
    public User deserialize(final JsonElement jsonElement, final Type type,
                            final JsonDeserializationContext jsonDeserializationContext)
            throws JsonParseException {

        JsonObject jsonObject = jsonElement.getAsJsonObject();
        String userName = jsonObject.get(usernameField).getAsString();
        String passwordHashString = jsonObject.get(passwordHashField).getAsString();
        String passwordSaltString = jsonObject.get(passwordSaltField).getAsString();
        return new User(userName, passwordHashString, passwordSaltString);
    }

    @Override
    public JsonElement serialize(final User user, final Type type,
                                 final JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(usernameField, user.getUsername());
        jsonObject.addProperty(passwordHashField, user.getHashString());
        jsonObject.addProperty(passwordSaltField, user.getSaltString());
        return jsonObject;
    }
}
