package com.procurement.mdm.model.dto.databinding

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonToken
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.procurement.mdm.exception.ErrorType
import com.procurement.mdm.exception.InErrorException
import java.io.IOException


class StringsDeserializer : JsonDeserializer<String>() {

    @Throws(IOException::class)
    override fun deserialize(jsonParser: JsonParser, deserializationContext: DeserializationContext): String {
        if (jsonParser.currentToken != JsonToken.VALUE_STRING) {
            throw InErrorException(ErrorType.INVALID_JSON_TYPE, jsonParser.currentName)
        }
        return jsonParser.valueAsString
    }
}