package labrynth.CS146;

//import javax.annotation.Generated;
import org.junit.Test;
import java.util.*;
import org.junit.Assert;

public class LabyrinthTest {

	private Labyrinth createTestSubject() {
		return new Labyrinth();
	}

	@Test
	public void testReadLabyrinth() throws Exception {
		Labyrinth testSubject;
		String fileName = "maze10.txt";

		// default test
		testSubject = createTestSubject();
		testSubject.readLabyrinth(fileName);
	}

	@Test
	public void testCreateLabyrinth() throws Exception {
		Labyrinth testSubject;
		int size = 0;

		// default test
		testSubject = createTestSubject();
		testSubject.createLabyrinth(size);
	}

	@Test
	public void testPreexistingFiles() throws Exception {
		Labyrinth testSubject;
		testSubject=createTestSubject();

		String[] fileNames = {"maze4.txt","maze6.txt","maze8.txt","maze10.txt","maze20.txt"};

		for(String s: fileNames) {
			testSubject.readLabyrinth(s);
			testSubject.createLabyrinthResultFile("results" + s);
		}
	}
}