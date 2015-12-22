package main.java.dao.test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import main.java.dao.impl.NoteDaoImpl;
import main.java.dao.interfaces.NoteDao;
import main.java.model.Note;

public class TestGetAllNotesOfUser {
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
	public void testGetAllNotesOfUser() {
		Note note = new Note();
		note.setId(1l);
		List<Note> notes = new ArrayList<Note>();
		notes.add(note);
		assertEquals(notes, noteDao.getAllNotesOfUser(1l));
	}

}
