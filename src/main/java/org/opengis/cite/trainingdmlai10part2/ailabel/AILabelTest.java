package org.opengis.cite.trainingdmlai10part2.ailabel;

import org.apache.jena.atlas.lib.NotImplemented;
import org.opengis.cite.trainingdmlai10part2.CommonFixture;
import org.opengis.cite.trainingdmlai10part2.SuiteAttribute;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;

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
        throw new NotImplemented();
    }

    @Test(description = "Implements Abstract Test 20 (/conf/ailabel/scenelabel)")
    public void verifySceneLabel() {
        throw new NotImplemented();
    }

    @Test(description = "Implements Abstract Test 21 (/conf/ailabel/objectlabel)")
    public void verifyObjectLabel() {
        throw new NotImplemented();
    }

    @Test(description = "Implements Abstract Test 22 (/conf/ailabel/pixellabel)")
    public void verifyPixelLabel() {
        throw new NotImplemented();
    }

    @Test(description = "Implements Abstract Test 23 (/conf/ailabel/imageformatcode)")
    public void verifyImageFormatCode() {
        throw new NotImplemented();
    }
}
