package at.mse.walchhofer.smokee.utils;

public class JsonObject {
	private String name;
	private Object value;


	JsonObject() {
	}

	public JsonObject(String name, Object value) {
		this.name = name;
		this.value = value;
	}


	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{\"");
		sb.append(name.toString());
		sb.append("\":\"");
		sb.append(value.toString());
		sb.append("\"}");
		return sb.toString();
	}

	public static JsonObject name(String name) {
		JsonObject jsonObject = new JsonObject();
		jsonObject.name = name;
		return jsonObject;
	}

	public JsonObject value(Object value) {
		this.value=value;
		return this;
	}
}
