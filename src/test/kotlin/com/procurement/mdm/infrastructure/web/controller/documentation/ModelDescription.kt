package com.procurement.mdm.infrastructure.web.controller.documentation

import org.springframework.restdocs.payload.FieldDescriptor
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath

object ModelDescription {
    object Address {
        object Country {
            fun one(): List<FieldDescriptor> {
                return listOf(
                    getFieldDescriptor("data", "The data of response."),
                    getFieldDescriptor(
                        "data.scheme",
                        "The scheme is used to indicate the list or register from which the identifier is drawn."
                    ),
                    getFieldDescriptor("data.id", "The identifier of the country in the selected scheme."),
                    getFieldDescriptor("data.description", "The description of the country."),
                    getFieldDescriptor("data.uri", "A URI to identify the country.")
                )
            }

            fun collection(): List<FieldDescriptor> {
                return listOf(
                    getFieldDescriptor("data[]", "The data of response."),
                    getFieldDescriptor(
                        "data[].scheme",
                        "The scheme is used to indicate the list or register from which the identifier is drawn."
                    ),
                    getFieldDescriptor("data[].id", "The identifier of the country in the selected scheme."),
                    getFieldDescriptor("data[].description", "The description of the country."),
                    getFieldDescriptor("data[].uri", "A URI to identify the country.")
                )
            }
        }

        object Region {
            fun one(): List<FieldDescriptor> {
                return listOf(
                    getFieldDescriptor("data", "The data of response."),
                    getFieldDescriptor(
                        "data.scheme",
                        "The scheme is used to indicate the list or register from which the identifier is drawn."
                    ),
                    getFieldDescriptor("data.id", "The identifier of the region in the selected scheme."),
                    getFieldDescriptor("data.description", "The description of the region."),
                    getFieldDescriptor("data.uri", "A URI to identify the region.")
                )
            }
        }

        object Locality {
            fun one(): List<FieldDescriptor> {
                return listOf(
                    getFieldDescriptor("data", "The data of response."),
                    getFieldDescriptor(
                        "data.scheme",
                        "The scheme is used to indicate the list or register from which the identifier is drawn."
                    ),
                    getFieldDescriptor("data.id", "The identifier of the locality in the selected scheme."),
                    getFieldDescriptor("data.description", "The description of the locality."),
                    getFieldDescriptor("data.uri", "A URI to identify the locality.")
                )
            }

            fun collection(): List<FieldDescriptor> {
                return listOf(
                    getFieldDescriptor("data", "The data of response."),
                    getFieldDescriptor(
                        "data[].scheme",
                        "The scheme is used to indicate the list or register from which the identifier is drawn."
                    ),
                    getFieldDescriptor("data[].id", "The identifier of the locality in the selected scheme."),
                    getFieldDescriptor("data[].description", "The description of the locality."),
                    getFieldDescriptor("data[].uri", "A URI to identify the locality.")
                )
            }
            fun emptyCollection(): List<FieldDescriptor> {
                return listOf(
                    getFieldDescriptor("data", "Empty data of response.")
                )
            }
        }
    }

    object Organization {
        object Scheme {
            fun codes(): List<FieldDescriptor> {
                return listOf(
                    getFieldDescriptor("data", "The data of response."),
                    getFieldDescriptor(
                        "data.schemes",
                        "The scheme is used to indicate the list or register from which the identifier is drawn."
                    )
                )
            }
        }

        object Scale {
            fun codes(): List<FieldDescriptor> {
                return listOf(
                    getFieldDescriptor("data", "The data of response."),
                    getFieldDescriptor(
                        "data.scales",
                        "The scales is used to indicate the list or register from which the identifier is drawn."
                    )
                )
            }
        }
    }

    fun responseError(): List<FieldDescriptor> {
        return listOf(
            getFieldDescriptor("errors", "List of errors."),
            getFieldDescriptor("errors[].code", "The code of the error."),
            getFieldDescriptor("errors[].description", "The description of the error.")
        )
    }
}

private fun getFieldDescriptor(property: String, description: String): FieldDescriptor =
    fieldWithPath(property).description(description)
