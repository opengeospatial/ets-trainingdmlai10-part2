package org.opengis.cite.trainingdmlai10part2.ailabel;

import com.fasterxml.jackson.databind.JsonNode;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.ValidationMessage;
import org.apache.jena.atlas.lib.NotImplemented;
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

public class AILabelTest extends CommonFixture {
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

    @Test(description = "Implements Abstract Test 19 (/conf/ailabel/label)")
    public void verifyLabel() {
        if (!testSubject.isFile()) {
            Assert.assertTrue(testSubject.isFile(), "No file selected. ");
        }

        String schemaToApply = SCHEMA_PATH + "ai_label.json";
        StringBuffer sb = new StringBuffer();

        try {
            BaseJsonSchemaValidatorTest tester = new BaseJsonSchemaValidatorTest();

            JsonSchema schema = tester.getSchema(schemaToApply);
            JsonNode rootNode = tester.getNodeFromFile(testSubject);

            String[] arrayToFetch = {"labels"};

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

    @Test(description = "Implements Abstract Test 20 (/conf/ailabel/scenelabel)")
    public void verifySceneLabel() {
        if (!testSubject.isFile()) {
            Assert.assertTrue(testSubject.isFile(), "No file selected. ");
        }

        String schemaToApply = SCHEMA_PATH + "ai_sceneLabel.json";
        String targetType = "AI_SceneLabel";

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
                        sb.append("Item " + node + " has error " + it.next() + ".\n");
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

    @Test(description = "Implements Abstract Test 21 (/conf/ailabel/objectlabel)")
    public void verifyObjectLabel() {
        if (!testSubject.isFile()) {
            Assert.assertTrue(testSubject.isFile(), "No file selected. ");
        }

        String schemaToApply = SCHEMA_PATH + "ai_objectLabel.json";
        String targetType = "AI_ObjectLabel";

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
                        sb.append("Item " + node + " has error " + it.next() + ".\n");
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

    @Test(description = "Implements Abstract Test 22 (/conf/ailabel/pixellabel)")
    public void verifyPixelLabel() {
        if (!testSubject.isFile()) {
            Assert.assertTrue(testSubject.isFile(), "No file selected. ");
        }

        String schemaToApply = SCHEMA_PATH + "ai_pixelLabel.json";
        String targetType = "AI_PixelLabel";

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
                        sb.append("Item " + node + " has error " + it.next() + ".\n");
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

    @Test(description = "Implements Abstract Test 23 (/conf/ailabel/imageformatcode)")
    public void verifyImageFormatCode() {
        if (!testSubject.isFile()) {
            Assert.assertTrue(testSubject.isFile(), "No file selected. ");
        }

        String schemaToApply = SCHEMA_PATH + "ai_imageFormatCode.json";
        StringBuffer sb = new StringBuffer();

        try {
            BaseJsonSchemaValidatorTest tester = new BaseJsonSchemaValidatorTest();

            JsonSchema schema = tester.getSchema(schemaToApply);
            JsonNode rootNode = tester.getNodeFromFile(testSubject);

            String[] arrayToFetch = {"imageFormat"};

            List<JsonNode> nodes = JsonUtils.findNodesByNames(rootNode, arrayToFetch);
            for (JsonNode targetNode : nodes) {
                // it's an array of strings
                for (int i = 0; i < targetNode.size(); i++) {
                    JsonNode currentNode = targetNode.get(i);
                    if (currentNode.isTextual()) {
                        Set<ValidationMessage> errors = schema.validate(currentNode);
                        Iterator it = errors.iterator();
                        while (it.hasNext()) {
                            sb.append("Item " + currentNode + " has error " + it.next() + ".\n");
                        }
                    } else {
                        sb.append("Item " + i + " is not a string.\n");
                    }
                }
            }
        } catch (Exception e) {
            sb.append(e.getMessage());
            e.printStackTrace();
        }
        Assert.assertTrue(sb.toString().length() == 0, sb.toString());
    }
}
