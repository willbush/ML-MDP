import org.junit.*;
import org.junit.rules.ExpectedException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
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

    @Ignore
    @Test
    public void fromFile_throwsWhenGivenEmptyFile() throws Exception {
        final String emptyFilePath = "resources/dataFormatTest/emptyFile.in";
        expectedException.expect(Main.EmptyFileException.class);
        expectedException.expectMessage(Main.EMPTY_FILE_ERROR + emptyFilePath);
        Main.start(4, 4, emptyFilePath, 0.5);
    }

    @Test
    public void nothing() throws FileNotFoundException {
        State.Builder sb1 = State.Builder(0);
        sb1.put("a1s1", 0.5);
        sb1.put("a1s2", 0.5);
        sb1.put("a2s1", 1.0);
        State s1 = sb1.build();

        State.Builder sb2 = State.Builder(0);
        sb2.put("a1s2", 1.0);
        sb2.put("a2s1", 0.5);
        sb2.put("a2s3", 0.5);
        State s2 = sb2.build();

        State.Builder sb3 = State.Builder(10);
        sb3.put("a1s2", 1.0);
        sb3.put("a2s3", 0.5);
        sb3.put("a2s4", 0.5);
        State s3 = sb3.build();

        State.Builder sb4 = State.Builder(10);
        sb4.put("a1s1", 0.5);
        sb4.put("a1s2", 0.5);
        sb4.put("a2s4", 0.5);
        sb4.put("a2s1", 0.5);
        State s4 = sb4.build();

        List<State> states = new ArrayList<>(4);
        states.add(s1);
        states.add(s2);
        states.add(s3);
        states.add(s4);

        MDP mdp = new MDP(states, DISCOUNT_FACTOR);
        mdp.performValueIteration();

        final String outputPath = "resources/DataSet1/test.out";
        final String expectedOutput = new Scanner(new File(outputPath)).useDelimiter("\\Z").next();
        Assert.assertEquals(expectedOutput, out.toString());
    }
}