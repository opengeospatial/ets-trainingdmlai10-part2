package org.opengis.cite.trainingdmlai10part2;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.SpecVersionDetector;

public class BaseJsonSchemaValidatorTest {
    public final int DEFAULT_BUFFER_SIZE = 8192;
    private ObjectMapper mapper = new ObjectMapper();

    //from https://github.com/networknt/json-schema-validator/blob/master/doc/quickstart.md
    public JsonNode getJsonNodeFromClasspath(String fileName) throws IOException {
        InputStream is1 = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream(fileName);
        return mapper.readTree(is1);
    }

    public JsonNode getJsonNodeFromStringContent(String content) throws IOException {
        return mapper.readTree(content);
    }

    public JsonNode getJsonNodeFromUrl(String url) throws IOException {
        return mapper.readTree(new URL(url));
    }

    public JsonSchema getJsonSchemaFromClasspath(String name) {
        JsonSchemaFactory factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V4);
        InputStream is = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream(name);
        return factory.getSchema(is);
    }

    public JsonSchema getJsonSchemaFromStringContent(String schemaContent) {
        JsonSchemaFactory factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V4);
        return factory.getSchema(schemaContent);
    }

    public JsonSchema getJsonSchemaFromUrl(String uri) throws URISyntaxException {
        JsonSchemaFactory factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V4);
        return factory.getSchema(new URI(uri));
    }

    public JsonSchema getJsonSchemaFromJsonNode(JsonNode jsonNode) {
        JsonSchemaFactory factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V4);
        return factory.getSchema(jsonNode);
    }

    /**
     * Get a JSON schema by providing a JSON node.
     * Automatically detect version for given JsonNode
     *
     * @param jsonNode
     * @return
     */
    public JsonSchema getJsonSchemaFromJsonNodeAutomaticVersion(JsonNode jsonNode) {
        JsonSchemaFactory factory = JsonSchemaFactory.getInstance(SpecVersionDetector.detect(jsonNode));
        return factory.getSchema(jsonNode);
    }

    // from https://mkyong.com/java/how-to-convert-inputstream-to-string-in-java/

    /**
     * Converts an InputStream to a String with buffering.
     *
     * @param is
     * @return
     * @throws IOException
     */
    public String otherConvertInputStreamToString(InputStream is) throws IOException {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        int length;
        while ((length = is.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }

        return result.toString("UTF-8");
    }

    public JsonSchema getSchema(String schemaName) throws IOException {
        InputStream inputStream = getClass().getResourceAsStream(schemaName);
        if (inputStream == null) {
            throw new IOException("Resource not found: " + schemaName + ".");
        }
        JsonNode schemaNode = getJsonNodeFromStringContent(otherConvertInputStreamToString(inputStream));
        JsonSchema schema = getJsonSchemaFromJsonNodeAutomaticVersion(schemaNode);

        schema.initializeValidators();

        return schema;
    }

    public JsonNode getNodeFromFile(java.io.File testSubject) throws IOException {
        JsonNode node = getJsonNodeFromStringContent(otherConvertInputStreamToString(new FileInputStream(testSubject)));
        return node;
    }
}