package org.opengis.cite.trainingdmlai10part2.aitrainingdata;

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
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class AITrainingDataTest extends CommonFixture {
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

    @Test(description = "Implements Abstract Test 14 (/conf/aitrainingdata/trainingdata)")
    public void verifyTrainingData() {
        if (!testSubject.isFile()) {
            Assert.assertTrue(testSubject.isFile(), "No file selected. ");
        }

        String schemaToApply = SCHEMA_PATH + "ai_trainingData.json";
        String targetType = "AI_AbstractTrainingData";

        StringBuffer sb = new StringBuffer();

        try {
            BaseJsonSchemaValidatorTest tester = new BaseJsonSchemaValidatorTest();

            JsonSchema schema = tester.getSchema(schemaToApply);
            JsonNode rootNode = tester.getNodeFromFile(testSubject);
            List<JsonNode> targetNode = JsonUtils.findNodesByType(rootNode, targetType);

            try {
                for (JsonNode node : targetNode) {
                    Set<ValidationMessage> errors = schema.validate(node);
                    Iterator it = errors.iterator();
                    while (it.hasNext()) {
                        sb.append("Item " + rootNode + " has error " + it.next() + ".\n");
                    }
                }
            } catch (Exception e) {
                sb.append(e.getMessage());
                e.printStackTrace();
            }
        } catch (Exception e) {
            sb.append(e.getMessage());
            e.printStackTrace();
        }

        Assert.assertTrue(sb.toString().length() == 0, sb.toString());
    }

    @Test(description = "Implements Abstract Test 15 (/conf/aitrainingdata/trainingtypecode)")
    public void verifyTrainingTypeCode() {
        if (!testSubject.isFile()) {
            Assert.assertTrue(testSubject.isFile(), "No file selected. ");
        }

        String schemaToApply = SCHEMA_PATH + "ai_trainingTypeCode.json";
        StringBuffer sb = new StringBuffer();

        try {
            BaseJsonSchemaValidatorTest tester = new BaseJsonSchemaValidatorTest();

            JsonSchema schema = tester.getSchema(schemaToApply);
            JsonNode rootNode = tester.getNodeFromFile(testSubject);

            String[] arrayToFetch = {"trainingType"};

            List<JsonNode> nodes = JsonUtils.findNodesByNames(rootNode, arrayToFetch);

            for (JsonNode targetNode : nodes) {
                String nodeClass = targetNode.getClass().toString();
                if (nodeClass.endsWith("class com.fasterxml.jackson.databind.node.TextNode")) {
                    Set<ValidationMessage> errors = schema.validate(targetNode);
                    Iterator it = errors.iterator();
                    while (it.hasNext()) {
                        sb.append("Item " + targetNode + " has error " + it.next() + ".\n");
                    }
                } else {
                    sb.append("Item " + targetNode + " is not an string.\n");
                }
            }

        } catch (Exception e) {
            sb.append(e.getMessage());
            e.printStackTrace();
        }
        Assert.assertTrue(sb.toString().length() == 0, sb.toString());
    }

    @Test(description = "Implements Abstract Test 16 (/conf/aitrainingdata/eotrainingdata)")
    public void verifyEOTrainingData() {
        if (!testSubject.isFile()) {
            Assert.assertTrue(testSubject.isFile(), "No file selected. ");
        }

        String schemaToApply = SCHEMA_PATH + "ai_eoTrainingData.json";
        String targetType = "AI_EOTrainingData";

        StringBuffer sb = new StringBuffer();

        try {
            BaseJsonSchemaValidatorTest tester = new BaseJsonSchemaValidatorTest();

            JsonSchema schema = tester.getSchema(schemaToApply);
            JsonNode rootNode = tester.getNodeFromFile(testSubject);
            List<JsonNode> targetNode = JsonUtils.findNodesByType(rootNode, targetType);

            try {
                for (JsonNode node : targetNode) {
                    Set<ValidationMessage> errors = schema.validate(node);
                    Iterator it = errors.iterator();
                    while (it.hasNext()) {
                        sb.append("Item " + rootNode + " has error " + it.next() + ".\n");
                    }
                }
            } catch (Exception e) {
                sb.append(e.getMessage());
                e.printStackTrace();
            }

        } catch (Exception e) {
            sb.append(e.getMessage());
            e.printStackTrace();
        }

        Assert.assertTrue(sb.toString().length() == 0, sb.toString());
    }
}
