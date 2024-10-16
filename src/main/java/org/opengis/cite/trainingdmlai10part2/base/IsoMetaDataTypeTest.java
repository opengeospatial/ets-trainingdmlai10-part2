package org.opengis.cite.trainingdmlai10part2.base;

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
import org.testng.SkipException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class IsoMetaDataTypeTest extends CommonFixture {
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
     * Verify that instance documents using the MD_Band JSON objects validate against the JSON schema specified in <a href="http://schemas.opengis.net/trainingdml-ai/1.0/md_band.json">md_band.json</a>.
     */
    @Test(description = "Implements Abstract Test 5 (/conf/base/isometadatatype/band)")
    public void verifyBand() throws IOException {
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
    public void verifyExtent() throws IOException {
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
    public void verifyCitation() {
        throw new SkipException("Not implemented yet.");

    }

    /**
     * Verify that instance documents using the MD_Scope JSON objects validate against the JSON schema specified in <a href="http://schemas.opengis.net/trainingdml-ai/1.0/md_scope.json">md_scope.json</a>.
     */
    @Test(description = "Implements Abstract Test 8 (/conf/base/isometadatatype/scope)")
    public void verifyScope() {
        throw new SkipException("Not implemented yet.");

    }
}
