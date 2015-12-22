package main.java.dao.test;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import main.java.dao.impl.NoteDaoImpl;
import main.java.dao.interfaces.NoteDao;
import main.java.model.Note;
public class TestUpdateNote {
	private NoteDao noteDao;
	
	@Before
	public void setUp() throws Exception {
		noteDao = new NoteDaoImpl();
	}

	@After
	public void tearDown() throws Exception {
		noteDao = null;
	}
	
	@Test
	public void testUpdateNote() {
		Note note = new Note();
		note.setId(1l);
		note.setNoteString("noteOne");
		assertEquals(note, noteDao.updateAndReturnNote(note));
	}

}
