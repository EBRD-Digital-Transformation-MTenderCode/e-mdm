package com.procurement.mdm.model.dto.databinding

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.procurement.mdm.exception.ErrorType
import com.procurement.mdm.exception.InErrorException
import java.io.IOException


class BooleansDeserializer : JsonDeserializer<Boolean>() {

    @Throws(IOException::class)
    override fun deserialize(jsonParser: JsonParser, deserializationContext: DeserializationContext): Boolean {
        if (!jsonParser.currentToken.isBoolean) {
            throw InErrorException(ErrorType.INVALID_JSON_TYPE, jsonParser.currentName)
        }
        return jsonParser.valueAsBoolean
    }
}