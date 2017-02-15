package com.buttering.roler.net.serialization;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * Created by kinamare on 2016-12-17.
 */

public class RolerResponseDeserializer implements JsonDeserializer<RolerResponse> {

	@Override
	public RolerResponse deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

		final JsonObject jsonObject = json.getAsJsonObject();

		final String message = jsonObject.get("message").getAsString();
		final int statusCode = jsonObject.get("statusCode").getAsInt();
		final String param = jsonObject.get("param").getAsString();

		final RolerResponse response = new RolerResponse();
		response.setMessage(message);
		response.setParam(param);
		response.setStatusCode(statusCode);

		return response;
	}
}