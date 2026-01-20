package org.opengis.cite.trainingdmlai10part2.trainingdataset;

import com.fasterxml.jackson.databind.JsonNode;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.ValidationMessage;
import org.opengis.cite.trainingdmlai10part2.BaseJsonSchemaValidatorTest;
import org.opengis.cite.trainingdmlai10part2.CommonFixture;
import org.opengis.cite.trainingdmlai10part2.SuiteAttribute;
import org.opengis.cite.trainingdmlai10part2.util.JsonUtils;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.SkipException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Includes various tests of capability 1.
 */
public class TrainingDatasetClassTests extends CommonFixture {

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
     * Checks the behavior of the trim function.
     */
    @Test(description = "Implements Abstract Test 1 (/conf/base/jsonbasetype/json)")
    public void verifyDocumentIsJSON() {
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
    public void validateDateTimeValues() {
        if (!testSubject.isFile()) {
            Assert.assertTrue(testSubject.isFile(), "No file selected. ");
        }

        // TODO: should follow RFC 3339 Section 5.6 but not schema (https://datatracker.ietf.org/doc/html/rfc3339#section-5.6)
        // https://github.com/ethlo/itu?tab=readme-ov-file#parserfc3339
        // https://central.sonatype.com/artifact/com.ethlo.time/itu

        String schemaToApply = SCHEMA_PATH + "dateTime.json";
        StringBuffer sb = new StringBuffer();
        try {
            BaseJsonSchemaValidatorTest tester = new BaseJsonSchemaValidatorTest();

            JsonSchema schema = tester.getSchema(schemaToApply);
            JsonNode rootNode = tester.getNodeFromFile(testSubject);

            String[] arrayToFetch = {"createdTime", "updatedTime"};
            List<JsonNode> nodes = JsonUtils.findNodesByNames(rootNode, arrayToFetch);

            for (JsonNode targetNode : nodes) {
                if (targetNode.isTextual()) {
                    Set<ValidationMessage> errors = schema.validate(targetNode);
                    Iterator it = errors.iterator();
                    while (it.hasNext()) {
                        sb.append("Item has error " + it.next() + ".\n");
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
     * Verify that JSON instance documents claiming conformance to this specification validate against the JSON schema specified in <a href="http://schemas.opengis.net/trainingdml-ai/1.0/namedValue.json">namedValue.json</a>.
     */
    @Test(description = "Implements Abstract Test 3 (/conf/base/jsonbasetype/namedvalue)")
    public void ValidateAgainstNamedValueSchema() {
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
    public void VerifyThatURLsAreValid() {
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

    /**
     * Verify that instance documents using the MD_Band JSON objects validate against the JSON schema specified in <a href="http://schemas.opengis.net/trainingdml-ai/1.0/md_band.json">md_band.json</a>.
     */
    @Test(description = "Implements Abstract Test 5 (/conf/base/isometadatatype/band)")
    public void validateAgainstBandSchema() throws IOException {
        if (!testSubject.isFile()) {
            Assert.assertTrue(testSubject.isFile(), "No file selected. ");
        }

        String schemaToApply = SCHEMA_PATH + "md_band.json";
        StringBuffer sb = new StringBuffer();

        try {
            BaseJsonSchemaValidatorTest tester = new BaseJsonSchemaValidatorTest();

            JsonSchema schema = tester.getSchema(schemaToApply);
            JsonNode rootNode = tester.getNodeFromFile(testSubject);

            String[] arrayToFetch = {"bands"};

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
     * Verify that instance documents using the EX_Extent JSON objects validate against the JSON schema specified in <a href="http://schemas.opengis.net/trainingdml-ai/1.0/ex_extent.json">ex_extent.json</a>.
     */
    @Test(description = "Implements Abstract Test 6 (/conf/base/isometadatatype/extent)")
    public void ValidateAgainstExtentSchema() throws IOException {
        if (!testSubject.isFile()) {
            Assert.assertTrue(testSubject.isFile(), "No file selected. ");
        }

        String schemaToApply = SCHEMA_PATH + "ex_extent.json";
        StringBuffer sb = new StringBuffer();

        try {
            BaseJsonSchemaValidatorTest tester = new BaseJsonSchemaValidatorTest();

            JsonSchema schema = tester.getSchema(schemaToApply);
            JsonNode node = tester.getNodeFromFile(testSubject);

            String[] arrayToFetch = {"extent"};
            List<JsonNode> nodes = JsonUtils.findNodesByNames(node, arrayToFetch);

            for (JsonNode targetNode : nodes) {
                Set<ValidationMessage> errors = schema.validate(targetNode);
                Iterator it = errors.iterator();
                while (it.hasNext()) {
                    sb.append("Item " + targetNode.asText() + " has error " + it.next() + ".\n");
                }
            }

        } catch (Exception e) {
            sb.append(e.getMessage());
            e.printStackTrace();
        }
        Assert.assertTrue(sb.toString().length() == 0, sb.toString());
    }

    /**
     * Verify that instance documents using the CI_Citation JSON objects validate against the JSON schema specified in <a href="http://schemas.opengis.net/trainingdml-ai/1.0/ci_citation.json">ci_citation.json</a>.
     */
    @Test(description = "Implements Abstract Test 7 (/conf/base/isometadatatype/citation)")
    public void ValidateAgainstCitationSchema() {
        throw new SkipException("Not implemented yet.");

    }

    /**
     * Verify that instance documents using the MD_Scope JSON objects validate against the JSON schema specified in <a href="http://schemas.opengis.net/trainingdml-ai/1.0/md_scope.json">md_scope.json</a>.
     */
    @Test(description = "Implements Abstract Test 8 (/conf/base/isometadatatype/scope)")
    public void ValidateAgainstScopeSchema() {
        throw new SkipException("Not implemented yet.");

    }

    /**
     * Verify that instance documents using the QualityElement JSON objects validate against the JSON schema specified in <a href="http://schemas.opengis.net/trainingdml-ai/1.0/qualityElement.json">qualityElement.json</a>.
     */
    @Test(description = "Implements Abstract Test 9 (/conf/base/isoqualitytype/element)")
    public void ValidateAgainstElementQualitySchema() {
        throw new SkipException("Not implemented yet.");

    }

    /**
     * Verify that instance documents using the Feature JSON objects validate against the JSON schema specified in <a href="https://geojson.org/schema/Feature.json">Feature.json</a>.
     */
    @Test(description = "Implements Abstract Test 10 (/conf/base/geospatialtype/feature)")
    public void validateAgainstFeatureSchema() {
        throw new SkipException("Not implemented yet.");

    }

    /**
     * Verify that instance documents using the AI_MetricsInLiterature JSON objects listed in Table 3 validate against the JSON schema specified in <a href="http://schemas.opengis.net/trainingdml-ai/1.0/ai_metricsInLiterature.json">ai_metricsInLiterature.json</a>.
     */
    @Test(description = "Implements Abstract Test 12 (/conf/aitrainingdataset/metricsinliterature)")
    public void validateAgainstMetricsInLiteratureSchema() {
        throw new SkipException("Not implemented yet.");

    }


    /**
     * Abstract Test 11: Verify that instance documents using the AI_TrainingDataset JSON objects listed in Table 2 validate against the JSON schema specified in <a href="http://schemas.opengis.net/trainingdml-ai/1.0/ai_trainingDataset.json">ai_trainingDataset.json</a>.
     * <br/><br/>
     * Abstract Test 13: Verify that instance documents using the AI_EOTrainingDataset JSON objects listed in Table 2 validate against the JSON schema specified in <a href="http://schemas.opengis.net/trainingdml-ai/1.0/ai_eoTrainingDataset.json">ai_eoTrainingDataset.json</a>.
     */
    @Test(description = "Implements Abstract Test 11 (/conf/aitrainingdataset/trainingdataset) and Abstract Test 13 (/conf/aitrainingdataset/eotrainingdataset)", priority = -1)
    public void validateByTrainingDatasetSchema() {
        if (!testSubject.isFile()) {
            Assert.assertTrue(testSubject.isFile(), "No file selected. ");
        }

        BaseJsonSchemaValidatorTest tester = new BaseJsonSchemaValidatorTest();

        boolean valid = false;
        StringBuffer sb = new StringBuffer();

        try {
            Set<ValidationMessage> errors = null;

            JsonNode node = tester.getJsonNodeFromStringContent(tester.otherConvertInputStreamToString(new FileInputStream(testSubject)));

            if (node.has("type")) {
                if (node.get("type").asText().equals("AI_EOTrainingDataset")) {
                    String schemaToApply = SCHEMA_PATH + "ai_eoTrainingDataset.json";
                    InputStream inputStream = tester.getClass().getResourceAsStream(schemaToApply);
                    JsonNode schemaNode = tester.getJsonNodeFromStringContent(tester.otherConvertInputStreamToString(inputStream));
                    JsonSchema schema = tester.getJsonSchemaFromJsonNodeAutomaticVersion(schemaNode);
                    schema.initializeValidators();
                    errors = schema.validate(node);
                } else if (node.get("type").asText().equals("AI_AbstractTrainingDataset")) {
                    String schemaToApply = SCHEMA_PATH + "ai_trainingDataset.json";
                    InputStream inputStream = tester.getClass().getResourceAsStream(schemaToApply);
                    JsonNode schemaNode = tester.getJsonNodeFromStringContent(tester.otherConvertInputStreamToString(inputStream));
                    JsonSchema schema = tester.getJsonSchemaFromJsonNodeAutomaticVersion(schemaNode);
                    schema.initializeValidators();
                    errors = schema.validate(node);
                } else {
                    sb.append("No recognised type in the root object. ");
                }
            } else {
                sb.append("No 'type' key found in the root object. ");
            }

            Iterator it = errors.iterator();
            while (it.hasNext()) {
                sb.append(" " + it.next() + ".\n");
            }
        } catch (Exception e) {
            sb.append(e.getMessage());
            e.printStackTrace();
        }

        Assert.assertTrue(sb.toString().length() == 0, sb.toString());
    }
}
