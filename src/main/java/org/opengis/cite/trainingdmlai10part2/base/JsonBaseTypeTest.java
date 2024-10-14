package org.opengis.cite.trainingdmlai10part2.base;

import com.ethlo.time.ITU;
import com.fasterxml.jackson.databind.JsonNode;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.ValidationMessage;
import org.opengis.cite.trainingdmlai10part2.BaseJsonSchemaValidatorTest;
import org.opengis.cite.trainingdmlai10part2.CommonFixture;
import org.opengis.cite.trainingdmlai10part2.SuiteAttribute;
import org.opengis.cite.trainingdmlai10part2.util.JsonUtils;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileInputStream;
import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JsonBaseTypeTest extends CommonFixture {
    private File testSubject;

    /**
     * Obtains the test subject from the ISuite context. The suite attribute
     * {@link org.opengis.cite.trainingdmlai10part2.SuiteAttribute#TEST_SUBJECT} should
     * evaluate to a DOM Document node.
     *
     * @param testContext The test (group) context.
     */
    @BeforeClass
    public void obtainTestSubject(ITestContext testContext) {
        Object obj = testContext.getSuite().getAttribute(SuiteAttribute.TEST_SUBJECT.getName());
        this.testSubject = (File) obj;
    }

    /**
     * Sets the test subject. This method is intended to facilitate unit
     * testing.
     *
     * @param testSubject A Document node representing the test subject or
     *                    metadata about it.
     */
    public void setTestSubject(File testSubject) {
        this.testSubject = testSubject;
    }

    /**
     * Verify that the document is well-formed JSON.
     */
    @Test(description = "Implements Abstract Test 1 (/conf/base/jsonbasetype/json)")
    public void verifyJson() {
        if (!testSubject.isFile()) {
            Assert.assertTrue(testSubject.isFile(), "No file selected. ");
        }
        JsonNode node = null;
        BaseJsonSchemaValidatorTest tester = new BaseJsonSchemaValidatorTest();
        try {
            node = tester.getJsonNodeFromStringContent(tester.otherConvertInputStreamToString(new FileInputStream(testSubject)));
        } catch (Exception e) {
            Assert.fail(e.getMessage());
            e.printStackTrace();
        }
        Assert.assertTrue(node != null, "Invalid JSON file. ");
    }

    /**
     * Verify that JSON instance documents claiming conformance to this specification contain valid DateTime values according
     * to Date and Time on the Internet: Timestamps <a href="https://datatracker.ietf.org/doc/html/rfc3339#section-5.6">RFC 3339 Section 5.6</a>.
     */
    @Test(description = "Implements Abstract Test 2 (/conf/base/jsonbasetype/datetime)")
    public void validateDateTime() {
        if (!testSubject.isFile()) {
            Assert.assertTrue(testSubject.isFile(), "No file selected. ");
        }

        StringBuffer sb = new StringBuffer();
        try {
            BaseJsonSchemaValidatorTest tester = new BaseJsonSchemaValidatorTest();
            JsonNode rootNode = tester.getNodeFromFile(testSubject);

            String[] arrayToFetch = {"createdTime", "updatedTime"};
            List<JsonNode> nodes = JsonUtils.findNodesByNames(rootNode, arrayToFetch);

            for (JsonNode targetNode : nodes) {
                if (targetNode.isTextual()) {
                    if (!isValidRFC3339(targetNode.asText())) {
                        sb.append("Invalid RFC 3339 date time: " + targetNode.asText() + ".\n");
                    }
                }
            }
        } catch (Exception e) {
            sb.append(e.getMessage());
            e.printStackTrace();
        }

        Assert.assertTrue(sb.toString().length() == 0, sb.toString());
    }

    /**
     * The following profile of ISO 8601 [ISO8601] dates SHOULD be used in new protocols on the Internet.  This is specified using the syntax description notation defined in [ABNF].
     */
    private boolean isValidRFC3339(String input) {
        try {
            OffsetDateTime dateTime = ITU.parseDateTime(input);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Verify that JSON instance documents claiming conformance to this specification validate against the JSON schema specified in <a href="http://schemas.opengis.net/trainingdml-ai/1.0/namedValue.json">namedValue.json</a>.
     */
    @Test(description = "Implements Abstract Test 3 (/conf/base/jsonbasetype/namedvalue)")
    public void validateNamedValue() {
        if (!testSubject.isFile()) {
            Assert.assertTrue(testSubject.isFile(), "No file selected. ");
        }

        String schemaToApply = SCHEMA_PATH + "namedValue.json";
        StringBuffer sb = new StringBuffer();

        try {
            BaseJsonSchemaValidatorTest tester = new BaseJsonSchemaValidatorTest();

            JsonSchema schema = tester.getSchema(schemaToApply);
            JsonNode rootNode = tester.getNodeFromFile(testSubject);

            String[] arrayToFetch = {"statisticsInfo", "classes", "metrics"};

            List<JsonNode> nodes = JsonUtils.findNodesByNames(rootNode, arrayToFetch);
            for (JsonNode targetNode : nodes) {
                for (int i = 0; i < targetNode.size(); i++) {
                    JsonNode currentNode = targetNode.get(i);
                    String nodeClass = currentNode.getClass().toString();
                    if (nodeClass.endsWith("com.fasterxml.jackson.databind.node.ObjectNode")) {
                        Set<ValidationMessage> errors = schema.validate(currentNode);
                        Iterator it = errors.iterator();
                        while (it.hasNext()) {
                            sb.append("Item " + i + " has error " + it.next() + ".\n");
                        }
                    }
                }
            }
        } catch (Exception e) {
            sb.append(e.getMessage());
            e.printStackTrace();
        }
        Assert.assertTrue(sb.toString().length() == 0, sb.toString());
    }

    /**
     * Verify that JSON instance documents claiming conformance to this specification contain valid URL values according to Uniform Resource Identifier (URI): Generic Syntax <a href="https://datatracker.ietf.org/doc/html/rfc3986#section-4.1">RFC 3986 Section 4.1</a>. A URL value can be absolute or relative and may have an optional fragment identifier.
     */
    @Test(description = "Implements Abstract Test 4 (/conf/base/jsonbasetype/url)")
    public void validateUrl() {
        if (!testSubject.isFile()) {
            Assert.assertTrue(testSubject.isFile(), "No file selected. ");
        }

        String URI_REGEX = "^(([^:/?#]+):)?(//([^/?#]*))?([^?#]*)(\\?([^#]*))?(#(.*))?";
        Pattern URI_PATTERN = Pattern.compile(URI_REGEX);
        BaseJsonSchemaValidatorTest tester = new BaseJsonSchemaValidatorTest();

        StringBuffer sb = new StringBuffer();

        boolean valid = true;
        try {
            JsonNode rootNode = tester.getNodeFromFile(testSubject);

            String[] arrayToFetch = {"dataURL", "imageURL"};
            List<JsonNode> nodes = JsonUtils.findNodesByNames(rootNode, arrayToFetch);

            List<String> urls = new ArrayList<>();
            for (JsonNode currentNode : nodes) {
                for (JsonNode node : currentNode) {
                    if (!node.isTextual()) {
                        throw new IllegalArgumentException("All elements in dataURL/imageURL must be strings");
                    }
                    urls.add(node.asText());
                }
            }

            for (String url : urls) {
                Matcher matcher = URI_PATTERN.matcher(url.trim());
                if (!matcher.matches()) {
                    sb.append("Invalid URL: " + url + ".\n");
                }
            }

        } catch (Exception e) {
            sb.append(e.getMessage());
            e.printStackTrace();
        }

        Assert.assertTrue(sb.toString().length() == 0, sb.toString());
    }
}
