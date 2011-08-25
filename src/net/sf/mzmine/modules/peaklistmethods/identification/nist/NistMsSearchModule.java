/*
 * Copyright 2006-2011 The MZmine 2 Development Team
 *
 * This file is part of MZmine 2.
 *
 * MZmine 2 is free software; you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * MZmine 2 is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * MZmine 2; if not, write to the Free Software Foundation, Inc., 51 Franklin St,
 * Fifth Floor, Boston, MA 02110-1301 USA
 */

/* Code created was by or on behalf of Syngenta and is released under the open source license in use for the
 * pre-existing code or project. Syngenta does not assert ownership or copyright any over pre-existing work.
 */

package net.sf.mzmine.modules.peaklistmethods.identification.nist;

import java.io.File;

import net.sf.mzmine.data.PeakList;
import net.sf.mzmine.main.MZmineCore;
import net.sf.mzmine.modules.MZmineModuleCategory;
import net.sf.mzmine.modules.MZmineProcessingModule;
import net.sf.mzmine.parameters.ParameterSet;
import net.sf.mzmine.taskcontrol.Task;

/**
 * NIST MS Search module.
 * 
 * @author Chris Pudney, Syngenta Ltd
 * @version $Revision: 2369 $
 */
public class NistMsSearchModule implements MZmineProcessingModule {

	// System property holding the path to the executable.
	static final String NIST_MS_SEARCH_PATH_PROPERTY = "nist.ms.search.path";

	// NIST MS Search home directory and executable.
	static final String NIST_MS_SEARCH_DIR = System
			.getProperty(NIST_MS_SEARCH_PATH_PROPERTY);
	static final File NIST_MS_SEARCH_EXE = NIST_MS_SEARCH_DIR == null ? null
			: new File(NIST_MS_SEARCH_DIR, "nistms$.exe");

	// Command-line arguments passed to executable.
	private static final String COMMAND_LINE_ARGS = "/par=2 /instrument";

	// Module name.
	private static final String MODULE_NAME = "NIST MS Search";

	// Parameters.
	private final NistMsSearchParameters parameterSet = new NistMsSearchParameters();

	@Override
	public ParameterSet getParameterSet() {
		return parameterSet;
	}

	@Override
	public Task[] runModule(final ParameterSet parameters) {

		PeakList peakLists[] = parameters.getParameter(
				NistMsSearchParameters.peakLists).getValue();
		
		// Construct the command string.
		final String searchCommand = NIST_MS_SEARCH_EXE.getAbsolutePath() + ' '
				+ COMMAND_LINE_ARGS;

		// Process each peak list.
		Task[] tasks = new Task[peakLists.length];
		int i = 0;
		for (final PeakList peakList : peakLists) {

			tasks[i++] = new NistMsSearchTask(peakList, NIST_MS_SEARCH_DIR,
					searchCommand, parameters);
		}

		// Queue and return tasks.
		MZmineCore.getTaskController().addTasks(tasks);

		return tasks;
	}

	@Override
	public MZmineModuleCategory getModuleCategory() {
		return MZmineModuleCategory.IDENTIFICATION;
	}

	@Override
	public String toString() {
		return MODULE_NAME;
	}

}