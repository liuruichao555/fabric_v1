package com.liuruichao.model;

import lombok.Getter;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

/**
 * Attribute
 *
 * @author liuruichao
 * Created on 2017/3/22 18:17
 */
@Getter
public class Attribute {
    private String name;
    private String value;

    public Attribute(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public JsonObject toJsonObject() {
        JsonObjectBuilder ob = Json.createObjectBuilder();
        ob.add("name", this.name);
        ob.add("value", this.value);
        return ob.build();
    }

}
