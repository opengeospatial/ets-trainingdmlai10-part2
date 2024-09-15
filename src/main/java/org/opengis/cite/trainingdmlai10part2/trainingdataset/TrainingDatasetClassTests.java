package org.opengis.cite.trainingdmlai10part2.trainingdataset;

import com.fasterxml.jackson.databind.JsonNode;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.ValidationMessage;
import org.opengis.cite.trainingdmlai10part2.BaseJsonSchemaValidatorTest;
import org.opengis.cite.trainingdmlai10part2.CommonFixture;
import org.opengis.cite.trainingdmlai10part2.SuiteAttribute;
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

    public JsonNode GetSchemaNode(String schemaToApply) throws IOException {
        BaseJsonSchemaValidatorTest tester = new BaseJsonSchemaValidatorTest();

        InputStream inputStream = tester.getClass().getResourceAsStream(schemaToApply);
        JsonNode schemaNode = tester.getJsonNodeFromStringContent(tester.otherConvertInputStreamToString(inputStream));
        JsonSchema schema = tester.getJsonSchemaFromJsonNodeAutomaticVersion(schemaNode);

        schema.initializeValidators();
        return schemaNode;
    }

    /**
     * Checks the behavior of the trim function.
     */
    @Test(description = "Implements Abstract Test 2 (/conf/base/jsonbasetype/datetime)")
    public void validateDateTimeValues() throws Exception {
        if (!testSubject.isFile()) {
            Assert.assertTrue(testSubject.isFile(), "No file selected. ");
        }

        String schemaToApply = "/org/opengis/cite/trainingdmlai10part2/jsonschema/ci_date.json";
        StringBuffer sb = new StringBuffer();
        boolean valid = false;
        try {
            BaseJsonSchemaValidatorTest tester = new BaseJsonSchemaValidatorTest();

            JsonSchema schema = tester.getSchema(schemaToApply);
            JsonNode node = tester.getNodeFromFile(testSubject);

            String[] arrayToFetch = {"createdTime", "updatedTime"};
//            createdTime DateTime [0..1]
//            updatedTime DateTime [0..1]

            List<JsonNode> nodes = new ArrayList<JsonNode>();

            for (String array : arrayToFetch) {
                if (node.has(array)) {
                    nodes.add(node.get(array));
                }
            }

            for (JsonNode targetNode : nodes) {
                if (targetNode.size() > 1) Assert.fail(String.format("Node %s has more than one item.", targetNode));

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
     * Checks the behavior of the trim function.
     */
    @Test(description = "Implements Abstract Test 3 (/conf/base/jsonbasetype/namedvalue)")
    public void ValidateAgainstNamedValueSchema() {
        if (!testSubject.isFile()) {
            Assert.assertTrue(testSubject.isFile(), "No file selected. ");
        }

        String schemaToApply = "/org/opengis/cite/trainingdmlai10part2/jsonschema/namedValue.json";
        StringBuffer sb = new StringBuffer();

        try {
            BaseJsonSchemaValidatorTest tester = new BaseJsonSchemaValidatorTest();

            JsonSchema schema = tester.getSchema(schemaToApply);
            JsonNode node = tester.getNodeFromFile(testSubject);

            String[] arrayToFetch = {"statisticsInfo", "classes"};
            String[] arrayToFetchAtLeastOne = {"metrics"};

            // statisticsInfo [0..*]
            // classes [0..*]
            // metrics [1..*]

            List<JsonNode> nodes = new ArrayList<JsonNode>();

            for (String array : arrayToFetch) {
                if (node.has(array)) {
                    nodes.add(node.get(array));
                }
            }

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

            List<JsonNode> nodesAtLeastOne = new ArrayList<JsonNode>();

            for (String array : arrayToFetchAtLeastOne) {
                if (node.has(array)) {
                    nodesAtLeastOne.add(node.get(array));
                }
            }

            for (JsonNode targetNode : nodesAtLeastOne) {
                if (targetNode.size() > 1) Assert.fail(String.format("Node %s has more than one item.", targetNode));

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
    }

    /**
     * Checks the behavior of the trim function.
     */
    @Test(description = "Implements Abstract Test 4 (/conf/base/jsonbasetype/url)")
    public void VerifyThatURLsAreValid() {
        throw new SkipException("Not implemented yet.");

    }


    /**
     * Checks the behavior of the trim function.
     */
    @Test(description = "Implements Abstract Test 5 (/conf/base/isometadatatype/band)")
    public void validateAgainstBandSchema() {
        throw new SkipException("Not implemented yet.");

    }

    /**
     * Checks the behavior of the trim function.
     */
    @Test(description = "Implements Abstract Test 6 (/conf/base/isometadatatype/extent)")
    public void ValidateAgainstExtentSchema() {
        throw new SkipException("Not implemented yet.");

    }

    /**
     * Checks the behavior of the trim function.
     */
    @Test(description = "Implements Abstract Test 7 (/conf/base/isometadatatype/citation)")
    public void ValidateAgainstCitationSchema() {
        throw new SkipException("Not implemented yet.");

    }

    /**
     * Checks the behavior of the trim function.
     */
    @Test(description = "Implements Abstract Test 8 (/conf/base/isometadatatype/scope)")
    public void ValidateAgainstScopeSchema() {
        throw new SkipException("Not implemented yet.");

    }

    /**
     * Checks the behavior of the trim function.
     */
    @Test(description = "Implements Abstract Test 9 (/conf/base/isoqualitytype/element)")
    public void ValidateAgainstElementQualitySchema() {
        throw new SkipException("Not implemented yet.");

    }

    /**
     * Checks the behavior of the trim function.
     */
    @Test(description = "Implements Abstract Test 10 (/conf/base/geospatialtype/feature)")
    public void validateAgainstFeatureSchema() {
        throw new SkipException("Not implemented yet.");

    }

    /**
     * Checks the behavior of the trim function.
     */
    @Test(description = "Implements Abstract Test 12 (/conf/aitrainingdataset/metricsinliterature)")
    public void validateAgainstMetricsInLiteratureSchema() {
        throw new SkipException("Not implemented yet.");

    }


    /**
     * Checks the behavior of the trim function.
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
                    String schemaToApply = "/org/opengis/cite/trainingdmlai10part2/jsonschema/ai_eoTrainingDataset.json";
                    InputStream inputStream = tester.getClass().getResourceAsStream(schemaToApply);
                    JsonNode schemaNode = tester.getJsonNodeFromStringContent(tester.otherConvertInputStreamToString(inputStream));
                    JsonSchema schema = tester.getJsonSchemaFromJsonNodeAutomaticVersion(schemaNode);
                    schema.initializeValidators();
                    errors = schema.validate(node);
                } else if (node.get("type").asText().equals("AI_AbstractTrainingDataset")) {
                    String schemaToApply = "/org/opengis/cite/trainingdmlai10part2/jsonschema/ai_trainingDataset.json";
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
