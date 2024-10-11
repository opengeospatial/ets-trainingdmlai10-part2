package org.opengis.cite.trainingdmlai10part2.base;

import org.apache.jena.atlas.lib.NotImplemented;
import org.opengis.cite.trainingdmlai10part2.CommonFixture;
import org.opengis.cite.trainingdmlai10part2.SuiteAttribute;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;

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

    @Test(description = "Implements Abstract Test 1 (/conf/base/jsonbasetype/json)")
    public void verifyJson() {
        throw new NotImplemented();
    }

    @Test(description = "Implements Abstract Test 2 (/conf/base/jsonbasetype/datetime)")
    public void validateDateTime() {
        throw new NotImplemented();
    }

    @Test(description = "Implements Abstract Test 3 (/conf/base/jsonbasetype/namedvalue)")
    public void validateNamedValue() {
        throw new NotImplemented();
    }

    @Test(description = "Implements Abstract Test 4 (/conf/base/jsonbasetype/url)")
    public void validateUrl() {
        throw new NotImplemented();
    }
}
