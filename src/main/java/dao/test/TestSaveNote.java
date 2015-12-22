package main.java.dao.test;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import main.java.dao.impl.NoteDaoImpl;
import main.java.dao.interfaces.NoteDao;
import main.java.model.Note;
import main.java.model.User;
public class TestSaveNote  {
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
	public void testSaveNoteAndReturnSavedNote() {
		Note note = new Note();
		note.setNoteString("noteOne");
		note.setNoteTitle("noteOne");
		note.setNoteCreatedTime(new Date());
		note.setUser(new User());
		assertEquals(note, noteDao.saveNoteAndReturnSavedNote(note));
	}
}
