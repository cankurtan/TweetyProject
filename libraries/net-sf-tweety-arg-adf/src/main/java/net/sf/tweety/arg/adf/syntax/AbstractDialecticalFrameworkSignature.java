/*
 *  This file is part of "TweetyProject", a collection of Java libraries for
 *  logical aspects of artificial intelligence and knowledge representation.
 *
 *  TweetyProject is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License version 3 as
 *  published by the Free Software Foundation.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 *  Copyright 2019 The TweetyProject Team <http://tweetyproject.org/contact/>
 */
package net.sf.tweety.arg.adf.syntax;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import net.sf.tweety.commons.SingleSetSignature;

/**
 * 
 * @author Mathias Hofer
 *
 */
public class AbstractDialecticalFrameworkSignature extends SingleSetSignature<Argument> {

	/**
	 * 
	 */
	public AbstractDialecticalFrameworkSignature() {
		super();
	}
	
	public AbstractDialecticalFrameworkSignature(Argument a) {
		super(new HashSet<Argument>(Arrays.asList(a)));
	}

	/**
	 * @param formulas
	 */
	public AbstractDialecticalFrameworkSignature(Set<Argument> formulas) {
		super(formulas);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void add(Object obj) {
		if (obj instanceof Argument) {
			formulas.add((Argument) obj);
		}
	}

	@Override
	public AbstractDialecticalFrameworkSignature clone() {
		return new AbstractDialecticalFrameworkSignature(this.formulas);
	}

}
