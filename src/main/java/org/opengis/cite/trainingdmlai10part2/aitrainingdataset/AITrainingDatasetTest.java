package org.opengis.cite.trainingdmlai10part2.aitrainingdataset;

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
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class AITrainingDatasetTest extends CommonFixture {
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

    @Test(description = "Implements Abstract Test 11 (/conf/aitrainingdataset/trainingdataset)")
    public void verifyTrainingDataset() {
        if (!testSubject.isFile()) {
            Assert.assertTrue(testSubject.isFile(), "No file selected. ");
        }

        String schemaToApply = SCHEMA_PATH + "ai_trainingDataset.json";
        String targetType = "AI_AbstractTrainingDataset";

        StringBuffer sb = new StringBuffer();

        try {
            BaseJsonSchemaValidatorTest tester = new BaseJsonSchemaValidatorTest();

            JsonSchema schema = tester.getSchema(schemaToApply);
            JsonNode rootNode = tester.getNodeFromFile(testSubject);

            if (rootNode.has("type") && rootNode.get("type").asText().equals(targetType)) {
                Set<ValidationMessage> errors = schema.validate(rootNode);
                Iterator it = errors.iterator();
                while (it.hasNext()) {
                    sb.append("Item " + rootNode + " has error " + it.next() + ".\n");
                }
            } else {
                sb.append("Item " + rootNode + " is not an AI_AbstractTrainingDataset.\n");
            }
        } catch (Exception e) {
            sb.append(e.getMessage());
            e.printStackTrace();
        }
        Assert.assertTrue(sb.toString().length() == 0, sb.toString());
    }

    @Test(description = "Implements Abstract Test 12 (/conf/aitrainingdataset/metricsinliterature)")
    public void verifyMetricsInLiterature() {
        if (!testSubject.isFile()) {
            Assert.assertTrue(testSubject.isFile(), "No file selected. ");
        }

        String schemaToApply = SCHEMA_PATH + "ai_metricsInLiterature.json";
        StringBuffer sb = new StringBuffer();

        try {
            BaseJsonSchemaValidatorTest tester = new BaseJsonSchemaValidatorTest();

            JsonSchema schema = tester.getSchema(schemaToApply);
            JsonNode rootNode = tester.getNodeFromFile(testSubject);

            String[] arrayToFetch = {"metricsInLIT"};

            List<JsonNode> nodes = JsonUtils.findNodesByNames(rootNode, arrayToFetch);

            for (JsonNode targetNode : nodes) {
                String nodeClass = targetNode.getClass().toString();
                if (nodeClass.endsWith("com.fasterxml.jackson.databind.node.ObjectNode")) {
                    Set<ValidationMessage> errors = schema.validate(targetNode);
                    Iterator it = errors.iterator();
                    while (it.hasNext()) {
                        sb.append("Item " + targetNode + " has error " + it.next() + ".\n");
                    }
                } else {
                    sb.append("Item " + targetNode + " is not an object.\n");
                }
            }

        } catch (Exception e) {
            sb.append(e.getMessage());
            e.printStackTrace();
        }
        Assert.assertTrue(sb.toString().length() == 0, sb.toString());
    }

    @Test(description = "Implements Abstract Test 13 (/conf/aitrainingdataset/eotrainingdataset)")
    public void verifyEOTrainingDataset() {
        if (!testSubject.isFile()) {
            Assert.assertTrue(testSubject.isFile(), "No file selected. ");
        }

        String schemaToApply = SCHEMA_PATH + "ai_eoTrainingDataset.json";
        String targetType = "AI_EOTrainingDataset";

        StringBuffer sb = new StringBuffer();

        try {
            BaseJsonSchemaValidatorTest tester = new BaseJsonSchemaValidatorTest();

            JsonSchema schema = tester.getSchema(schemaToApply);
            JsonNode rootNode = tester.getNodeFromFile(testSubject);

            if (rootNode.has("type") && rootNode.get("type").asText().equals(targetType)) {
                Set<ValidationMessage> errors = schema.validate(rootNode);
                Iterator it = errors.iterator();
                while (it.hasNext()) {
                    sb.append("Item " + rootNode + " has error " + it.next() + ".\n");
                }
            } else {
                sb.append("Item " + rootNode + " is not an " + targetType + ".\n");
            }
        } catch (Exception e) {
            sb.append(e.getMessage());
            e.printStackTrace();
        }

        Assert.assertTrue(sb.toString().length() == 0, sb.toString());
    }
}
