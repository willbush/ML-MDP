import org.junit.*;
import org.junit.rules.ExpectedException;

import java.io.*;
import java.util.Scanner;

public class MdpTest {
    private final ByteArrayOutputStream out = new ByteArrayOutputStream();
    private static final double DISCOUNT_FACTOR = 0.9;

    @Before
    public void arrange() {
        System.setOut(new PrintStream(out));
    }

    @After
    public void tearDown() {
        out.reset();
    }

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void fromFile_throwsWhenGivenEmptyFile() throws Exception {
        final String emptyFilePath = "resources/dataFormatTest/emptyFile.in";
        expectedException.expect(Main.EmptyFileException.class);
        expectedException.expectMessage(Main.EMPTY_FILE_ERROR + emptyFilePath);
        Main.start(4, emptyFilePath, DISCOUNT_FACTOR);
    }

    @Test
    public void valueIteration_worksOnExtraWhiteSpaceDataSet1() throws IOException {
        final String path = "resources/dataFormatTest/extraWhiteSpace.in";
        final int numOfStates = 4;
        Main.start(numOfStates, path, DISCOUNT_FACTOR);
        final String outputPath = "resources/DataSet1/test.out";
        final String expectedOutput = readFile(outputPath);

        Assert.assertEquals(expectedOutput, out.toString());
    }

    @Test
    public void valueIteration_worksOnDataSet1() throws IOException {
        final String path = "resources/DataSet1/test.in";
        final int numOfStates = 4;
        Main.start(numOfStates, path, DISCOUNT_FACTOR);
        final String outputPath = "resources/DataSet1/test.out";
        final String expectedOutput = readFile(outputPath);

        Assert.assertEquals(expectedOutput, out.toString());
    }

    @Test
    public void valueIteration_worksOnDataSet2() throws IOException {
        final String path = "resources/DataSet2/test2.in";
        final int numOfStates = 10;
        Main.start(numOfStates, path, DISCOUNT_FACTOR);
        final String outputPath = "resources/DataSet2/test2.out";
        final String expectedOutput = readFile(outputPath);

        Assert.assertEquals(expectedOutput, out.toString());
    }

    @Test
    public void valueIteration_worksOnDataSet3() throws IOException {
        final String path = "resources/DataSet3/test3.in";
        final int numOfStates = 4;
        Main.start(numOfStates, path, DISCOUNT_FACTOR);
        final String outputPath = "resources/DataSet3/test3.out";
        final String expectedOutput = readFile(outputPath);

        Assert.assertEquals(expectedOutput, out.toString());
    }

    private static String readFile(String path) throws FileNotFoundException {
        return new Scanner(new File(path)).useDelimiter("\\Z").next();
    }
}