package org.opengis.cite.trainingdmlai10part2.task;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Iterator;
import java.util.Set;

import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;

import org.opengis.cite.trainingdmlai10part2.BaseJsonSchemaValidatorTest;
import org.opengis.cite.trainingdmlai10part2.CommonFixture;
import org.opengis.cite.trainingdmlai10part2.ErrorMessage;
import org.opengis.cite.trainingdmlai10part2.ErrorMessageKeys;
import org.opengis.cite.trainingdmlai10part2.SuiteAttribute;
import org.opengis.cite.validation.RelaxNGValidator;
import org.opengis.cite.validation.ValidationErrorHandler;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.SkipException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.fasterxml.jackson.databind.JsonNode;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.ValidationMessage;

/**
 * Includes various tests of capability 1.
 */
public class TaskClassTests extends CommonFixture {
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

        Object obj = testContext.getSuite().getAttribute(
                SuiteAttribute.TEST_SUBJECT.getName());

        this.testSubject = (File) obj;
    }

    /**
     * Verify that instance documents using the AI_Task JSON objects listed in Table 7 validate against the JSON schema specified in <a href="http://schemas.opengis.net/trainingdml-ai/1.0/ai_task.json">ai_task.json</a>.
     **/
    @Test(description = "Implements Abstract Test 17 (/req/aitask/task)")
    public void validateByTask() {
        throw new SkipException("Not implemented yet.");
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
     * Verify that instance documents using the AI_EOTask JSON objects listed in Table 7 and Table 8 validate against the JSON schema specified in <a href="http://schemas.opengis.net/trainingdml-ai/1.0/ai_eoTask.json">ai_eoTask.json</a>.
     **/
    @Test(description = "Implements Abstract Test 18 AI Task - TBA (/req/aitask/eotask)")
    public void validateByTaskSchema() {
        if (!testSubject.isFile()) {
            Assert.assertTrue(testSubject.isFile(), "No file selected. ");
        }

        BaseJsonSchemaValidatorTest tester = new BaseJsonSchemaValidatorTest();
        String schemaToApply = "/org/opengis/cite/trainingdmlai10part2/jsonschema/ai_eoTask.json";

        boolean valid = false;
        StringBuffer sb = new StringBuffer();

        InputStream inputStream = tester.getClass()
                .getResourceAsStream(schemaToApply);

        try {
            JsonNode schemaNode = tester.getJsonNodeFromStringContent(tester.otherConvertInputStreamToString(inputStream));
            JsonSchema schema = tester.getJsonSchemaFromJsonNodeAutomaticVersion(schemaNode);

            schema.initializeValidators();

            JsonNode node = tester.getJsonNodeFromStringContent(tester.otherConvertInputStreamToString(new FileInputStream(testSubject)));

            String arrayToFetch = "tasks";
            if (node.get(arrayToFetch).size() > 0) {
                for (int indexToFetch = 0; indexToFetch < node.get(arrayToFetch).size(); indexToFetch++) {
                    if (!(node.get(arrayToFetch).get(indexToFetch).getClass().toString().endsWith("com.fasterxml.jackson.databind.node.ObjectNode")))
                        continue;

                    Set<ValidationMessage> errors = schema.validate(node.get(arrayToFetch).get(indexToFetch));
                    Iterator it = errors.iterator();
                    while (it.hasNext()) {
                        sb.append("Item " + indexToFetch + " has error " + it.next() + ".\n");
                    }
                }
            } else {
                Assert.fail("There was no '" + arrayToFetch + "' array found in the file.");
            }
        } catch (Exception e) {
            sb.append(e.getMessage());
            e.printStackTrace();
        }

        Assert.assertTrue(sb.toString().length() == 0, sb.toString());
    }
}
